package com.views;

import java.io.File;

//import views.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the activity class responsible for creating and handling actions in
 * success.xml. This page is responsible for showing the stego image and offer
 * the user the choice to share.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */

public class Success extends Activity {
	String outputPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.success);

		Bundle bundle = this.getIntent().getExtras();
		outputPath = bundle.getString("outputPath");

		// TextView path = (TextView)findViewById(R.id.textView2);
		Button exitButton = (Button) findViewById(R.id.button1);
		Button shareButton = (Button) findViewById(R.id.button2);
		ImageView imageView = (ImageView) findViewById(R.id.imgView);
		imageView.setImageBitmap(BitmapFactory.decodeFile(outputPath));
	
		Toast.makeText(getApplicationContext(), "Saved in: "+outputPath,
				Toast.LENGTH_LONG).show();
		
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Send via email
				Intent intent = new Intent(
						android.content.Intent.ACTION_SEND);

				File file = new File(outputPath);
				intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Infomage");
				intent.setType("application/octet-stream");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(file));
				startActivity(Intent.createChooser(intent,
						"Mail Chooser"));
			}
		});

		exitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Success.this.finish();
				// Intent intent = new Intent(Intent.ACTION_MAIN);
				Intent intent = new Intent();
				intent.setClass(Success.this, MainActivity.class);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.putExtra("EXIT", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});
		
		// Currently no use - used in the last version
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to send it via email now?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								Intent intent = new Intent(
										android.content.Intent.ACTION_SEND);

								File file = new File(outputPath);
								intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"a.bmp");
								intent.setType("application/octet-stream");
								intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
								startActivity(Intent.createChooser(intent,"Share via:"));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.setTitle("Success");
//		alert.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.runFinalizersOnExit(true);
		System.exit(0);
	}
}
