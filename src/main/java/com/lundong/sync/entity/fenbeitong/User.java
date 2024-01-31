package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 分贝通用户
 *
 * @author shuangquan.chen
 * @date 2023-12-26 19:56
 */
@Data
public class User {

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "phone")
    private String phone;

    @JSONField(name = "department_id")
    private String departmentId;

    @JSONField(name = "department_name")
    private String departmentName;

    @JSONField(name = "dept_all_path_name")
    private String deptAllPathName;
}
