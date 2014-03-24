package com.LSB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.views.BMPTool;
import com.views.MainActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;

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

	public void setname(String a, String b) // ()//= new
											// FileInputStream(filename);
	{

	
		if (a.endsWith(end)) {
			infilename = new File(a);
		} else {	// convert .jpg to .bmp
			try{
//			FileOutputStream temp = new FileOutputStream("/storage/sdcard0/encoded/a.jpg");
			Bitmap tempBmp = BitmapFactory.decodeFile(a);
//			tempBmp = ThumbnailUtils.extractThumbnail(tempBmp, tempBmp.getWidth()/100, tempBmp.getHeight()/100,
//		            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//			tempBmp.compress(Bitmap.CompressFormat.JPEG,10,temp);
//			tempBmp = BitmapFactory.decodeFile("/storage/sdcard0/encoded/a.jpg");
			tool.saveBmp(tempBmp);
			//infilename = new File("/storage/sdcard/encoded/" + "temp.bmp");
			infilename = new File(MainActivity.OUTPUT_PATH+"temp.bmp");

			}catch(Exception e){
				e.printStackTrace();
			}
		}
		outfilename = new File(b);
	}

	public void write(String words) {
		try {
			// File afile = new File("a.bmp");
			// if (afile.exists() == false) {
			// afile.createNewFile();
			// }
			// File file = new File("/storage/sdcard/encoded/a.bmp");

			//String filenameTemp = "/storage/sdcard/encoded/" + "/a" + ".bmp";
			String filenameTemp = MainActivity.OUTPUT_PATH+"a.bmp";
			File dir = new File(filenameTemp);
			if (!dir.exists()) {
				try {
					// ��ָ�����ļ����д����ļ�
					dir.createNewFile();
				} catch (Exception e) {
				}
			}

			//FileInputStream intemp = new FileInputStream("/storage/sdcard/encoded/a.bmp");// "/storage/sdcard/encoded/a.bmp");
			FileInputStream intemp = new FileInputStream(MainActivity.OUTPUT_PATH+"a.bmp");
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
							//temp = temp - intemp.read();
							temp = in.read();
							k++;
						}
					}
				} else {
					out.write(temp);
					k++;
					//temp = intemp.read();
					temp = in.read();
				}

			}
			System.out.println("end");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
