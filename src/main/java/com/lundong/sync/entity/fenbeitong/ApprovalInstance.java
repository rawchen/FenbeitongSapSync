package com.lundong.sync.entity.fenbeitong;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author RawChen
 * @date 2023-08-09 10:35
 */
@Data
public class ApprovalInstance {

	@JSONField(name = "url")
	private String url;

	@JSONField(name = "reimb_id")
	private String reimbId;

	@JSONField(name = "reimb_code")
	private String reimbCode;

	/**
	 * 单据类型
	 * 26：自定义报销申请单
	 */
	@JSONField(name = "type")
	private Integer type;

	@JSONField(name = "form_id")
	private String formId;

	@JSONField(name = "currency_code")
	private String currencyCode;

	@JSONField(name = "total_amount")
	private Double totalAmount;

	@JSONField(name = "payment_amount")
	private Double paymentAmount;

	@JSONField(name = "invoice_total_amount")
	private Double invoiceTotalAmount;

	@JSONField(name = "expense_number")
	private Integer expenseNumber;

	@JSONField(name = "invoice_number")
	private Integer invoiceNumber;

	@JSONField(name = "apply_state")
	private Integer applyState;

	@JSONField(name = "ticket_state")
	private Integer ticketState;

	@JSONField(name = "apply_reason")
	private String applyReason;

	@JSONField(name = "apply_remark")
	private String applyRemark;

	@JSONField(name = "create_time")
	private String createTime;

	@JSONField(name = "complete_time")
	private String completeTime;

	@JSONField(name = "invoice_label")
	private Integer invoiceLabel;

	/**
	 * 关联申请单列表
	 */
	@JSONField(name = "apply")
	private List<Apply> apply;

	/**
	 * 申请单提交人信息
	 */
	@JSONField(name = "proposer")
	private User proposer;

	/**
	 * 报销人信息
	 */
	@JSONField(name = "user")
	private User user;

	/**
	 * 报销单费用信息
	 */
	@JSONField(name = "expenses")
	private List<Expense> expenses;

	/**
	 * 自定义表单自定义控件列表
	 */
	@JSONField(name = "custom_controls")
	private List<CustomControl> customControls;

}
