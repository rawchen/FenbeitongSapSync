package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-12-27 09:45
 */
@Data
public class CostCategory {

    /**
     * ID
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 编码
     */
    @JSONField(name = "code")
    private String code;

    /**
     * 名称
     */
    @JSONField(name = "name")
    private String name;
}
