package com.hong.bean;

import java.io.Serializable;

/**
 * 接口返回通用类
 */
public class Result implements Serializable {

	private static final long serialVersionUID = -2804195259517755050L;

	public static final int SUCCESS_CODE = 0;
	public static final String ERROR_MSG = "fail";
	
	private int code; // 状态码

	private String message; // 说明信息

	private String result; // result结果，快速判断

	private Object data; // 结果数据
	
	public Result() {
		this(SUCCESS_CODE, "成功","success",null);
	}

	public Result(int code, String msg) {
		this(code, msg, ERROR_MSG, null);
	}

	public Result(Object data) {
		this(SUCCESS_CODE, "成功", "success", data);
	}

	public Result(int code, String message, String result, Object data) {
		this.code = code;
		this.message = message;
		this.result = result;
		this.data=data;
	}

	public String getCode() {
		return String.valueOf(code);
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Result [code=" + code + ", message=" + message + ", result=" + result + ", data=" + data + "]";
	}

}
