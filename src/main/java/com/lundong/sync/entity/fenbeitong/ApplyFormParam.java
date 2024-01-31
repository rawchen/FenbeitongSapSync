package com.lundong.sync.entity.fenbeitong;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * @author shuangquan.chen
 * @date 2023-12-27 10:42
 */
@Data
public class ApplyFormParam {

    /**
     * 申请单类型
     */
    @JsonAlias("apply_type")
    private String applyType;

    /**
     * 申请单状态
     */
    @JsonAlias("apply_state")
    private String applyState;

    /**
     * 原申请单ID，审批单状态为变更时有值
     */
    @JsonAlias("root_apply_id")
    private String rootApplyId;

    /**
     * 三方系统原申请单ID，审批单状态为变更时有值
     */
    @JsonAlias("third_root_apply_id")
    private String thirdRootApplyId;

    /**
     * 分贝通申请单ID
     */
    @JsonAlias("apply_id")
    private String applyId;

    /**
     * 三方系统新申请单ID
     */
    @JsonAlias("third_apply_id")
    private String thirdApplyId;
}
