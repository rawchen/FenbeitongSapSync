package com.lundong.sync.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-11-29 17:22
 */
@Data
public class Employee {

    @JSONField(name = "sap_employee_id")
    private String sapEmployeeId;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "mobile_no")
    private String mobileNo;

    @JSONField(name = "city")
    private String city;

    @JSONField(name = "default_ou_id")
    private String defaultOuId;

    @JSONField(name = "default_ou_name")
    private String defaultOuName;

    @JSONField(name = "company_id")
    private Integer companyId;
}
