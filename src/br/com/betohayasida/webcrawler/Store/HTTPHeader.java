package br.com.betohayasida.webcrawler.Store;

/**
 * Class for storing HTTP header responses
 * @author rkhayasidajunior
 *
 */
public class HTTPHeader {

	private int code;
	private String location;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


}
