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
import android.widget.TextView;

public class Success extends Activity {
	String outputPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        
		Bundle bundle = this.getIntent().getExtras();
		outputPath = bundle.getString("outputPath");
        
        TextView path = (TextView)findViewById(R.id.textView2);
        Button exitButton = (Button)findViewById(R.id.exit);
        //TODO
        //path.setText("/storage/sdcard0/encoded/"+"a.bmp");
        
        //For F5
        path.setText(outputPath);
        
        exitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//Success.this.finish();
				//Intent intent = new Intent(Intent.ACTION_MAIN);
				Intent intent = new Intent();
				intent.setClass(Success.this, MainActivity.class);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.putExtra("EXIT", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				//System.exit(0);
//				android.os.Process.killProcess(android.os.Process.myPid());
				//finish();
//				Intent intent = new Intent();
//				intent.setClass(Success.this, MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				intent.putExtra("EXIT", false);
//				startActivity(intent);
				//android.os.Process.killProcess(android.os.Process.myPid());
			}
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to send it via email now?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
//       				Intent intent = new Intent();
//    				intent.setClass(Success.this, Send.class);
//    				startActivity(intent);
                	   
                		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        				// 附件
        				//File file = new File("/storage/sdcard0/encoded/"+"a.bmp");
                		File file = new File(outputPath);
        				// 收件人
        				//intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"pop1030123@163.com"});
        				// 主题
        				intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "a.bmp");
        				// 正文
        				//intent.putExtra(android.content.Intent.EXTRA_TEXT,"this is test extra.");
        				intent.setType("application/octet-stream");
        				//当无法确认发送类型的时候使用如下语句
        				//intent.setType(“*/*”);
        				//当没有附件,纯文本发送时使用如下语句
        				//intent.setType(“plain/text”);
        				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        				//NoteActivity.mNext_tab = NoteActivity.NOTE_SETTING;
        				startActivity(Intent.createChooser(intent, "Mail Chooser"));
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        AlertDialog alert = builder.create();
        alert.setTitle("Success");  
        alert.show();
    }
    
    @Override  
    protected void onDestroy() {  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        System.runFinalizersOnExit(true);  
        System.exit(0);  
    } 
}
