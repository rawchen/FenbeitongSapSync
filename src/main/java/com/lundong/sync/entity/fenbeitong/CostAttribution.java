package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author shuangquan.chen
 * @date 2023-12-27 10:06
 */
@Data
public class CostAttribution {

    /**
     * 类型
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 类型
     */
    @JSONField(name = "details")
    private List<CostAttributionDetail> details;

}
