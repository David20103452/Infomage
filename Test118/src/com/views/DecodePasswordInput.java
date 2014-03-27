package com.views;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;

import org.apache.http.util.EncodingUtils;

import com.F5.test;
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

/**
 * This is the activity class responsible for creating and handling actions in
 * decode_password.xml. This class is the client class which dynamically
 * generate the extracting algorithm using Factory Method Pattern. It also
 * utilized reflection techniques in Java to creating the Factory class 
 * dynamically, which ensures that no modification will be made in this class
 * when a new algorithm is added to the system.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */

public class DecodePasswordInput extends Activity {

	public static String decodePassword = "";
	private String picturePath = ImageViewer.picturePath;
	private static final String TAG = "text";
	EditText passwordEditText;
	ProgressDialog mpDialog;
	test test1 = new test();
	AlgorithmFactory r; // Class generated with reflection
	String message = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.decode_password);

		// Creating Factory as a client with reflection
		String factoryName = MainActivity.ALGORITHM;
		try {
			Class cls = Class.forName("com.algorithms." + factoryName);
			Constructor cons = cls.getConstructor(null);
			r = (AlgorithmFactory) cons.newInstance();
		} catch (Throwable e) {
		}

		Toast.makeText(getApplicationContext(),"Opened file:" + ImageViewer.picturePath, Toast.LENGTH_SHORT).show();

		TextView textView = (TextView) findViewById(R.id.textView2);
		textView.setText("To extract messages, you are supposed to enter the password, which is the same as the embedding password. The algorithm currently used to extract is "
				+ MainActivity.ALGORITHM + ".");
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

					mpDialog = new ProgressDialog(DecodePasswordInput.this);
					mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					mpDialog.setMessage("Please wait. Extracting...");
					mpDialog.setCanceledOnTouchOutside(false);
					mpDialog.show();
					new Thread(new Runnable() {
						@Override
						public void run() {

							String imagePath = ImageViewer.picturePath;
							String password = DecodePasswordInput.decodePassword;
							Log.i("test", imagePath + "+" + password);

							// Extract image with the algorithm
							// generated dynamically
							Looper.prepare();
							message = r.generateAlgorithm().extract(imagePath,
									password);

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
		public void handleMessage(Message msg) {
			// Commented out because of Android 4.4 regulations
			// sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
			// Uri.parse("file://"
			// + Environment.getExternalStorageDirectory()
			// + "/encoded/")));
			mpDialog.dismiss();

			switch (msg.what) {
			case 0:
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
