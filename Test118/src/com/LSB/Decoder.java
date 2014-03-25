package com.LSB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This is the LSB extracting class for conducting LSB algorithm.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */

public class Decoder {

	File infilename;

	/**
	 * Extract the message from stego image
	 * 
	 * @param a
	 *            path for stego image
	 * @return extracted message
	 * @throws IOException
	 */
	public String getText(String a) throws IOException {
		String text = "";
		int temp = 0;
		int i;
		int key = 0;
		char tempchar;
		File filea = new File(a);
		FileInputStream in = new FileInputStream(filea);
		in.skip(54);
		do {
			key = 0;
			for (i = 0; i < 8; i++) {
				key = key * 2;
				temp = in.read();
				temp = temp % 2;
				key += temp;

			}
			tempchar = (char) key;
			text = text + tempchar;
		} while (key != '$');
		if (text.contains("a$"))
			return text.substring(0, text.length() - 2);
		else
			return null;
	}

}
