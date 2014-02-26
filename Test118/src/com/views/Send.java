package com.views;

import java.io.File;

//import views.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Send extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);
        
		Button emailButton = (Button) findViewById(R.id.email);
		Button bluetoothButton = (Button) findViewById(R.id.bluetooth);

		emailButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				// 附件
				File file = new File("/storage/sdcard0/encoded/"+"a.bmp");
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
		});
		
		bluetoothButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
    }
}
