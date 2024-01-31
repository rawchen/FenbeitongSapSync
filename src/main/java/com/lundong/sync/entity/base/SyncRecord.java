package com.lundong.sync.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2024-01-09 15:41
 */
@Data
public class SyncRecord {

    @JSONField(name = "审批实例ID")
    private String reimbId;

    @JSONField(name = "审批标题")
    private String title;

    @JSONField(name = "申请编号")
    private String reimbCode;


    @JSONField(name = "发起人")
    private String proposerName;

    @JSONField(name = "审批发起日期")
    private String createTime;

    @JSONField(name = "审批完成时间")
    private String completeTime;

    @JSONField(name = "同步状态")
    private String syncType;

    @JSONField(name = "错误信息")
    private String errorInfo;

    @JSONField(name = "重试日期")
    private String generationDate;

    @JSONField(name = "重试状态")
    private String hasGenerate;

    @JSONField(name = "重试错误信息")
    private String retryErrorInfo;



}
