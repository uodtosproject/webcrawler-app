package br.com.betohayasida.webcrawler.Modules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logger
 * @author rkhayasidajunior
 *
 */
public class MyLogger {
	private String method = "";
	private boolean onFile = true;
	private String filename = "output.txt";
	private FileWriter fw = null;
	private BufferedWriter bw = null;
	
	public MyLogger(String filename){
		this.filename = filename;
		System.setOut(System.out);
		if(onFile){
			try {
				this.setOnFile(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setOnFile(boolean onFile) throws IOException{
		this.onFile = onFile;
		if(onFile){
			File file = new File(filename);
			 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
		}
	}
	
	public void setMethod(String method){
		this.method = method;
	}
	
	public void log(String message) throws IOException{
		if(onFile && bw!=null){
			bw.write(this.method + ": " + message + "\n");
		} 
		System.out.println(this.method + ": " + message);
	}
	
	public void log(String message, String method) throws IOException{
		if(onFile && bw!=null){
			bw.write(method + ": " + message + "\n");
		}
		System.out.println(method + ": " + message);
	}
	
	public void close() throws IOException{
		if(onFile && bw!=null){
			bw.close();
		}
	}
}
