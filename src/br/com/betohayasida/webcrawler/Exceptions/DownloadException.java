package br.com.betohayasida.webcrawler.Exceptions;

/**
 * Extension of Exception class for HTTPModule
 */
public class DownloadException extends Exception {

	private static final long serialVersionUID = 1565749737340449799L;

	public DownloadException(String message){
		super(message);
	}
}
