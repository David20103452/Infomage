package com.algorithms;

public interface Algorithm {
	
	public boolean embed(String inputPath, String outputPath, String password, String message);
	public String extract(String inputPath, String password);

}
