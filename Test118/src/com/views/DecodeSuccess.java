package com.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//import views.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DecodeSuccess extends Activity {

	public static String content;
	String outputPath = MainActivity.CONFIG_PATH + "content.txt";
	private static final String TAG = "text";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decode_result);

		Bundle bundle = this.getIntent().getExtras();
		content = bundle.getString("content");

		EditText contentEditText = (EditText) findViewById(R.id.editText1);
		// TextView contentEditText = (TextView)findViewById(R.id.textView2);
		Button exitButton = (Button) findViewById(R.id.button1);
		Button saveButton = (Button) findViewById(R.id.button2);

		//Log.i(TAG, content);
		contentEditText.setText(content);
		// contentEditText.setEnabled(false);
		exitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Success.this.finish();
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				android.os.Process.killProcess(0);
				Intent intent = new Intent();
				intent.setClass(DecodeSuccess.this, MainActivity.class);
				//intent.addCategory(Intent.CATEGORY_HOME);
				intent.putExtra("EXIT", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				try {
					
					// Get output image name 
					String image = ImageViewer.picturePath;	
					String[] path = image.split("/");
					String[] separate = path[path.length-1].split("_");
					String outputImageName = "extracted_content_from_"+separate[separate.length-1];	
					outputImageName = outputImageName.replace(".jpg", ".txt");
					outputPath = MainActivity.OUTPUT_PATH + outputImageName;
					
					writeSDFile(outputPath, content);
					showSaveSuccessDialog();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	public void writeSDFile(String fileName, String write_str)
			throws IOException {

		File file = new File(fileName);
		if (!file.exists()) {
			try {
				// 在指定的文件夹中创建文件
				file.createNewFile();
			} catch (Exception e) {
			}
		}
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = write_str.getBytes();
		fos.write(bytes);
		fos.close();
	}

	public void showSaveSuccessDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(""+outputPath)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.setTitle("Saved in:");
		alert.show();
	}
}
