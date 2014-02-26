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
				// ����
				File file = new File("/storage/sdcard0/encoded/"+"a.bmp");
				// �ռ���
				//intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"pop1030123@163.com"});
				// ����
				intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "a.bmp");
				// ����
				//intent.putExtra(android.content.Intent.EXTRA_TEXT,"this is test extra.");
				intent.setType("application/octet-stream");
				//���޷�ȷ�Ϸ������͵�ʱ��ʹ���������
				//intent.setType(��*/*��);
				//��û�и���,���ı�����ʱʹ���������
				//intent.setType(��plain/text��);
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
