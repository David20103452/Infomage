package com.views;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;

import org.apache.http.util.EncodingUtils;

import com.Test118.Test118Act;
import com.algorithms.AlgorithmFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DecodePasswordInput extends Activity {

	public static String decodePassword = "";
	private String picturePath = ImageViewer.picturePath;
	private static final String TAG = "text";
	EditText passwordEditText;
	ProgressDialog mpDialog;
	test test1 = new test();
	AlgorithmFactory r;
	String message = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.decode_password);
		
		// for creating Factory as a client
		String factoryName = "LSBFactory";
		try{
			Class cls = Class.forName("com.algorithms."+factoryName);
			Constructor cons = cls.getConstructor(null);
			r = (AlgorithmFactory)cons.newInstance();			
			
		}
		catch(Throwable e){
			
		}

		Toast.makeText(getApplicationContext(), "Opened file:"+ImageViewer.picturePath,
				Toast.LENGTH_SHORT).show();

		passwordEditText = (EditText) findViewById(R.id.password);
		Button backButton = (Button) findViewById(R.id.button1);
		Button nextButton = (Button) findViewById(R.id.button2);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(DecodePasswordInput.this, MainActivity.class);
				startActivity(intent);
				DecodePasswordInput.this.finish();

			}
		});

		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				decodePassword = passwordEditText.getText().toString();
				if (!decodePassword.equals("")) {
					// try {
					// // Decoder decoder = new Decoder();
					// // String text = decoder.getText(picturePath);
					//
					// Intent i = new Intent();
					// i.setClass(DecodePasswordInput.this, Test118Act.class);
					// startActivity(i);
					//
					// // String text = "haha";
					// // Log.i(TAG, text);
					// // if (!text.equals("")) {
					// // Intent intent = new Intent();
					// // intent.setClass(DecodePasswordInput.this,
					// // DecodeSuccess.class);
					// // intent.putExtra("content", text);
					// // startActivity(intent);
					// // } else {
					// // showDecodeFailDialog();
					// // }
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					mpDialog = new ProgressDialog(DecodePasswordInput.this);
					mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���÷��ΪԲ�ν��
					mpDialog.setMessage("Please wait. Extracting...");
					mpDialog.setCanceledOnTouchOutside(false);
					mpDialog.show();
					new Thread(new Runnable() {
						@Override
						public void run() {
							// // encode
							// Intent intent = new Intent();
							// intent.setClass(DecodePasswordInput.this,
							// Test118Act.class);
							// // intent.putExtra("path", picturePath);
							// // intent.putExtra("password", decodePassword);
							// startActivity(intent);
							//
							// // encoder.write(content);
							// handler.obtainMessage(0).sendToTarget();

							String image = ImageViewer.picturePath;
							test1.setPath(image, "");
							Test118Act.coeffNumber = test1.getCoeffNumber();
							// Test118Act.coeffNumber = 1536;
							// Log.i(Test118Act.TAG, coeffNumber+"");
							int[] array = new int[Test118Act.coeffNumber];
							
							String imagePath = ImageViewer.picturePath;
							String password = DecodePasswordInput.decodePassword;
							Log.i("test", imagePath + "+" + password);
							// imagePath = "/sdcard/test.jpg";
							// password = "abc123";
							
							// for reflection test
							Looper.prepare();
							message = r.generateAlgorithm().extract(imagePath, password);
//							boolean extractedSuccess = true;
							
//							int[] coeff = decodeEmbededImage(array, imagePath,
//									Test118Act.coeffNumber);
//							boolean extractedSuccess = Extract.extract(coeff,
//									password);

							
							if (message != null) {
								handler.obtainMessage(0).sendToTarget();
							} else {
								handler.obtainMessage(1).sendToTarget();
							}
						}

					}).start();

				} else {
					Toast.makeText(getApplicationContext(),
							"Please enter the extracting password",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private static native int[] decodeEmbededImage(int[] coeff,
			String imagePath, int coeffNumber);

	static {
		System.loadLibrary("Test118");
	}

	public void showDecodeFailDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"The password is wrong or the image does not contain any hidden information")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.setTitle("Sorry");
		alert.show();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
//					Uri.parse("file://"
//							+ Environment.getExternalStorageDirectory()
//							+ "/encoded/")));
			mpDialog.dismiss();// �ر�ProgressDialog

			switch (msg.what) {
			case 0:
				// For F5, read from content.txt
//				String fileName = MainActivity.CONFIG_PATH + "content.txt";// �ļ�·��
//				String content = "";
//				try {
//					FileInputStream fin = new FileInputStream(fileName);
//					int length = fin.available();
//					byte[] buffer = new byte[length];
//					fin.read(buffer);
//					content = EncodingUtils.getString(buffer, "GBK");// //��Y.txt�ı�������ѡ����ʵı��룬�����������
//					fin.close();// �ر���Դ
//
//				} catch (Exception e) {
//				}
				
				String content = message;

				if (!content.equals("")) {

					Intent intent = new Intent();
					intent.setClass(DecodePasswordInput.this,
							DecodeSuccess.class);
					intent.putExtra("content", content);
					startActivity(intent);
				} else {
					showDecodeFailDialog();
				}
				break;
			case 1:
				showDecodeFailDialog();
				break;
			}
		}

	};
}
