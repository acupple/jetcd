package org.mokey.acupple.jetcd;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class Node {

	private String key;
	private String value;
	private long createdIndex;
	private long modifiedIndex;
	private long ttl;
	private boolean dir;
	private List<Node> nodes;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getCreatedIndex() {
		return createdIndex;
	}

	public void setCreatedIndex(long createdIndex) {
		this.createdIndex = createdIndex;
	}

	public long getModifiedIndex() {
		return modifiedIndex;
	}

	public void setModifiedIndex(long modifiedIndex) {
		this.modifiedIndex = modifiedIndex;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public boolean isDir() {
		return dir;
	}

	public void setDir(boolean dir) {
		this.dir = dir;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this, true);
	}
	
	public String key(){
		return getKey();
	}

}
