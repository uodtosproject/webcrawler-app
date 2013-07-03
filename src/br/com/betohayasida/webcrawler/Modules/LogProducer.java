package br.com.betohayasida.webcrawler.Modules;

import java.io.IOException;

public class LogProducer {
	public MyLogger logger = null;
	protected boolean debug = false;
	
    public void setDebug(boolean debug){
    	this.debug = debug;
    }
    
    public void log(String message, String method){
    	if(debug){
    		try {
				this.logger.log(message, method);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

}
