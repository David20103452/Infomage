package com.algorithms;

import com.LSB.Decoder;
import com.LSB.Encoder;

/**
 * This class is the implementation of LSB algorithm.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * @see com.algorithm.Algorithm
 * 
 */
public class LSBAlgorithm implements Algorithm {

	@Override
	public boolean embed(String inputPath, String outputPath, String password,
			String message) {

		Encoder encoder = new Encoder();
		// Set up the encoder
		encoder.setname(inputPath, outputPath);
		// Embed the message to image
		encoder.write(message);
		return true;
	}

	@Override
	public String extract(String inputPath, String password) {

		String text = "";
		Decoder decoder = new Decoder();
		try {
			// Extract message from stego image
			text = decoder.getText(inputPath);
		} catch (Exception e) {
		}
		return text;
	}

}
