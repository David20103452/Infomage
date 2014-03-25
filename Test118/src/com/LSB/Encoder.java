package com.LSB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.views.MainActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;

/**
 * This is the LSB embedding class for conducting LSB algorithm.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */
public class Encoder extends Activity {

	File infilename, outfilename; // = new File("1.bmp");
	FileInputStream in;
	FileOutputStream out;
	BMPTool tool = new BMPTool();
	String end = ".bmp";

	public Encoder() {
		infilename = outfilename = null;
		in = null;
		out = null;
	}

	/**
	 * Set up the encoder with input path and output path
	 * 
	 * @param a input path
	 * @param b output path
	 */
	public void setname(String a, String b) {
		if (a.endsWith(end)) {
			infilename = new File(a);
		} else {
			// Convert .jpg to .bmp
			try {
				Bitmap tempBmp = BitmapFactory.decodeFile(a);
				tool.saveBmp(tempBmp);
				infilename = new File(MainActivity.OUTPUT_PATH + "temp.bmp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		outfilename = new File(b);
	}

	/**
	 * Write the message to image with LSB algorithm
	 * 
	 * @param words message to embed
	 */
	public void write(String words) {
		try {

			String filenameTemp = MainActivity.OUTPUT_PATH + "a.bmp";
			File dir = new File(filenameTemp);
			if (!dir.exists()) {
				try {
					dir.createNewFile();
				} catch (Exception e) {
				}
			}

			FileInputStream intemp = new FileInputStream(
					MainActivity.OUTPUT_PATH + "a.bmp");
			// FileInputStream fin = new FileInputStream(fileName);
			words += "a$";
			char[] c = words.toCharArray();
			int i, j, k = 0;
			int hehe;
			int asc = 0;
			int temp = 0;
			int flag = 0;
			in = new FileInputStream(infilename);
			out = new FileOutputStream(outfilename);
			temp = in.read();
			while (temp != -1) {
				if (flag == 0 && k >= 54) {

					for (j = 0; j < words.length(); j++) {
						if (c[j] == '$') {
							flag = 1;
						}
						for (i = 0; i < 8; i++) {
							hehe = (c[j] >> (7 - i) & 0x01);
							temp = hehe + (temp & 0xfe);
							out.write(temp);
							// temp = temp - intemp.read();
							temp = in.read();
							k++;
						}
					}
				} else {
					out.write(temp);
					k++;
					// temp = intemp.read();
					temp = in.read();
				}

			}
			System.out.println("end");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
