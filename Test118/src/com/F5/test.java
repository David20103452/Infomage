package com.F5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.views.MainActivity;

import james.JpegEncoder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This is the configuration class for conducting f5 embedding algorithms.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */
public class test extends Activity {
	File infilename, outfilename; // = new File("1.bmp");
	FileInputStream in;
	FileOutputStream out;
	Bitmap image;

	public test() {
		infilename = outfilename = null;
		in = null;
		out = null;
		image = null;
	}

	/**
	 * Invoke JpegEncoder to embed message to DCT coefficients
	 * 
	 * @param originalCoeff
	 *            coefficients of original image
	 * @param password
	 *            password needed for embedding
	 * @return coefficient array
	 */
	public int[] encode(int[] originalCoeff, String password) {
		JpegEncoder encoder = new JpegEncoder(image, 100, out, "");
		int[] coeff = encoder.Compress(in, password, originalCoeff);
		return coeff;
	}

	/**
	 * Get coefficient numbers of the image
	 * 
	 * @return coefficient number
	 */
	public int getCoeffNumber() {
		JpegEncoder encoder = new JpegEncoder(image, 100, out, "");
		int number = encoder.getCoeffNumber();
		return number;
	}

	/**
	 * Get other image parameters including width, length, DCT number and
	 * message capacity
	 * 
	 * @return parameter array
	 */
	public int[] getParameters() {
		JpegEncoder encoder = new JpegEncoder(image, 100, out, "");
		// the parameters in order: [0]width, [1]length, [2]DCT, [3]capacity
		int[] para = encoder.getParameters();
		return para;
	}

	/**
	 * Configure input and output paths for f5 embedding algorithm
	 * 
	 * @param inputPath
	 *            path of original image
	 * @param outputPath
	 *            path of stego image
	 */
	public void setPath(String inputPath, String outputPath) {
		try {
			infilename = new File(MainActivity.CONFIG_PATH + "test.txt");
			outfilename = new File(MainActivity.CONFIG_PATH + "a.jpg");

			try {
				Bitmap image1 = BitmapFactory.decodeFile(inputPath);
				FileOutputStream temp = new FileOutputStream(
						MainActivity.CONFIG_PATH + "temp.jpg");
				image1.compress(Bitmap.CompressFormat.JPEG, 100, temp);
			} catch (Exception e) {
			}
			image = BitmapFactory.decodeFile(MainActivity.CONFIG_PATH
					+ "temp.jpg");

			// FileOutputStream temp = new
			// FileOutputStream("/storage/sdcard0/encoded/temp.jpg");
			// image.compress(Bitmap.CompressFormat.JPEG, 100, temp);

			in = new FileInputStream(infilename);
			out = new FileOutputStream(outfilename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
