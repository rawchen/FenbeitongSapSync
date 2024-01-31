package com.lundong.sync.entity.sap;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author shuangquan.chen
 * @date 2024-01-05 13:58
 */
@Data
public class SapParam {

    @JSONField(name = "EmployeeID")
    private String employeeId;

    @JSONField(name = "TypeCode")
    private String typeCode;

    @JSONField(name = "StartDateTime")
    private String startDateTime;

    @JSONField(name = "endDateTime")
    private String endDateTime;

    @JSONField(name = "Description")
    private String description;

    @JSONField(name = "Note")
    private String note;

    @JSONField(name = "AccountingBusinessTransactionDate")
    private String accountingBusinessTransactionDate;

    @JSONField(name = "Receipt")
    private List<Receipt> receipts;

    @JSONField(name = "AccountingCodingBlockAssignment")
    private List<Assignment> assignments;

    @JSONField(name = "AccountingBusinessTransactionDateActionCode")
    private String accountingCodingBlockDistributionActionCode;

}
