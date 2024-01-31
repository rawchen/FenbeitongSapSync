package com.lundong.sync.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-11-29 17:17
 */
@Data
public class CostType {

    @JSONField(name = "费用类别编码")
    private String costTypeCode;

    @JSONField(name = "费用类别名称")
    private String costTypeName;

    @JSONField(name = "SAP费用类型编码")
    private String sapCostTypeCode;

}
