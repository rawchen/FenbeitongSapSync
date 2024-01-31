package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-12-25 13:42
 */
@Data
public class Apply {

    /**
     * 分贝通申请单ID
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 三方系统申请单ID
     */
    @JSONField(name = "third_id")
    private String thirdId;

    /**
     * 申请单类型
     */
    @JSONField(name = "type")
    private Integer type;
}
