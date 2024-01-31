package com.lundong.sync.entity;

/**
 * 封装结果
 *
 * @author RawChen
 * @since 2021-10-06 20:50
 */
public class R {
	private Integer code;
	private String msg;
	private Object data;

	private R(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
		this.data = null;
	}

	private R(Integer code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static R ok(String msg, Object data) {
		return new R(0, msg, data);
	}

	public static R ok(String msg) {
		return new R(0, msg);
	}

	public static R ok() {
		return new R(0, "success");
	}

	public static R ok(Object data) {
		return new R(0, "success", data);
	}

	public static R fail(String msg) {
		return new R(400, msg);
	}

	public static R fail() {
		return new R(400, "fail");
	}

	public static R error(String msg) {
		return new R(500, msg);
	}

	public static R error() {
		return new R(500, "error");
	}

	public static R create(Integer code, String msg, Object data) {
		return new R(code, msg, data);
	}

	public static R create(Integer code, String msg) {
		return new R(code, msg);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "R{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}
}