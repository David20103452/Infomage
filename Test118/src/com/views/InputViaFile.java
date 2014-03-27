package com.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;

import org.apache.http.util.EncodingUtils;

import com.F5.test;
import com.LSB.Encoder;
import com.Test118.Test118Act;
import com.algorithms.AlgorithmFactory;

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
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the activity class responsible for creating and handling actions in
 * step2_file.xml. This is an input page for user to input message through file.
 * This class is the client class which dynamically generate the embedding
 * algorithm using Factory Method Pattern. It also utilized reflection
 * techniques in Java to creating the Factory class dynamically, which ensures
 * that no modification will be made in this class when a new algorithm is added
 * to the system.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */

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
	AlgorithmFactory r;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.step2_file);

		// Creating Factory as a client with reflection
		String factoryName = MainActivity.ALGORITHM;
		try {
			Class cls = Class.forName("com.algorithms." + factoryName);
			Constructor cons = cls.getConstructor(null);
			r = (AlgorithmFactory) cons.newInstance();

		} catch (Throwable e) {
		}

		Toast.makeText(getApplicationContext(),
				"Opened file:" + ImageViewer.picturePath, Toast.LENGTH_SHORT)
				.show();
		TextView textView = (TextView) findViewById(R.id.textView2);
		textView.setText("Now you are supposed to input the message and password for embedding. The algorithm currently used to embed is "
				+ MainActivity.ALGORITHM + ".");
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
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String filePath = inputFile.getText().toString();
				String password = inputPassword.getText().toString();
				encodedPassword = password;
				Log.i(TAG, filePath + " " + password);

				if (!filePath.equals("") && !password.equals("")) {
					try {

						FileInputStream fin = new FileInputStream(filePath);
						int length = fin.available();
						byte[] buffer = new byte[length];
						fin.read(buffer);
						content = EncodingUtils.getString(buffer, "GBK");
						fin.close();
						Log.i(TAG, content);

						mpDialog = new ProgressDialog(InputViaFile.this);
						mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						mpDialog.setMessage("Please wait. Embeding...");
						mpDialog.setCanceledOnTouchOutside(false);
						mpDialog.show();
						new Thread(new Runnable() {
							@Override
							public void run() {

								String image = ImageViewer.picturePath;

								// Get output image name
								String[] path = image.split("/");
								String outputImageName = "embedded_"
										+ path[path.length - 1];

								outputPath = MainActivity.OUTPUT_PATH
										+ outputImageName;
								String password = InputViaFile.encodedPassword;

								// Embed the message with the algorithm
								// generated dynamically
								Looper.prepare();
								boolean embeddedsuccess = r.generateAlgorithm()
										.embed(ImageViewer.picturePath,
												outputPath, password, content);
								if (embeddedsuccess) {
									handler.obtainMessage(0).sendToTarget();
								} else {
									handler.obtainMessage(1).sendToTarget();
								}
							}

						}).start();

					} catch (java.io.FileNotFoundException e) {
						Log.d(TAG, "The File doesn't not exist.");
					} catch (Exception e) {
						Log.d(TAG, e.getMessage());
					}
				} else if (filePath.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please choose a file", Toast.LENGTH_SHORT).show();
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

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Commented out because of Android 4.4 regulations
			// sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
			// Uri.parse("file://"+
			// Environment.getExternalStorageDirectory()+"/encoded/")));
			mpDialog.dismiss();
			switch (msg.what) {
			case 0:
				Intent intent = new Intent();
				intent.setClass(InputViaFile.this, Success.class);
				intent.putExtra("outputPath", outputPath);
				startActivity(intent);
				break;
			case 1:
				showEmbedFailDialog();
			}

		}
	};

	public void showEmbedFailDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Embedding failed!").setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.setTitle("Sorry");
		alert.show();
	}

}
