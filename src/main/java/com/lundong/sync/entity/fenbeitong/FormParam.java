package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author shuangquan.chen
 * @date 2023-12-25 13:39
 */
@Data
public class FormParam {

    /**
     * 付款单付款状态枚举值
     * 80：交易成功
     * 3：部分成功
     * 21：交易失败
     * 84：退回汇款
     */
    @JSONField(name = "payment_state")
    private Integer paymentState;

    /**
     * 失败、退回汇款原因
     */
    @JSONField(name = "fail_reason")
    private String failReason;

    /**
     * 报销单信息
     */
    @JSONField(name = "applies")
    private List<Apply> applies;

    /**
     * 支付失败报销单信息
     */
    @JSONField(name = "fail_applies")
    private List<Apply> failApplies;

    /**
     * 分贝通付款人ID
     */
    @JSONField(name = "payer_id")
    private String payerId;

    /**
     * 三方系统付款人ID
     */
    @JSONField(name = "third_payer_id")
    private String thirdPayerId;

}
