package edu.sysu.netlab.livestreaming.responseApi;

public enum ResponseCode {
	Success(200),
	GeneralError(500),
	PostDataError(501),
	PublisherError(502),
	RtmpServerError(505);
	
	private int code = -1;
	
	ResponseCode(int c) {code = c;}

	public int value() {return code;}
}