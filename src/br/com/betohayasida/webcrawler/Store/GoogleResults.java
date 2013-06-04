package br.com.betohayasida.webcrawler.Store;


public class GoogleResults {

    private ResponseData responseData;
    
    public ResponseData getResponseData() { 
    	return responseData; 
    }
    public void setResponseData(ResponseData responseData){ 
    	this.responseData = responseData; 
    }
    public String toString() { 
    	return "ResponseData[" + responseData + "]"; 
    }

}