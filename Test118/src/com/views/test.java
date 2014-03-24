package com.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import james.JpegEncoder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class test extends Activity{
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
	public int[] encode(int[] originalCoeff, String password){
		JpegEncoder encoder = new JpegEncoder(image,100,out,"");
		int[] coeff = encoder.Compress(in, password,originalCoeff);
		return coeff;

//		Extract.extract();
	}
	
	public int getCoeffNumber(){
		JpegEncoder encoder = new JpegEncoder(image,100,out,"");
		int number = encoder.getCoeffNumber();
		return number;
	}
	
	public int[] getParameters(){
		JpegEncoder encoder = new JpegEncoder(image,100,out,"");
		// the parameters in order: [0]width, [1]length, [2]DCT, [3]capacity
		int[] para = encoder.getParameters();
		return para;
	}
	
	public void setPath(String inputPath, String outputPath) {
		try{
		infilename = new File(MainActivity.CONFIG_PATH + "test.txt");
		outfilename = new File(MainActivity.CONFIG_PATH + "a.jpg");
		
		try{
		Bitmap image1 = BitmapFactory.decodeFile(inputPath);
		FileOutputStream temp = new FileOutputStream(MainActivity.CONFIG_PATH + "temp.jpg");
		image1.compress(Bitmap.CompressFormat.JPEG, 100, temp);
		}catch(Exception e){}
		image = BitmapFactory.decodeFile(MainActivity.CONFIG_PATH + "temp.jpg");
		
//		FileOutputStream temp = new FileOutputStream("/storage/sdcard0/encoded/temp.jpg");
//		image.compress(Bitmap.CompressFormat.JPEG, 100, temp);
		
		
		in = new FileInputStream(infilename);
		out = new FileOutputStream(outfilename);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
}
