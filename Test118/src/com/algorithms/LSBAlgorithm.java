package com.algorithms;

import com.views.Decoder;
import com.views.Encoder;

public class LSBAlgorithm implements Algorithm{

	@Override
	public boolean embed(String inputPath, String outputPath, String password,
			String message) {
		// TODO Auto-generated method stub
		Encoder encoder = new Encoder();
		encoder.setname(inputPath, outputPath);
		encoder.write(message);
		return true;
	}

	@Override
	public String extract(String inputPath, String password) {
		// TODO Auto-generated method stub
		String text = "";
		Decoder decoder = new Decoder();
		try{
			text = decoder.getText(inputPath);
		}catch(Exception e){}
		return text;
	}

	
}
