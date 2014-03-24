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

public class F5Algorithm implements Algorithm{
	test test1 = new test();
	test test = new test();
	
	@Override
	public boolean embed(String inputPath, String outputPath, String password,
			String message) {
		// TODO Auto-generated method stub
		
		// save the content to text file, only for F5
		try {
			File file = new File(MainActivity.CONFIG_PATH + "test.txt");
			if (!file.exists()) {
				try {
					// ��ָ�����ļ����д����ļ�
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
		

		String image = inputPath;		
		test1.setPath(image, "");
		Test118Act.coeffNumber = test1.getCoeffNumber();
//		Test118Act.coeffNumber = 1536;
		//Log.i(Test118Act.TAG, coeffNumber+"");
		int[] array = new int[Test118Act.coeffNumber];
		String imagePath = MainActivity.CONFIG_PATH + "temp.jpg";
		
		int[] coeff = RenderBitmap(Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565), array,imagePath,Test118Act.coeffNumber);
		int[] coeffAfterEmbed = F5Embed(coeff, password,imagePath);
		createImage(coeffAfterEmbed, imagePath, outputPath,Test118Act.coeffNumber);
		return true;
	}
	
	public int[] F5Embed(int[] origicalCoeff, String password,String imagePath){
		
		test.setPath(imagePath, "");
		int[] coeff = test.encode(origicalCoeff, password);
		return coeff;
	}
	
	private static native int[] RenderBitmap(Bitmap bitmap, int[] array, String imagePath, int coeffNumber);
	private static native void createImage(int[] coeff, String imagePath,String outputPath, int coeffNumber);
	private static native int[] decodeEmbededImage(int[] coeff, String imagePath, int coeffNumber);
	static {
		System.loadLibrary("Test118");
	}

	@Override
	public String extract(String inputPath, String password) {
		// TODO Auto-generated method stub
		
		String image = ImageViewer.picturePath;
		test1.setPath(image, "");
		Test118Act.coeffNumber = test1.getCoeffNumber();
		// Test118Act.coeffNumber = 1536;
		// Log.i(Test118Act.TAG, coeffNumber+"");
		int[] array = new int[Test118Act.coeffNumber];
		
		String imagePath = inputPath;
//		String imagePath =MainActivity.CONFIG_PATH + "temp.jpg";
		try{
		int[] coeff = decodeEmbededImage(array, imagePath,Test118Act.coeffNumber);		
		boolean extractedSuccess = Extract.extract(coeff,password);
		
		
		if(extractedSuccess){
			String fileName = MainActivity.CONFIG_PATH + "content.txt";// �ļ�·��
			String content = "";
			try {
				FileInputStream fin = new FileInputStream(fileName);
				int length = fin.available();
				byte[] buffer = new byte[length];
				fin.read(buffer);
				content = EncodingUtils.getString(buffer, "GBK");// //��Y.txt�ı�������ѡ����ʵı��룬�����������
				fin.close();// �ر���Դ

			} catch (Exception e) {	}				
			
			return content;			
			
		}else{
			return null;
		}	
		}catch(Exception e){ return null;}
		
	}

}
