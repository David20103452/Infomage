package com.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.util.EncodingUtils;

import com.Test118.Test118Act;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputViaFile extends Activity {

	private FileDialog fileDialog;
	private File mPath;
	private EditText inputFile;
	private EditText inputPassword;
	private String outputPath;
	private static final String TAG = "file";
	public static String encodedPassword;
	ProgressDialog mpDialog;
	String content = "";
	Encoder encoder = new Encoder();
	test test1 = new test();
	test test = new test();

	// private static String INPUT_FILE_PATH;
	// private static String INPUT_PASSWORD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.step2_file);
		
		Toast.makeText(getApplicationContext(),
				ImageViewer.picturePath,
				Toast.LENGTH_SHORT).show();

		inputFile = (EditText) findViewById(R.id.editText1);
		inputPassword = (EditText) findViewById(R.id.editText2);
		Button backButton = (Button) findViewById(R.id.button1);
		EditText filePath = (EditText) findViewById(R.id.editText1);
		filePath.setEnabled(false);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(InputViaFile.this, MainActivity.class);
				startActivity(intent);
				InputViaFile.this.finish();

			}
		});
		Button nextButton = (Button) findViewById(R.id.button2);
		// this.showDialog(1000);
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String filePath = inputFile.getText().toString();
				String password = inputPassword.getText().toString();
				encodedPassword = password;
				Log.i(TAG, filePath + " " + password);

				if (!filePath.equals("") && !password.equals("")) {
					// read the file
//					File file = new File(filePath);
					try {
						
						/*
						 * The following is for LBS
						 */
//						InputStream instream = new FileInputStream(file);
//						if (instream != null) {
//							InputStreamReader inputreader = new InputStreamReader(
//									instream);
//							BufferedReader buffreader = new BufferedReader(
//									inputreader);
//							String line;
//							while ((line = buffreader.readLine()) != null) {
//								content += line + "\n";
//							}
//							instream.close();
//							Log.i(TAG, content);			
											
			  
			    			FileInputStream fin = new FileInputStream(filePath);
			    			int length = fin.available();
			    			byte[] buffer = new byte[length];
			    			fin.read(buffer);
			    			content = EncodingUtils.getString(buffer, "GBK");// //��Y.txt�ı�������ѡ����ʵı��룬�����������    			
			    			fin.close();// �ر���Դ
			    			Log.i(TAG, content);
			    			
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


							mpDialog = new ProgressDialog(InputViaFile.this);
							mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���÷��ΪԲ�ν��
							mpDialog.setMessage("Please wait. Embeding...");
							mpDialog.setCanceledOnTouchOutside(false);
							mpDialog.show();
							new Thread(new Runnable() {
								@Override
								public void run() {
//									// For LSB encoding
////									String inputPath = ImageViewer.picturePath;
////									String outputPath = MainActivity.OUTPUT_PATH + "a.bmp";
////									encoder.setname(inputPath, outputPath);
////									Log.i(TAG, inputPath + "" + outputPath);
////									encoder.write(content);
////									handler.obtainMessage(0).sendToTarget();									
//
//									
//									// For F5 embeding
//									Intent intent = new Intent();
//									intent.setClass(InputViaFile.this, Test118Act.class);
//									startActivity(intent);
//									// encoder.write(content);
//									handler.obtainMessage(0).sendToTarget();
									String image = ImageViewer.picturePath;		
									test1.setPath(image, "");
									Test118Act.coeffNumber = test1.getCoeffNumber();
//									Test118Act.coeffNumber = 1536;
									//Log.i(Test118Act.TAG, coeffNumber+"");
									int[] array = new int[Test118Act.coeffNumber];
									String imagePath = MainActivity.CONFIG_PATH + "temp.jpg";
									
									// Get output image name 
									String[] path = image.split("/");
									String outputImageName = "embedded_"+path[path.length-1];
									
									outputPath = MainActivity.OUTPUT_PATH+outputImageName;
									String password = InputViaFile.encodedPassword;
									//password = "abc123";
									//outputPath = "/sdcard/test.jpg";							
									
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

//						}

					} catch (java.io.FileNotFoundException e) {
						Log.d(TAG, "The File doesn't not exist.");
					} catch (Exception e) {
						Log.d(TAG, e.getMessage());
					}
				} else if (filePath.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please choose a file",
							Toast.LENGTH_SHORT).show();
				} else if (password.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter embedding password",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		Button chooseButton = (Button) findViewById(R.id.button3);
		chooseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mPath = new File(Environment.getExternalStorageDirectory()
						+ "//DIR//");
				fileDialog = new FileDialog(InputViaFile.this, mPath);
				fileDialog.setFileEndsWith(".txt");
				fileDialog
						.addFileListener(new FileDialog.FileSelectedListener() {
							public void fileSelected(File file) {
								Log.d(getClass().getName(), "selected file "
										+ file.toString());

								inputFile.setText(file.toString());
							}
						});
				fileDialog.showDialog();
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
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory()+"/encoded/")));
			mpDialog.dismiss();// �ر�ProgressDialog
			Intent intent = new Intent();
			intent.setClass(InputViaFile.this, Success.class);
			intent.putExtra("outputPath", outputPath);
			startActivity(intent);
		}
	};

}
