package com.syo.platform.controller;

public class APIPageRequest {
	
	private int pageNo = 1;
	
	private int pageSize = 15;

	public APIPageRequest() {
		super();
	}

	public APIPageRequest(int pageNo, int pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	

}
