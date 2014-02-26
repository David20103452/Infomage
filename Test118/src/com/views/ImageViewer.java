package com.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageViewer extends Activity {

	private static final String TAG = "viewer";
	public static String picturePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.uploaded_image);
		Bundle bundle = this.getIntent().getExtras();
		picturePath = bundle.getString("picturePath");
		Log.i(TAG, picturePath);
		ImageView imageView = (ImageView) findViewById(R.id.imgView);
		imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		Button backButton = (Button) findViewById(R.id.button1);
		Button nextButton = (Button) findViewById(R.id.button2);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(ImageViewer.this, MainActivity.class);
				startActivity(intent);
				ImageViewer.this.finish();

			}
		});
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (MainActivity.DECODE_FLAG == 1) {
					//Decode Mode
					Intent intent = new Intent();
					intent.setClass(ImageViewer.this, DecodePasswordInput.class);
					startActivity(intent);
				} else {
					//Encode Mode
					Log.i(TAG, MainActivity.INPUT_METHOD + "");
					if (MainActivity.INPUT_METHOD == 0) {
						Intent intent = new Intent();
						intent.setClass(ImageViewer.this, InputViaText.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent();
						intent.setClass(ImageViewer.this, InputViaFile.class);
						startActivity(intent);
					}
				}

			}
		});

	}
}
