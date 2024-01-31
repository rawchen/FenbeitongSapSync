package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-12-27 10:19
 */
@Data
public class InvoiceDetail {

    /**
     * 分贝通发票费用税率
     */
    @JSONField(name = "rate")
    private String rate;

    /**
     * 分贝通发票费用税额
     */
    @JSONField(name = "amount")
    private String amount;

    /**
     * 名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 含税额
     */
    @JSONField(name = "include_tax_amount")
    private String includeTaxAmount;

    /**
     * 不含税额
     */
    @JSONField(name = "exclude_tax_amount")
    private String excludeTaxAmount;

}
