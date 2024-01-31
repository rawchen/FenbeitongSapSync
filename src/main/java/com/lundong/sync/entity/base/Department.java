package com.lundong.sync.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-11-29 17:22
 */
@Data
public class Department {

    @JSONField(name = "ou_code")
    private String ouCode;

    @JSONField(name = "ou_name")
    private String ouName;

    @JSONField(name = "ou_id")
    private String ouId;
}
