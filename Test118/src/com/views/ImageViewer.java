package com.views;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageViewer extends Activity {

	private static final String TAG = "viewer";
	public static String picturePath = "";
	
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.more_info, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		showImageInfo();
		return true;
	}
	
	public void showImageInfo(){
/*		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[]{"Image Path:"+picturePath,"Image Name",""}, null).setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.setTitle("Image Information");
		alert.show();*/

		// Get image parameters
		String [] parameters = getImageParameters();
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.image_info, (ViewGroup)findViewById(R.id.dialog));		
		TextView titleTextView = (TextView)layout.findViewById(R.id.textView1);
		TextView widthTextView = (TextView)layout.findViewById(R.id.textView2);
		TextView lengthTextView = (TextView)layout.findViewById(R.id.textView3);
		TextView DCTTextView = (TextView)layout.findViewById(R.id.textView4);
		TextView capacityTextView = (TextView)layout.findViewById(R.id.textView5);
		TextView pathTextView = (TextView)layout.findViewById(R.id.textView6);
		titleTextView.setText("Title: "+parameters[0]);
		widthTextView.setText("Width: "+parameters[1]);
		lengthTextView.setText("Length: "+parameters[2]);
		DCTTextView.setText("DCT coefficient: "+parameters[3]);
		capacityTextView.setText("Expected massage capacity: "+parameters[4]);
		pathTextView.setText("Path: "+parameters[5]);
		new AlertDialog.Builder(this).setTitle("Image Information").setIcon(android.R.drawable.ic_dialog_info).setView(layout).setPositiveButton("OK", null).show();
	}
	
	public String[] getImageParameters(){
		String[] parameters = new String[6];
		// title
		String[] path = picturePath.split("/");
		parameters[0] = path[path.length-1];
		// width & length
		// DCT
		// capacity
		test test1 = new test();
		test1.setPath(picturePath, "");			
		int[] para = test1.getParameters();
		// the para in order: [0]width, [1]length, [2]DCT, [3]capacity
		parameters[1] = para[0]+""; // Width
		parameters[2] = para[1]+""; // Length
		parameters[3] = para[2]+""; // DCT
		parameters[4] = para[3]+" bits"; // capacity
		
		// path
		parameters[5] = picturePath;
		return parameters;
		
	}

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
