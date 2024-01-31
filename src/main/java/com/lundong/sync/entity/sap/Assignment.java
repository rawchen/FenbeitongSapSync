package com.lundong.sync.entity.sap;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2024-01-05 14:29
 */
@Data
public class Assignment {
    @JSONField(name = "Percent")
    private String percent;

    @JSONField(name = "AccountingCodingBlockTypeCode")
    private String accountingCodingBlockTypeCode;

    @JSONField(name = "CostCentreID")
    private String costCentreId;

    @JSONField(name = "ActionCode")
    private String actionCode;

    @JSONField(name = "ProjectTaskKey")
    private String projectTaskKey;
}
