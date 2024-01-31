package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 报销单费用信息
 *
 * @author shuangquan.chen
 * @date 2023-12-26 20:08
 */
@Data
public class Expense {

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "type")
    private Integer type;

    @JSONField(name = "cost_category")
    private CostCategory costCategory;

    @JSONField(name = "reason")
    private String reason;

    @JSONField(name = "total_amount")
    private Double totalAmount;

    @JSONField(name = "cost_attributions")
    private List<CostAttribution> costAttributions;

    @JSONField(name = "invoices")
    private List<Invoice> invoices;

    @JSONField(name = "cost_custom_fields")
    private List<CustomControl> costCustomFields;

}
