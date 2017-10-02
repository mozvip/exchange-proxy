package com.github.mozvip.exchange.core;

import okhttp3.Request;

public class ActiveSyncRequest {
	
	private ActiveSyncQueryString query;
	private Request request;

	public ActiveSyncRequest(ActiveSyncQueryString query, Request request) {
		this.query = query;
		this.request = request;
	}
	
	public ActiveSyncQueryString getQuery() {
		return query;
	}
	
	public Request getRequest() {
		return request;
	}

}
