package br.com.betohayasida.webcrawler.Store;

import java.util.List;


public class ResponseData {
    private List<Result> results;
    public List<Result> getResults() { return results; }
    public void setResults(List<Result> results) { this.results = results; }
    public String toString() { return "Results[" + results + "]"; }
    
}
