package com.lundong.sync.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lundong.sync.config.Constants;
import com.lundong.sync.entity.ApprovalInstance;
import com.lundong.sync.entity.BitableParam;
import com.lundong.sync.entity.feishu.FeishuUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author RawChen
 * @date 2023-06-25 14:33
 */
@Slf4j
public class SignUtil {

    /**
     * 飞书自建应用获取tenant_access_token
     */
    public static String getAccessToken(String appId, String appSecret) {

//        if (!StrUtil.isEmpty(Constants.ACCESS_TOKEN)) {
//            return Constants.ACCESS_TOKEN;
//        }
        JSONObject object = new JSONObject();
        object.put("app_id", appId);
        object.put("app_secret", appSecret);
        String resultStr = "";
        JSONObject resultObject = null;
        for (int i = 0; i < 3; i++) {
            try {
                HttpResponse execute = HttpRequest.post("https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal")
                        .form(object)
                        .execute();
                resultStr = execute.body();
                execute.close();
                if (StringUtils.isNotEmpty(resultStr)) {
                    resultObject = JSON.parseObject(resultStr);
                }
            } catch (Exception e) {
                log.error("获取tenant_access_token异常，重试 {} 次, message: {}, body: {}", i + 1, e.getMessage(), resultStr);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ecp) {
                    log.error("sleep异常", ecp);
                }
            }
            if (resultObject != null && resultObject.getInteger("code") == 0) {
                break;
            } else {
                log.error("获取tenant_access_token失败，重试 {} 次, body: {}", i + 1, resultStr);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ecp) {
                    log.error("sleep异常", ecp);
                }
            }
        }
        // 重试完检测
        if (resultObject == null || resultObject.getInteger("code") != 0) {
            log.error("重试3次获取tenant_access_token后都失败");
            return "";
        } else {
            String tenantAccessToken = resultObject.getString("tenant_access_token");
            if (tenantAccessToken != null) {
                return tenantAccessToken;
            }
        }
        log.error("access_token获取不成功: {}", resultStr);
        return "";
    }

    /**
     * 获取飞书用户姓名
     *
     * @param accessToken
     * @return
     */
    public static String getFeishuUserName(String accessToken, String userId) {

        Map<String, Object> param = new HashMap<>();
        param.put("user_id_type", "user_id");
        param.put("department_id_type", "department_id");
        FeishuUser feishuUser = new FeishuUser();
        try {
            String resultStr = HttpRequest.get("https://open.feishu.cn/open-apis/contact/v3/users/" + userId)
                    .header("Authorization", "Bearer " + accessToken)
                    .form(param)
                    .execute()
                    .body();
//            log.info("获取飞书用户姓名接口: {}", resultStr);
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            if (jsonObject.getInteger("code") == 0) {
                JSONObject user = jsonObject.getJSONObject("data").getJSONObject("user");
                if (user != null) {
                    feishuUser.setUserId(user.getString("user_id"));
                    feishuUser.setName(user.getString("name"));
                }
                return feishuUser.getName();
            } else {
                log.error("获取飞书用户姓名接口失败: {}", resultStr);
                return "";
            }
        } catch (Exception e) {
            log.error("获取飞书用户姓名接口异常: ", e);
            return "";
        }
    }

    /**
     * 获取飞书用户
     *
     * @return
     */
    public static String getFeishuUserName(String userId) {
        return getFeishuUserName(Constants.ACCESS_TOKEN, userId);
    }

    /**
     * 获取单个审批实例详情
     *
     * @param accessToken
     * @param instanceId
     * @return
     */
    public static ApprovalInstance approvalInstanceDetail(String accessToken, String instanceId) {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id_type", "user_id");
            String resultStr = HttpRequest.get("https://open.feishu.cn/open-apis/approval/v4/instances/" + instanceId)
                    .header("Authorization", "Bearer " + accessToken)
                    .form(object)
                    .execute().body();
            log.info("获取单个审批实例详情接口: {}", StringUtil.subLog(resultStr));
            if (StringUtils.isNotEmpty(resultStr)) {
                JSONObject resultObject = JSON.parseObject(resultStr);
                if (resultObject.getInteger("code") == 0) {
                    return resultObject.getJSONObject("data").toJavaObject(ApprovalInstance.class);
                } else {
                    log.error("获取单个审批实例详情接口出错: {}", resultStr);
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("获取单个审批实例详情异常: ", e);
            return null;
        }
        return null;
    }

    /**
     * 获取单个审批实例详情
     *
     * @param instanceId
     * @return
     */
    public static ApprovalInstance approvalInstanceDetail(String instanceId) {
        return approvalInstanceDetail(Constants.ACCESS_TOKEN, instanceId);
    }

    /**
     * 列出记录
     *
     * @param accessToken
     * @param appToken
     * @param tableId
     * @return
     */
    public static <T> List<T> findBaseList(String accessToken, String appToken, String tableId, Class<T> tClass) {
        List<T> results = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("page_size", 500);
        boolean hasMore = true;

        while (hasMore) {
            JSONObject jsonObject = null;
            String resultStr = "";
            for (int i = 0; i < 3; i++) {
                try {
                    HttpResponse response = HttpRequest.get("https://open.feishu.cn/open-apis/bitable/v1/apps/" + appToken + "/tables/" + tableId + "/records")
                            .header("Authorization", "Bearer " + accessToken)
                            .form(param)
                            .execute();
                    resultStr = response.body();
                    response.close();
//                    log.info("列出记录接口: {}", StringUtil.subLog(resultStr));
                    jsonObject = JSON.parseObject(resultStr);
                } catch (Exception e) {
                    log.error("列出记录接口请求异常，重试 {} 次, message: {}, body: {}", i + 1, e.getMessage(), resultStr);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ecp) {
                        log.error("sleep异常", ecp);
                    }
                }
                if (jsonObject != null && jsonObject.getInteger("code") != 0) {
                    log.error("列出记录接口请求失败，重试 {} 次, body: {}", i + 1, resultStr);
                } else if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                    break;
                }
            }
            if (jsonObject == null || jsonObject.getInteger("code") != 0) {
                log.error("重试3次列出记录接口调用失败，appToken: {}, tableId: {}", appToken, tableId);
                return Collections.emptyList();
            }
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray items = data.getJSONArray("items");
            for (int i = 0; i < items.size(); i++) {
                JSONObject records = items.getJSONObject(i).getJSONObject("fields");
                T testEntity;
                testEntity = JSONObject.toJavaObject(records, tClass);
                StringUtil.bracketReplace(testEntity);
                StringUtil.clearSpecialSymbols(testEntity);
                results.add(testEntity);
            }
            if ((boolean) data.get("has_more")) {
                param.put("page_token", data.getString("page_token"));
            } else {
                hasMore = false;
            }
        }
        return results;
    }

    /**
     * 获取多维表列表
     *
     * @param appToken
     * @param tableId
     * @return
     */
    public static <T> List<T> findBaseList(String appToken, String tableId, Class<T> tClass) {
        return findBaseList(Constants.ACCESS_TOKEN, appToken, tableId, tClass);
    }

    /**
     * 列出记录
     *
     * @param accessToken
     * @param bitableParam
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T findBaseRecord(String accessToken, BitableParam bitableParam, Class<T> tClass) {
        T result;
        try {
            String resultStr = HttpRequest.get("https://open.feishu.cn/open-apis/bitable/v1/apps/" +
                            bitableParam.getAppToken() +
                            "/tables/" + bitableParam.getTableId() + "/records/" + bitableParam.getRecordId())
                    .header("Authorization", "Bearer " + accessToken)
                    .execute()
                    .body();
            log.info("检索记录接口: {}", StringUtil.subLog(resultStr));
            JSONObject jsonObject = JSON.parseObject(resultStr);
            if (jsonObject.getInteger("code") != 0) {
                log.error("检索记录接口调用失败");
                return null;
            }
            JSONObject fields = jsonObject.getJSONObject("data").getJSONObject("record").getJSONObject("fields");
            result = JSONObject.toJavaObject(fields, tClass);
            StringUtil.clearSpecialSymbols(result);
        } catch (Exception e) {
            log.info("检索记录接口调用异常", e);
            return null;
        }
        return result;
    }

    public static <T> T findBaseRecord(BitableParam bitableParam, Class<T> tClass) {
        return findBaseRecord(Constants.ACCESS_TOKEN, bitableParam, tClass);
    }

    public static void updateHasGenerate(String save, BitableParam bitableParam) {
        try {
            String statusStr = "成功";
            if (!"success".equals(save)) {
                statusStr = "失败";
            }
            String successBody = "{\"fields\": {\"重试状态\":\"" + statusStr + "\",\"重试错误信息\":\"\",\"重试日期\":\"" + DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss") + "\"}}";
            String failBody = "{\"fields\": {\"重试状态\":\"" + statusStr + "\",\"重试错误信息\":\"" + StringUtil.escape(save) + "\",\"重试日期\":\"" + DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss") + "\"}}";
            String resultStr = HttpRequest.put("https://open.feishu.cn/open-apis/bitable/v1/apps/" +
                            bitableParam.getAppToken() +
                            "/tables/" + bitableParam.getTableId() + "/records/" + bitableParam.getRecordId())
                    .header("Authorization", "Bearer " + Constants.ACCESS_TOKEN)
                    .body("success".equals(save) ? successBody : failBody)
                    .execute()
                    .body();
            log.info("更新记录接口: {}", resultStr);
            JSONObject jsonObject = JSON.parseObject(resultStr);
            if (jsonObject.getInteger("code") != 0) {
                log.error("更新记录接口失败: {}", resultStr);
            }
        } catch (Exception e) {
            log.error("更新记录接口异常: ", e);
        }
    }

    /**
     * 多维表格新增多条记录
     *
     * @param json
     * @return
     */
    public static List<String> batchInsertRecord(String json, String appToken, String tableId) {
        return batchInsertRecord(Constants.ACCESS_TOKEN, json, appToken, tableId);
    }

    /**
     * 多维表格新增多条记录
     *
     * @param accessToken
     * @param json
     * @return
     */
    private static List<String> batchInsertRecord(String accessToken, String json, String appToken, String tableId) {
        List<String> recordIds = new ArrayList<>();
        String resultStr = "";
        JSONObject resultObject = null;
        for (int i = 0; i < 3; i++) {
            try {
                resultStr = HttpRequest.post("https://open.feishu.cn/open-apis/bitable/v1/apps/" + appToken + "/tables/" + tableId + "/records/batch_create")
                        .header("Authorization", "Bearer " + accessToken)
                        .body(json)
                        .execute()
                        .body();
                log.info("resultStr: {}", StringUtil.subLog(resultStr));
                if (StringUtils.isNotEmpty(resultStr)) {
                    resultObject = (JSONObject) JSON.parse(resultStr);
                    if (resultObject.getInteger("code") != 0) {
                        log.error("新增多条记录失败，重试 {} 次, body: {}", i + 1, resultStr);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ecp) {
                            log.error("sleep异常", ecp);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("新增多条记录接口调用异常，重试 {} 次, message: {}, body: {}", i + 1, e.getMessage(), resultStr);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ecp) {
                    log.error("sleep异常", ecp);
                }
            }
            if (resultObject != null && resultObject.getInteger("code") == 0) {
                break;
            }
        }
        if (resultObject == null || resultObject.getInteger("code") != 0) {
            log.error("重试3次新增多条记录接口调用后都失败");
            return Collections.emptyList();
        } else {
            JSONObject data = (JSONObject) resultObject.get("data");
            JSONArray records = (JSONArray) data.get("records");
            for (int j = 0; j < records.size(); j++) {
                JSONObject jsonObject = records.getJSONObject(j);
                String recordId = jsonObject.getString("record_id");
                recordIds.add(recordId);
            }
            return recordIds;
        }
    }

    public static String insertRecord(String str, String appToken, String tableId) {
        return insertRecord(Constants.ACCESS_TOKEN, str, appToken, tableId);
    }

    public static String insertRecord(String accessToken, String str, String appToken, String tableId) {
        String resultStr = "";
        JSONObject resultObject = null;
        for (int i = 0; i < 3; i++) {
            try {
                resultStr = HttpRequest.post("https://open.feishu.cn/open-apis/bitable/v1/apps/" + appToken + "/tables/" + tableId + "/records")
                        .header("Authorization", "Bearer " + accessToken)
                        .body(str)
                        .execute()
                        .body();
                log.info("resultStr: {}", StringUtil.subLog(resultStr));
                if (StringUtils.isNotEmpty(resultStr)) {
                    resultObject = (JSONObject) JSON.parse(resultStr);
                    if (resultObject.getInteger("code") != 0) {
                        log.error("新增记录失败，重试 {} 次, body: {}", i + 1, resultStr);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ecp) {
                            log.error("sleep异常", ecp);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("新增记录接口调用异常，重试 {} 次, message: {}, body: {}", i + 1, e.getMessage(), resultStr);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ecp) {
                    log.error("sleep异常", ecp);
                }
            }
            if (resultObject != null && resultObject.getInteger("code") == 0) {
                break;
            }
        }
        if (resultObject == null || resultObject.getInteger("code") != 0) {
            log.error("重试3次新增记录接口调用后都失败");
            return "";
        } else {
            // todo 是否记录今天插入成功，防止一天多次执行
            JSONObject data = (JSONObject) resultObject.get("data");
            JSONObject record = (JSONObject) data.get("record");
            return record.getString("record_id");
        }
    }
}
