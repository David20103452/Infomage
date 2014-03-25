package com.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.graphics.Bitmap;

import com.F5.Extract;
import com.F5.test;
import com.Test118.Test118Act;
import com.views.ImageViewer;
import com.views.MainActivity;

/**
 * This class is the implementation of F5 algorithm.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * @see com.algorithm.Algorithm
 * 
 */
public class F5Algorithm implements Algorithm {
	test test1 = new test();
	test test = new test();

	@Override
	public boolean embed(String inputPath, String outputPath, String password,
			String message) {

		// save the content to text file, only for F5
		try {
			File file = new File(MainActivity.CONFIG_PATH + "test.txt");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
				}
			}
			FileOutputStream fos = new FileOutputStream(file, false);
			fos.write(message.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Configure parameters for f5
		String image = inputPath;
		test1.setPath(image, "");
		Test118Act.coeffNumber = test1.getCoeffNumber();
		// Test118Act.coeffNumber = 1536;
		// Log.i(Test118Act.TAG, coeffNumber+"");
		int[] array = new int[Test118Act.coeffNumber];
		String imagePath = MainActivity.CONFIG_PATH + "temp.jpg";

		// Decode original image to DCT coefficients
		int[] coeff = RenderBitmap(Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565), array,	imagePath, Test118Act.coeffNumber);
		
		// Utilize f5 algorithm to embed messages into original DCT coefficients
		int[] coeffAfterEmbed = F5Embed(coeff, password, imagePath);
		
		// Covert DCT coefficients back to images
		createImage(coeffAfterEmbed, imagePath, outputPath,	Test118Act.coeffNumber);
		return true;
	}

	/**
	 * F5 Algorithm to embed the message to original DCT coefficients
	 * 
	 * @param origicalCoeff original DCT coefficients
	 * @param password password needed for embedding
	 * @param imagePath original image path
	 * @return array of DCT coefficients
	 */
	public int[] F5Embed(int[] origicalCoeff, String password, String imagePath) {

		test.setPath(imagePath, "");
		int[] coeff = test.encode(origicalCoeff, password);
		return coeff;
	}

	/**
	 * Declaration for Native Method: RenderBitmap, used to decode original image to DCT coefficients.
	 * 
	 * @param bitmap idle parameter
	 * @param array empty array for holding coefficients
	 * @param imagePath path for original image
	 * @param coeffNumber number of coefficients of original image
	 * @return coefficients array
	 */
	private static native int[] RenderBitmap(Bitmap bitmap, int[] array, String imagePath, int coeffNumber);
	
	/**
	 * Declaration for Native Method: createImage, used to covert DCT coefficients back to images
	 * 
	 * @param coeff DCT coefficient after embedding
	 * @param imagePath path for original image
	 * @param outputPath path for stego image
	 * @param coeffNumber coefficient number
	 */
	private static native void createImage(int[] coeff, String imagePath, String outputPath, int coeffNumber);
	
	/**
	 * Declaration for Native Method: decodeEmbededImage, used to decode stego image
	 * 
	 * @param coeff empty array
	 * @param imagePath path for stego image
	 * @param coeffNumber number of coefficients
	 * @return coefficient array
	 */
	private static native int[] decodeEmbededImage(int[] coeff,	String imagePath, int coeffNumber);

	// Load Test118.so which is compiled from native C++ codes
	static {
		System.loadLibrary("Test118");
	}

	@Override
	public String extract(String inputPath, String password) {

		// Configure for F5 extracting
		String image = ImageViewer.picturePath;
		test1.setPath(image, "");
		Test118Act.coeffNumber = test1.getCoeffNumber();
		// Test118Act.coeffNumber = 1536;
		// Log.i(Test118Act.TAG, coeffNumber+"");
		int[] array = new int[Test118Act.coeffNumber];

		String imagePath = inputPath;
		// String imagePath =MainActivity.CONFIG_PATH + "temp.jpg";
		try {
			
			// Decode the stego image to DCT coefficients
			int[] coeff = decodeEmbededImage(array, imagePath, Test118Act.coeffNumber);
			
			// Utilize F5 algorithm to extract message from DCT coefficients
			boolean extractedSuccess = Extract.extract(coeff, password);

			if (extractedSuccess) {
				String fileName = MainActivity.CONFIG_PATH + "content.txt";
				String content = "";
				try {
					FileInputStream fin = new FileInputStream(fileName);
					int length = fin.available();
					byte[] buffer = new byte[length];
					fin.read(buffer);
					content = EncodingUtils.getString(buffer, "GBK");
					fin.close();

				} catch (Exception e) {
				}

				return content;

			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}

}
