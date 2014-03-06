package com.views;

import java.io.File;
import java.io.FileOutputStream;

import com.Test118.Test118Act;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InputViaText extends Activity {

	public static String encodePassword = "";
	private EditText inputText;
	private EditText inputPassword;
	private static final String TAG = "text";
	private String outputPath;
	ProgressDialog mpDialog;
	String content = "";
	Encoder encoder = new Encoder();
	test test1 = new test();
	test test = new test();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.step2_text);
		
		Toast.makeText(getApplicationContext(),
				"Opened file:"+ImageViewer.picturePath,
				Toast.LENGTH_SHORT).show();

		inputText = (EditText) findViewById(R.id.editText1);
		inputPassword = (EditText) findViewById(R.id.editText2);

		Button backButton = (Button) findViewById(R.id.button1);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent();
				intent.setClass(InputViaText.this, MainActivity.class);
				startActivity(intent);
				InputViaText.this.finish();

			}
		});
		Button nextButton = (Button) findViewById(R.id.button2);
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				encodePassword = inputPassword.getText().toString();
				content = inputText.getText().toString();

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
					fos.write(content.getBytes());
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Log.i(TAG, text+" "+password);
				// // Intent intent = new Intent();
				// // intent.setClass(InputViaText.this, MainActivity.class);
				// // startActivity(intent);
				// Encoder encoder = new Encoder();
				// String inputPath = ImageViewer.picturePath;
				// String outputPath = "/storage/sdcard0/encoded/"+"a.bmp";
				// encoder.setname(inputPath, outputPath);
				 Log.i(TAG, "test");
				// try {
				// encoder.write(text);
				// //JOptionPane.showMessageDialog(getContentPane(),
				// "�ɹ�����д�ļ�a.bmp����������");
				// } catch (Throwable e) {
				// //JOptionPane.showMessageDialog(getContentPane(), "��дʧ�ܣ�");
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				if (!content.equals("") && !encodePassword.equals("")) {
					mpDialog = new ProgressDialog(InputViaText.this);
					mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���÷��ΪԲ�ν��
					mpDialog.setMessage("Please wait. Embeding...");
					mpDialog.setCanceledOnTouchOutside(false);
					mpDialog.show();
					new Thread(new Runnable() {
						@Override
						public void run() {
							// encode		
// For back up							
//							String inputPath = ImageViewer.picturePath;
//							// String outputPath =
//							// "/storage/sdcard0/encoded/"+"a.bmp";
//							// String outputPath =
//							// Environment.getExternalStorageDirectory()+"/encoded/"+"a.bmp";
//							String outputPath = MainActivity.OUTPUT_PATH
//									+ "a.bmp";
//							// encoder.setname(inputPath, outputPath);
//							test.setPath(inputPath, outputPath);
//							Log.i(TAG, inputPath + "" + outputPath);
//							// test.encode();
//							Intent intent = new Intent();
//							intent.setClass(InputViaText.this, Test118Act.class);
//							startActivity(intent);
//							// encoder.write(content);
//							handler.obtainMessage(0).sendToTarget();
							
							String image = ImageViewer.picturePath;		
							test1.setPath(image, "");
							Test118Act.coeffNumber = test1.getCoeffNumber();
//							Test118Act.coeffNumber = 1536;
							//Log.i(Test118Act.TAG, coeffNumber+"");
							int[] array = new int[Test118Act.coeffNumber];
							String imagePath = MainActivity.CONFIG_PATH + "temp.jpg";
							
							// Get output image name 
							String[] path = image.split("/");
							String outputImageName = "embedded_"+path[path.length-1];
							
							outputPath = MainActivity.OUTPUT_PATH+outputImageName;							
							String password = InputViaText.encodePassword;
							//password = "abc123";
							//outputPath = "/sdcard/test.jpg";							
							Log.i("encode", "test");
							int[] coeff = RenderBitmap(Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565), array,imagePath,Test118Act.coeffNumber);
							int[] coeffAfterEmbed = F5Embed(coeff, password,imagePath);
							createImage(coeffAfterEmbed, imagePath, outputPath,Test118Act.coeffNumber);
							Log.i("encode", outputPath+"+"+password);
							handler.obtainMessage(0).sendToTarget();
							
						}
						public int[] F5Embed(int[] origicalCoeff, String password,String imagePath){
							
							test.setPath(imagePath, "");
							int[] coeff = test.encode(origicalCoeff, password);
							return coeff;
						}

					}).start();
				} else if (content.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter embedding text",
							Toast.LENGTH_SHORT).show();
				} else if (encodePassword.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter embedding password",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private static native int[] RenderBitmap(Bitmap bitmap, int[] array, String imagePath, int coeffNumber);
	private static native void createImage(int[] coeff, String imagePath,String outputPath, int coeffNumber);
	private static native int[] decodeEmbededImage(int[] coeff, String imagePath, int coeffNumber);
	static {
		System.loadLibrary("Test118");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
//					Uri.parse("file://"
//							+ Environment.getExternalStorageDirectory()
//							+ "/encoded/")));
			mpDialog.dismiss();// �ر�ProgressDialog
			Intent intent = new Intent();
			intent.setClass(InputViaText.this, Success.class);
			intent.putExtra("outputPath", outputPath);
			startActivity(intent);
		}
	};
}
