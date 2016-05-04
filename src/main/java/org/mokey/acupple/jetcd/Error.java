package org.mokey.acupple.jetcd;

import com.alibaba.fastjson.JSON;

public class Error {

	private int errorCode;
	private String cause;
	private String message;
	private long index;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this, true);
	}

}
