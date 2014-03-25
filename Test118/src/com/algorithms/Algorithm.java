package com.algorithms;

/**
 * This is an interface for steganography algorithms. Two abstract methods(embed
 * and extract) should be implemented by classes which implement this interface.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */
public interface Algorithm {

	/**
	 * Extract message from a selected image
	 * 
	 * @param inputPath
	 *            path for selected image
	 * @param outputPath
	 *            output path for stego image(image after embedding)
	 * @param password
	 *            password needed for embedding
	 * @param message
	 *            message to embed in image
	 * @return true if embed message successfully else false
	 * 
	 */
	public boolean embed(String inputPath, String outputPath, String password,
			String message);

	/**
	 * Extract message from stego image
	 * 
	 * @param inputPath
	 *            path for stego image
	 * @param password
	 *            password needed for extracting
	 * @return messages extracted from stego image; Null if extracted failed
	 */
	public String extract(String inputPath, String password);

}
