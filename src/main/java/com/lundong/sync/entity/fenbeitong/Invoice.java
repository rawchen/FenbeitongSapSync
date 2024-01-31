package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author shuangquan.chen
 * @date 2023-12-27 10:13
 */
@Data
public class Invoice {

    /**
     * ID
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 类型
     */
    @JSONField(name = "type")
    private Integer type;

    @JSONField(name = "code")
    private String code;

    @JSONField(name = "number")
    private String number;

    @JSONField(name = "issued_time")
    private String issuedTime;

    @JSONField(name = "seller_name")
    private String sellerName;

    @JSONField(name = "seller_tax_code")
    private String sellerTaxCode;

    @JSONField(name = "buyer_name")
    private String buyerName;

    @JSONField(name = "buyer_tax_code")
    private String buyerTaxCode;

    @JSONField(name = "total_amount")
    private Double totalAmount;

    @JSONField(name = "tax_amount")
    private Double taxAmount;

    @JSONField(name = "tax_rate")
    private Double taxRate;

    @JSONField(name = "exclude_tax_amount")
    private Double excludeTaxAmount;

    @JSONField(name = "used_amount")
    private Double usedAmount;

    @JSONField(name = "url")
    private String url;

    @JSONField(name = "deductible_tax")
    private Double deductibleTax;

    @JSONField(name = "detail")
    private List<InvoiceDetail> detail;

}
