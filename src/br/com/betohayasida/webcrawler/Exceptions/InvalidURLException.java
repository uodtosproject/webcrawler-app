package br.com.betohayasida.webcrawler.Exceptions;

/**
 * Extension of Exception class for URLModule
 */
public class InvalidURLException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidURLException(String message){
		super(message);
	}

}
