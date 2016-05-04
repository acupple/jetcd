package org.mokey.acupple.jetcd;

import com.alibaba.fastjson.JSON;

public class EtcdResponse {

	private String action;
	private Node node;
	private Error error;

	private int responseCode;
	private boolean created;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public boolean isCreated() {
		return created;
	}

	public void setCreated(boolean created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this, true);
	}
	
	public boolean successful(){
		return this.getError() == null;
	}
	
	public Node node(){
		return getNode();
	}

}
