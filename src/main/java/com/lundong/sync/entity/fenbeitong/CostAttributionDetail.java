package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-12-27 10:09
 */
@Data
public class CostAttributionDetail {

    /**
     * ID
     */
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "code")
    private String code;

    @JSONField(name = "third_id")
    private String thirdId;

    /**
     * 名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 费用归属分摊比例
     */
    @JSONField(name = "weight")
    private Double weight;

    /**
     * 费用归属分摊金额
     */
    @JSONField(name = "amount")
    private Double amount;

}
