package com.example.clf.mediaplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btn_previous;
    private Button btn_play;
    private Button btn_next;
    private Button btn_stop;
    private ListView list;

    private MediaPlayer player;
    private int number;//当前歌曲的序号，下标从1开始

    private static final int REQUEST_PERMISSION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        registerListeners();
        number=1;

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            Toast.makeText(this,"列表初始化",Toast.LENGTH_LONG).show();
        }
    }


   //获取显示组件
    private void findViews(){
        btn_previous=(Button)findViewById(R.id.previus);
        btn_play=(Button)findViewById(R.id.play);
        btn_next=(Button)findViewById(R.id.next);
        btn_stop=(Button)findViewById(R.id.stop);
        list=(ListView)findViewById(R.id.listView1);
    }
    //为显示组件注册监听器
    private void registerListeners(){

    }

    protected void onResume() {
        super.onResume();
        //初始化音乐列表
        initMusicList();
        if(list.getCount()==0){
            btn_previous.setEnabled(false);
            btn_play.setEnabled(false);
            btn_stop.setEnabled(false);
            btn_next.setEnabled(false);
            Toast.makeText(this,"未找到音乐文件！",Toast.LENGTH_LONG).show();
        }else{
            btn_previous.setEnabled(true);
            btn_play.setEnabled(true);
            btn_stop.setEnabled(true);
            btn_next.setEnabled(true);
        }
    }

    private void initMusicList(){

        Cursor cursor= getMusicCursor();
       // cursor.moveToFirst();
        setListContent(cursor);
    }

    private void setListContent(Cursor musicCursor){
        CursorAdapter adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,musicCursor,new String[]{
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ARTIST},new int[]{
                android.R.id.text1,android.R.id.text2 });
          list.setAdapter(adapter);
    }

    private Cursor getMusicCursor(){
        ContentResolver resolver = getContentResolver();
       /* File file=new File(Environment.getExternalStorageDirectory(),null);*/
        Cursor cursor=resolver.query(/*Uri.parse(file.getPath())*//*Uri.parse("file://storage/emulated/0/kgmusic/download")*/MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        return cursor;
    }





    private void moveNumberToNext(){
        if((number+1)>list.getCount()){
            number=1;
            Toast.makeText(this,"已到达列表底端",Toast.LENGTH_LONG).show();
        }else{
            ++number;
        }
    }

    private void moveNumberToPrevious(){
        if(number==1){
            number=list.getCount();
            Toast.makeText(this,"已到达列表顶端",Toast.LENGTH_LONG).show();
        }else{
            --number;
        }
    }
}
