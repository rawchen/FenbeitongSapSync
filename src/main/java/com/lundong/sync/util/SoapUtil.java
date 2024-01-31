package com.lundong.sync.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lundong.sync.config.Constants;
import com.lundong.sync.entity.sap.Assignment;
import com.lundong.sync.entity.sap.Receipt;
import com.lundong.sync.entity.sap.SapParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author shuangquan.chen
 * @date 2024-01-03 15:43
 */
@Slf4j
public class SoapUtil {


    public static String getWebServiceAndSoap(String url, String isClass, String isMethod, StringBuffer sendSoapString) throws IOException {
        String soap = sendSoapString.toString();
        if (soap == null) {
            return null;
        }
        URL soapUrl = new URL(url);
        URLConnection conn = soapUrl.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Length",
                Integer.toString(soap.length()));
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        // 调用的接口方法是
        conn.setRequestProperty(isClass, isMethod);
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
        osw.write(soap);
        osw.flush();
        osw.close();
        // 获取webserivce返回的流
        InputStream is = conn.getInputStream();
        if (is != null) {
            byte[] bytes = new byte[0];
            bytes = new byte[is.available()];
            is.read(bytes);
            String str = new String(bytes);
            return str;
        } else {
            return null;
        }
    }

    public static String save(SapParam sapParam) {

        String resultStr = "";
        int exceptionNumber = 0;
        String exceptionStr = "";
        for (int i = 0; i < 3; i++) {
            try {
                String username = "xxx";
                String password = "xxx";
                String basicStr = "Basic " + Base64.encodeBase64String((username + ":" + password).getBytes());

                String detailList = "";
                for (Receipt receipt : sapParam.getReceipts()) {
                    String baseNumberValueString = "";
                    // 当费用类型是ENT时（业务招待费），需要多传一个字段，招待人数（2024.01.11）
                    if ("ENT".equals(receipt.getExpenseReportExpenseTypeCode()) && StrUtil.isNotEmpty(receipt.getBaseNumberValue())) {
                        baseNumberValueString += "<BaseNumberValue>" + receipt.getBaseNumberValue() + "</BaseNumberValue>";
                    }
                    detailList += "            <Receipt>\n" +
                            "               <ExpenseReportExpenseTypeCode>" + receipt.getExpenseReportExpenseTypeCode() + "</ExpenseReportExpenseTypeCode>\n" +
                            "               <Amount currencyCode=\"" + receipt.getCurrencyCode() + "\">" + receipt.getAmount() + "</Amount>\n" +
                            "               <Date>" + receipt.getDate() + "</Date>\n" +
                            baseNumberValueString +
                            "               <CS_Note>" + receipt.getCsNote() + "</CS_Note>\n" +
                            "            </Receipt>\n";
                }

                String accountingCodingBlockAssignment = "";
                for (Assignment assignment : sapParam.getAssignments()) {
                    String project = "";
                    String CostCentre = "";
                    if (assignment.getCostCentreId() != null) {
                        CostCentre = "<CostCentreID>" + assignment.getCostCentreId() + "</CostCentreID>\n";
                    }

                    if (assignment.getProjectTaskKey() != null) {
                        project = "<ProjectTaskKey>\n" +
                                "                      <TaskID>" + assignment.getProjectTaskKey() + "</TaskID>\n" +
                                "                  </ProjectTaskKey>";
                    }
                    accountingCodingBlockAssignment += "<AccountingCodingBlockAssignment ActionCode=\"" + assignment.getActionCode() + "\">\n" +
                            "                  <Percent>" + assignment.getPercent() + "</Percent>\n" +
                            "                  <AccountingCodingBlockTypeCode>" + assignment.getAccountingCodingBlockTypeCode() + "</AccountingCodingBlockTypeCode>\n" +
                            CostCentre +
                            project +
                            "               </AccountingCodingBlockAssignment>";
                }
                String description = "";
                if (StrUtil.isNotEmpty(sapParam.getDescription())) {
                    description += "            <Description>" + sapParam.getDescription() + "</Description>\n";
                }
                String startAndEndDateTime = "";
                if (StrUtil.isNotEmpty(sapParam.getStartDateTime()) && StrUtil.isNotEmpty(sapParam.getEndDateTime())) {
                    startAndEndDateTime += "            <Period><StartDateTime>" + sapParam.getStartDateTime() + "</StartDateTime>\n";
                    startAndEndDateTime += "            <EndDateTime>" + sapParam.getEndDateTime() + "</EndDateTime></Period>\n";
                }
                String text01 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:glob=\"http://sap.com/xi/SAPGlobal20/Global\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <glob:ExpenseReportCreateRequest_sync>\n" +
                        "         <BasicMessageHeader></BasicMessageHeader>\n" +
                        "         <ExpenseReport>\n" +
                        "            <EmployeeID>" + sapParam.getEmployeeId() + "</EmployeeID>\n" +
                        "            <TypeCode>" + sapParam.getTypeCode() + "</TypeCode>\n" +
                        startAndEndDateTime +
                        description +
                        "            <Note>" + sapParam.getNote() + "</Note>\n" +
                        "            <AccountingBusinessTransactionDate>" + sapParam.getAccountingBusinessTransactionDate() + "</AccountingBusinessTransactionDate>\n" +

                        detailList +
                        "            <AccountingCodingBlockDistribution ActionCode=\"01\">" +
                        accountingCodingBlockAssignment +
                        "            </AccountingCodingBlockDistribution>" +
                        "         </ExpenseReport>\n" +
                        "      </glob:ExpenseReportCreateRequest_sync>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";


                log.info("XML参数：{}", text01);
                //            if (true) {
                //                return "test";
                //            }
                String text02 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:glob=\"http://sap.com/xi/SAPGlobal20/Global\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <glob:ExpenseReportCreateRequest_sync>\n" +
                        "         <BasicMessageHeader></BasicMessageHeader>\n" +
                        "         <ExpenseReport>\n" +
                        "            <EmployeeID>10008</EmployeeID>\n" +
                        "            <TypeCode>02</TypeCode>\n" +
                        "            <Description>测试费用报销单接口，第2次测试</Description>\n" +
                        "            <Note>飞书审批单号：20238888888</Note>\n" +
                        "            <AccountingBusinessTransactionDate>2024-01-03</AccountingBusinessTransactionDate>\n" +
                        "            <Receipt>\n" +
                        "              <ExpenseReportExpenseTypeCode>AIRP</ExpenseReportExpenseTypeCode>\n" +
                        "               <Amount currencyCode=\"CNY\">71</Amount>\n" +
                        "               <Date>2023-12-02</Date>\n" +
                        "        <CS_Note>加班打车回家</CS_Note>\n" +
                        "            </Receipt>\n" +
                        "            <Receipt>\n" +
                        "               <ExpenseReportExpenseTypeCode>GAS</ExpenseReportExpenseTypeCode>\n" +
                        "               <Amount currencyCode=\"CNY\">105</Amount>\n" +
                        "               <Date>2024-01-03</Date>\n" +
                        "        <CS_Note>京东上购买打印纸</CS_Note>\n" +
                        "            </Receipt>\n" +
                        "           <AccountingCodingBlockDistribution ActionCode=\"01\">\n" +
                        "               <AccountingCodingBlockAssignment ActionCode=\"01\">\n" +
                        "                  <Percent>50</Percent>\n" +
                        "                  <AccountingCodingBlockTypeCode>CC</AccountingCodingBlockTypeCode>\n" +
                        "                  <CostCentreID>19400</CostCentreID>\n" +
                        "               </AccountingCodingBlockAssignment>\n" +
                        "               <AccountingCodingBlockAssignment ActionCode=\"02\">\n" +
                        "                  <Percent>50</Percent>\n" +
                        "                  <AccountingCodingBlockTypeCode>CC</AccountingCodingBlockTypeCode>\n" +
                        "                  <CostCentreID>12200</CostCentreID>\n" +
                        "               </AccountingCodingBlockAssignment>\n" +
                        " </AccountingCodingBlockDistribution>\n" +
                        "         </ExpenseReport>\n" +
                        "      </glob:ExpenseReportCreateRequest_sync>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
                //            out.write(text02);

                String text03 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:glob=\"http://sap.com/xi/SAPGlobal20/Global\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <glob:ExpenseReportCreateRequest_sync>\n" +
                        "         <BasicMessageHeader></BasicMessageHeader>\n" +
                        "         <ExpenseReport>\n" +
                        "            <EmployeeID>10002</EmployeeID>\n" +
                        "            <TypeCode>02</TypeCode>\n" +
                        "            <Description>测试费用报销单接口，第3次测试</Description>\n" +
                        "            <Note>飞书审批单号：333</Note>\n" +
                        "            <AccountingBusinessTransactionDate>2024-01-03</AccountingBusinessTransactionDate>\n" +
                        "            <Receipt>\n" +
                        "              <ExpenseReportExpenseTypeCode>AIRP</ExpenseReportExpenseTypeCode>\n" +
                        "               <Amount currencyCode=\"CNY\">71</Amount>\n" +
                        "               <Date>2024-01-03</Date>\n" +
                        "       <CS_Note>加班打车回家</CS_Note>\n" +
                        "            </Receipt>\n" +
                        "            <Receipt>\n" +
                        "               <ExpenseReportExpenseTypeCode>GAS</ExpenseReportExpenseTypeCode>\n" +
                        "               <Amount currencyCode=\"CNY\">105</Amount>\n" +
                        "               <Date>2024-01-03</Date>\n" +
                        "       <CS_Note>京东上购买打印纸</CS_Note>\n" +
                        "            </Receipt>\n" +
                        "            <AccountingCodingBlockDistribution ActionCode=\"01\">\n" +
                        "               <AccountingCodingBlockAssignment ActionCode=\"01\">\n" +
                        "                  <Percent>100</Percent>\n" +
                        "                  <AccountingCodingBlockTypeCode>PRO</AccountingCodingBlockTypeCode>\n" +
                        "                  <ProjectTaskKey>\n" +
                        "                      <TaskID>D2021001</TaskID>\n" +
                        "                  </ProjectTaskKey>\n" +
                        "               </AccountingCodingBlockAssignment>\n" +
                        "            </AccountingCodingBlockDistribution>\n" +
                        "         </ExpenseReport>\n" +
                        "      </glob:ExpenseReportCreateRequest_sync>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
                //            out.write(text03);


                HttpResponse execute = HttpRequest.post(Constants.SAP_URL)
                        .body(text01, ContentType.TEXT_XML.getValue())
                        //                    .header("Content-Type", "text/xml;charset=utf-8")
                        .header("Authorization", basicStr)
                        .execute();
                resultStr = execute.body();
                execute.close();
            } catch (Exception e) {
                exceptionNumber++;
                exceptionStr = e.getMessage();
                log.error("调用SAP保存单据接口出现异常，重试 {} 次, message: {}, body: {}", i + 1, e.getMessage(), resultStr);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ecp) {
                    log.error("sleep异常", ecp);
                }
            }
            if (StrUtil.isNotEmpty(resultStr) && resultStr.contains("创建操作成功")) {
                break;
            }
        }

        // 重试完检测
        log.info("result: {}", resultStr);
        if (!resultStr.contains("创建操作成功")) {
            log.error("重试3次调用SAP保存单据接口后都失败");
            if (resultStr.isEmpty() && exceptionNumber == 3) {
                // 网络问题三次失败且body为空
                return exceptionStr;
            } else {
                return resultStr;
            }
        } else {
            return "success";
        }
    }
}
