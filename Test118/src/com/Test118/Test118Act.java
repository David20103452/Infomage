package com.Test118;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import com.views.DecodePasswordInput;
import com.views.Extract;
import com.views.ImageViewer;
import com.views.InputViaText;
import com.views.MainActivity;
import com.views.test;

import crypt.F5Random;
import crypt.Permutation;
import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Test118Act extends Activity {
	//public static final String TAG = "test118";
	public static int coeffNumber;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(new NativeView(this));
	}

	static {
		System.loadLibrary("Test118");
	}
}

class NativeView extends View {
	
	test test1 = new test();
	private Bitmap _mBitmap;
	// westfeld
	InputStream embeddedData = null;
	String password = null;

	private static native int[] RenderBitmap(Bitmap bitmap, int[] array, String imagePath, int coeffNumber);
	private static native void createImage(int[] coeff, String imagePath,String outputPath, int coeffNumber);
	private static native int[] decodeEmbededImage(int[] coeff, String imagePath, int coeffNumber);

	public NativeView(Context context) {
		super(context);

		final int W = 200;
		final int H = 200;

		_mBitmap = Bitmap.createBitmap(W, H, Bitmap.Config.RGB_565);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		String image = ImageViewer.picturePath;		
		test1.setPath(image, "");
		Test118Act.coeffNumber = test1.getCoeffNumber();
//		Test118Act.coeffNumber = 1536;
		//Log.i(Test118Act.TAG, coeffNumber+"");
		int[] array = new int[Test118Act.coeffNumber];
		// String fileName = "/sdcard/encoded/temp after embed.txt";// 文件路径
		// String res = "";
		// try {
		// FileInputStream fin = new FileInputStream(fileName);
		// // FileInputStream fin = openFileInput(fileName);
		// // 用这个就不行了，必须用FileInputStream
		// int length = fin.available();
		// byte[] buffer = new byte[length];
		// fin.read(buffer);
		// res = EncodingUtils.getString(buffer, "GBK");//
		// //依Y.txt的编码类型选择合适的编码，如果不调整会乱码
		// String[] result = res.split("\\s");
		// fin.close();// 关闭资源
		// for (int n = 0; n < result.length; n++) {
		// array[n] = Integer.parseInt(result[n]);
		// }
		// }catch(Exception e){}

		if(MainActivity.DECODE_FLAG == 0){
//			String imagePath = ImageViewer.picturePath;
			String imagePath = MainActivity.OUTPUT_PATH + "temp.jpg";
			String outputPath = MainActivity.OUTPUT_PATH+"test.jpg";
			String password = InputViaText.encodePassword;
			password = "abc123";
			outputPath = "/sdcard/test.jpg";
			Log.i("encode", imagePath+"+"+password);
			
			int[] coeff = RenderBitmap(_mBitmap, array,imagePath,Test118Act.coeffNumber);
			int[] coeffAfterEmbed = F5Embed(coeff, password,imagePath);
			createImage(coeffAfterEmbed, imagePath, outputPath,Test118Act.coeffNumber);
			Log.i("encode", outputPath+"+"+password);
		}
		else if (MainActivity.DECODE_FLAG == 1){
			
			//String imagePath = "/sdcard/test.jpg";
			String imagePath = ImageViewer.picturePath;
			String password = DecodePasswordInput.decodePassword;
			Log.i("test", imagePath+"+"+password);
			//imagePath = "/sdcard/test.jpg";
			password = "abc123";
			int[] coeff = decodeEmbededImage(array,imagePath,Test118Act.coeffNumber);			
			Extract.extract(coeff, password);
 
		}
		
		
		
//		try {
//			File file = new File("/sdcard/coeffAfterEmbed.txt");
//			if (!file.exists()) {
//				try {
//					// 在指定的文件夹中创建文件
//					file.createNewFile();
//				} catch (Exception e) {
//				}
//			}
//			FileOutputStream fos = new FileOutputStream(file, true);
//
//			for (int count = 0; count < (coeffAfterEmbed.length); count++) {
//				Integer integer = new Integer(coeffAfterEmbed[count]);
//				fos.write((integer.toString() + " ").getBytes());
//				// Log.i(TAG,values[i]+"" );
//			}
//			fos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
  		canvas.drawBitmap(_mBitmap, 0, 0, null);
	}
	
	public int[] F5Embed(int[] origicalCoeff, String password,String imagePath){
		test test = new test();
		test.setPath(imagePath, "");
		int[] coeff = test.encode(origicalCoeff, password);
		return coeff;
	}
		
	
}
