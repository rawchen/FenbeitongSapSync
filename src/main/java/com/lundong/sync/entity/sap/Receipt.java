package com.lundong.sync.entity.sap;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2024-01-05 14:13
 */
@Data
public class Receipt {

    @JSONField(name = "ExpenseReportExpenseTypeCode")
    private String expenseReportExpenseTypeCode;

    @JSONField(name = "Amount")
    private String amount;

    @JSONField(name = "currencyCode")
    private String currencyCode;

    @JSONField(name = "Date")
    private String date;

    @JSONField(name = "CS_Note")
    private String csNote;

    @JSONField(name = "BaseNumberValue")
    private String baseNumberValue;
}
