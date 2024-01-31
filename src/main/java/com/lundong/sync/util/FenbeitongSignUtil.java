package com.lundong.sync.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lundong.sync.config.Constants;
import com.lundong.sync.entity.fenbeitong.ApprovalInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shuangquan.chen
 * @date 2023-12-25 13:58
 */
@Slf4j
public class FenbeitongSignUtil {

    /**
     * 分贝通获取access_token
     */
    public static String getFenbeitongAccessToken(String appId, String appKey) {

//        if (!StrUtil.isEmpty(Constants.ACCESS_TOKEN)) {
//            return Constants.ACCESS_TOKEN;
//        }
        JSONObject object = new JSONObject();
        object.put("app_id", appId);
        object.put("app_key", appKey);
        String resultStr = "";
        JSONObject resultObject = null;
        for (int i = 0; i < 3; i++) {
            try {
                HttpResponse execute = HttpRequest.post(StringUtil.fenbeitongDomain() + "/openapi/auth/getToken")
                        .body(object.toJSONString())
                        .execute();
                resultStr = execute.body();
                execute.close();
                if (StringUtils.isNotEmpty(resultStr)) {
                    resultObject = JSON.parseObject(resultStr);
                    if (resultObject.getInteger("code") != 0) {
                        log.error("获取access_token失败，重试 {} 次, body: {}", i + 1, resultStr);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ecp) {
                            log.error("sleep异常", ecp);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("获取access_token异常，重试 {} 次, message: {}, body: {}", i + 1, e.getMessage(), resultStr);
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
        // 重试完检测
        if (resultObject == null || resultObject.getInteger("code") != 0) {
            log.error("重试3次获取access_token后都失败");
            return "";
        } else {
            String accessToken = resultObject.getString("data");
            if (accessToken != null) {
                return accessToken;
            }
        }
        log.error("access_token获取不成功: {}", resultStr);
        return "";
    }

    /**
     * 获取报销单详情v2
     *
     * @param accessToken
     * @param reimbId
     * @param reimbCode
     * @return
     */
    public static ApprovalInstance reimbursementDetail(String accessToken, String reimbId, String reimbCode) {
        try {
            JSONObject object = new JSONObject();
            if (StrUtil.isNotEmpty(reimbId)) {
                object.put("reimb_id", reimbId);
            }
            if (StrUtil.isNotEmpty(reimbCode)) {
                object.put("reimb_code", reimbCode);
            }
            String resultStr = HttpRequest.post(StringUtil.fenbeitongDomain() + "/openapi/reimbursement/v2/detail")
                    .header("access-token", accessToken)
                    .body(object.toJSONString())
                    .execute().body();
            log.info("获取报销单详情v2接口: {}", resultStr);
            if (StringUtils.isNotEmpty(resultStr)) {
                JSONObject resultObject = JSON.parseObject(resultStr);
                if (resultObject.getInteger("code") == 0) {
                    return resultObject.getJSONObject("data").toJavaObject(ApprovalInstance.class);
                } else {
                    log.error("获取报销单详情v2接口出错: {}", resultStr);
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("获取报销单详情v2接口异常: ", e);
            return null;
        }
        return null;
    }

    /**
     * 获取报销单详情v2
     *
     * @param reimbId
     * @param reimbCode
     * @return
     */
    public static ApprovalInstance reimbursementDetail(String reimbId, String reimbCode) {
        return reimbursementDetail(Constants.ACCESS_TOKEN_FENBEITONG, reimbId, reimbCode);
    }
}
