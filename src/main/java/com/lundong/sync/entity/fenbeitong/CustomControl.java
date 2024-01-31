package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-12-26 20:07
 */
@Data
public class CustomControl {

    /**
     * 类型
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 标题
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 值详情不是固定格式
     */
    @JSONField(name = "detail")
    private String detail;
}
