package com.lundong.sync.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lundong.sync.config.Constants;
import com.lundong.sync.entity.BitableParam;
import com.lundong.sync.entity.R;
import com.lundong.sync.entity.base.Employee;
import com.lundong.sync.entity.base.SyncRecord;
import com.lundong.sync.entity.fenbeitong.*;
import com.lundong.sync.entity.sap.Assignment;
import com.lundong.sync.entity.sap.Receipt;
import com.lundong.sync.entity.sap.SapParam;
import com.lundong.sync.util.FenbeitongSignUtil;
import com.lundong.sync.util.SignUtil;
import com.lundong.sync.util.SoapUtil;
import com.lundong.sync.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author RawChen
 * @date 2023-03-02 11:19
 */
@Slf4j
@RestController
public class EventController {

    /**
     * 当付款单付款状态发生变化时，分贝通将付款单付款状态信息推送给客户方接口
     *
     * @param applyFormParam
     */
    @PostMapping("/reimbursement")
    public R form(@RequestBody ApplyFormParam applyFormParam) {
        new Thread(() -> {
            log.info("申请单事件接收: {}", applyFormParam);
            // 根据报销单ID获取报销单明细
            // 组成SAP单据体结构传入SAP
            // 自定义报销申请单

            if (StrUtil.isNotEmpty(applyFormParam.getApplyState()) && !"4".equals(applyFormParam.getApplyState())) {
                return;
            }

            ApprovalInstance approvalInstance = FenbeitongSignUtil.reimbursementDetail(applyFormParam.getApplyId(), null);
            String save = save(approvalInstance);

            SyncRecord record = new SyncRecord();
            record.setReimbId(approvalInstance.getReimbId());
            record.setTitle(approvalInstance.getUser().getName() + "_" + (Constants.APPROVAL_FORM_ID_TRAVEL.equals(approvalInstance.getFormId()) ? "差旅报销" : "非差旅报销"));
            record.setReimbCode(approvalInstance.getReimbCode());
            record.setProposerName(approvalInstance.getProposer().getName());
            record.setCreateTime(approvalInstance.getCreateTime());
            record.setCompleteTime(StringUtil.nullIsEmpty(approvalInstance.getCompleteTime()));
            if ("success".equals(save)) {
                record.setSyncType("已同步");
                record.setErrorInfo("");
            } else {
                record.setSyncType("同步失败");
                record.setErrorInfo(save);
            }

            JSONObject body = new JSONObject();
            body.put("fields", JSONObject.toJSON(record));
            String itemJson = body.toJSONString();
            // 处理字段为中文
            itemJson = StringUtil.processChineseTitleOrder(itemJson);
            log.info("json: {}", StringUtil.subLog(itemJson));
            SignUtil.insertRecord(itemJson, Constants.APP_TOKEN, Constants.TABLE_04);
        }).start();
        return R.ok();
    }

    public String save(ApprovalInstance approvalInstance) {
        // 获取多维表格员工映射表
        List<Employee> listTable01 = Constants.LIST_TABLE_01;
        SapParam sapParam = new SapParam();
        sapParam.setDescription(StringUtil.subLenString(approvalInstance.getApplyRemark()));
        if (Constants.APPROVAL_FORM_ID_TRAVEL.equals(approvalInstance.getFormId())) {
            // 差旅
            sapParam.setTypeCode("01");
            // 差旅开始结束时间
            for (CustomControl customControl : approvalInstance.getCustomControls()) {
                if ("差旅行程".equals(customControl.getTitle())) {
                    JSONArray jsonArray = JSONArray.parseArray(customControl.getDetail());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject != null) {
                        String fromTimeStr = jsonObject.getString("from_time");
                        String toTimeStr = jsonObject.getString("to_time");
                        int fromTimeType = jsonObject.getInteger("from_time_type");
                        int toTimeType = jsonObject.getInteger("to_time_type");
                        sapParam.setStartDateTime(StringUtil.startAndEndDateTimeFormat(fromTimeStr, fromTimeType, 1));
                        sapParam.setEndDateTime(StringUtil.startAndEndDateTimeFormat(toTimeStr, toTimeType, 2));
                    } else {
                        log.error("差旅报销单无开始结束时间");
                    }
                }
            }
        } else if (Constants.APPROVAL_FORM_ID_NOT_TRAVEL.equals(approvalInstance.getFormId())) {
            // 非差旅
            sapParam.setTypeCode("02");
        }

        String employeeId = "";
        for (Employee employee : listTable01) {
            if (approvalInstance.getUser().getPhone().equals(employee.getMobileNo())) {
                employeeId = employee.getSapEmployeeId();
            }
        }
        if (StrUtil.isEmpty(employeeId)) {
            log.error("员工映射表找不到手机号为：{} 的员工", approvalInstance.getUser().getPhone());
            return "fail: 员工映射表找不到手机号为：" + approvalInstance.getUser().getPhone() + " 的员工";
        }
        sapParam.setEmployeeId(employeeId);
        sapParam.setNote(approvalInstance.getReimbCode());
        sapParam.setAccountingBusinessTransactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 明细
        List<Receipt> receiptList = new ArrayList<>();
        List<Expense> expenses = approvalInstance.getExpenses();
        //            List<CostAttribution> costAttributionList = new ArrayList<>();
        for (Expense expens : expenses) {
            Receipt receipt = new Receipt();
            receipt.setDate(expens.getInvoices().get(0).getIssuedTime());
            receipt.setAmount(expens.getTotalAmount().toString());
            receipt.setCsNote(StringUtil.subLenString(expens.getReason()));
            receipt.setCurrencyCode("CNY");
            receipt.setExpenseReportExpenseTypeCode(StringUtil.getCostCategory(expens.getCostCategory().getCode()));

            // 自定义字段的招待人数
            for (CustomControl costCustomField : expens.getCostCustomFields()) {
                if ("招待人数".equals(costCustomField.getTitle())) {
                    receipt.setBaseNumberValue(costCustomField.getDetail());
                    break;
                }
            }
            receiptList.add(receipt);

            //                List<CostAttribution> costAttributions = expens.getCostAttributions();
            //                costAttributionList.addAll(costAttributions);
        }
        sapParam.setReceipts(receiptList);

        // 默认取第一个expenses的费用归属（成本中心）列表
        List<Assignment> assignmentList = new ArrayList<>();
        boolean isTask = false;
        List<CostAttribution> costAttributions = expenses.get(0).getCostAttributions();
        for (CostAttribution costAttribution : costAttributions) {
            if (costAttribution.getType() == 2 && !costAttribution.getDetails().isEmpty()) {
                isTask = true;
                break;
            }
        }
        if (!isTask) {
            costAttributions = costAttributions.stream().filter(n -> n.getType() == 1).collect(Collectors.toList());
            // 默认有多个费用归属类型为部门时取cost_attributions第一个
            List<CostAttributionDetail> details = costAttributions.get(0).getDetails();
            for (int i = 0; i < details.size(); i++) {
                Assignment assignment = new Assignment();
                assignment.setPercent(String.valueOf(details.get(i).getWeight()));
                assignment.setAccountingCodingBlockTypeCode("CC");
                assignment.setActionCode(String.format("%02d", i + 1));
                // todo 取第一笔费用成本中code值；然后取ou id；然后取报销人company_id+ou id（拼接）
                //                    assignment.setCostCentreId(StringUtil.getCostCentreId(String deptCode, String sapEmployeeId));
                assignment.setCostCentreId(StringUtil.getCostCentreId(details.get(i).getCode(), approvalInstance.getUser().getName()));
                assignmentList.add(assignment);
            }
        } else {
            // 用户的费用里面选择有至少一个任务
            costAttributions = costAttributions.stream().filter(n -> n.getType() == 2).collect(Collectors.toList());
            List<CostAttributionDetail> details = costAttributions.get(0).getDetails();
            for (int i = 0; i < details.size(); i++) {
                Assignment assignment = new Assignment();
                assignment.setPercent(String.valueOf(details.get(i).getWeight()));
                assignment.setAccountingCodingBlockTypeCode("PRO");
                assignment.setActionCode(String.format("%02d", i + 1));
                //                    assignment.setCostCentreId(StringUtil.getCostCentreId(details.get(0).getCode(), approvalInstance.getUser().getName()));
                assignment.setProjectTaskKey(details.get(i).getCode());
                assignmentList.add(assignment);
            }
        }
        sapParam.setAssignments(assignmentList);

        // 保存SAP、插入记录
        String save = SoapUtil.save(sapParam);
        return save;
    }

    /**
     * 当付款单付款状态发生变化时，分贝通将付款单付款状态信息推送给客户方接口
     *
     * @param recordId
     * @param reimbId
     */
    @GetMapping("/reimbursement_bitable")
    public R reimbursement_bitable(@RequestParam("record_id") String recordId, @RequestParam("reimb_id") String reimbId) {
        log.info("重推参数: appToken: {}, tableId: {}, recordId: {}", Constants.APP_TOKEN, Constants.TABLE_04, recordId);
        if (StrUtil.isNotEmpty(recordId) && StrUtil.isNotEmpty(reimbId)) {
            BitableParam bitableParam = new BitableParam();
            bitableParam.setAppToken(Constants.APP_TOKEN);
            bitableParam.setTableId(Constants.TABLE_04);
            bitableParam.setRecordId(recordId);
            SyncRecord baseRecord01 = SignUtil.findBaseRecord(bitableParam, SyncRecord.class);
            if ("成功".equals(baseRecord01.getHasGenerate())) {
                log.info("已成功生成过该单据: {}, bitableParam: {}", reimbId, bitableParam);
            } else {
                ApprovalInstance approvalInstance = FenbeitongSignUtil.reimbursementDetail(reimbId, null);
                String save = save(approvalInstance);
//                String save = "success";
                SignUtil.updateHasGenerate(save, bitableParam);
            }
        }
        return R.ok();
    }

    @GetMapping("/t")
    public R test() {
        new Thread(() -> {
            try {
//                if (1 == 1) {
//                    return;
//                }
                Thread.sleep(5000);
                log.info("1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        log.info("返回");
        return R.ok();
    }
}