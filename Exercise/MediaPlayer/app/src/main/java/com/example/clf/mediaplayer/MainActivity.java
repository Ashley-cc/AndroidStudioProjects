package com.example.clf.mediaplayer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout Back;
    private Button btn_previous;
    private Button btn_play;
    private Button btn_next;
    private Button btn_stop;
    //private Button btn_loop;
    private ListView list;
    private TextView text_Current;
    private TextView text_Duration;
    private SeekBar seekBar;


  boolean random=false;
    private Handler seekBarHandler;//更新进度条的Handler
    private int duration;//当前歌曲的持续时间和当前位置，作用于进度条
    private int time;

    //进度条控制常量
    private static final int PROCESS_INCREASE = 0;
    private static final int PROCESS_PAUSE = 1;
    private static final int PROCESS_RESET = 2;

    private Cursor cursor;
    // private String path;
    private int status;
    private int number;//当前歌曲的序号，下标从1开始
    private StatusChangedReceiver receiver;

    //boolean loop=false;

    public MainActivity() {
    }

    //private static final int REQUEST_PERMISSION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        registerListeners();
        number = 1;
        status = MusicService.STATUS_STOPPED;
        duration = 0;
        time = 0;
        startService(new Intent(this, MusicService.class));
        bindStatusChangedReceiver();
        sendBroadcastOnCommand(MusicService.COMMAND_CHECK_IS_PLAYING);
        initSeekBarHandler();
        Back.setBackgroundResource(R.drawable.back1);

        /*if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            //initMusicList();
            Toast.makeText(this,"列表初始化...",Toast.LENGTH_LONG).show();
        }*/
    }




 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"权限允许",Toast.LENGTH_SHORT).show();
               // initMusicList();
            }else{
                Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            default:
        }
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    private void bindStatusChangedReceiver(){
       receiver = new StatusChangedReceiver();
        IntentFilter filter = new IntentFilter(MusicService.BROADCAST_MUSICSERVICE_UPDATE_STATUS);
        registerReceiver(receiver,filter);

    }


   //获取显示组件
    private void findViews(){
        Back=(RelativeLayout) findViewById(R.id.Back);
        btn_previous=(Button)findViewById(R.id.previous);
        btn_play=(Button)findViewById(R.id.play);
        btn_next=(Button)findViewById(R.id.next);
        btn_stop=(Button)findViewById(R.id.stop);
        //btn_loop=(Button)findViewById(R.id.loop);
        list=(ListView)findViewById(R.id.listView1);
        seekBar=(SeekBar)findViewById(R.id.seekBar1);
        text_Current=(TextView)findViewById(R.id.textView1);
        text_Duration=(TextView)findViewById(R.id.textView2);
    }
    //为显示组件注册监听器
    private void registerListeners(){
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcastOnCommand(MusicService.COMMAND_PREVIOUS);
                //moveNumberToPrevious();
                //play(number);
                //btn_play.setBackground();
            }
        });
     //播放
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying()){
                    sendBroadcastOnCommand(MusicService.COMMAND_PAUSE);
                }else if(isPaused()){
                    sendBroadcastOnCommand(MusicService.COMMAND_RESUME);
                }else if(isStopped()){
                    sendBroadcastOnCommand(MusicService.COMMAND_PLAY);
                }
               /* if(player!=null && player.isPlaying()){
                    pause();
                }else{
                    //player.prepare();
                    play(number);//play(number);
                }*/
            }
        });
//下一首
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*moveNumberToNext();
                play(number);*/
                sendBroadcastOnCommand(MusicService.COMMAND_NEXT);
            }
        });
//停止
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcastOnCommand(MusicService.COMMAND_STOP);
               // stop();
            }
        });
        /*btn_loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcastOnCommand(MusicService.COMMAND_LOOP);
                // stop();
            }
        });*/

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number=position+1;
               // TextView textView=view.findViewById(android.R.id.text1);
                //path=  textView.getText().toString();
                //Log.v("abc",path);
                sendBroadcastOnCommand(MusicService.COMMAND_PLAY);
                //play(number);
            }
        });


        seekBar.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch (SeekBar seekBar){
                 //发送广播给MusicService，执行跳转
                sendBroadcastOnCommand(MusicService.COMMAND_SEEK_TO);
                if(isPlaying()){
                    //进度条恢复移动
                    seekBarHandler.sendEmptyMessageDelayed(PROCESS_INCREASE,1000);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //更新文本
                time=progress;
                text_Current.setText(formatTime(time));
            }

            public void onStartTrackingTouch (SeekBar seekBar){
                //进度条暂停移动
                seekBarHandler.sendEmptyMessage(PROCESS_PAUSE);
            }

        });


    }

    protected void onResume() {
        super.onResume();

       /* if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{*/
            initMusicList();
        /*    Toast.makeText(this,"列表初始化...",Toast.LENGTH_LONG).show();
        }*/
        //初始化音乐列表
       //initMusicList();
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
        //path=getPath(1);
       // cursor.moveToFirst();
        //getPath(0);
        setListContent(cursor);
    }

    private void setListContent(Cursor musicCursor){
        CursorAdapter adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,musicCursor,new String[]{
                //MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.TITLE,

                MediaStore.Audio.AudioColumns.ARTIST},new int[]{
                android.R.id.text1,android.R.id.text2 });
          list.setAdapter(adapter);
    }

    private String getPath(int number){
        cursor.moveToPosition(number-1);
       /* cursor.moveToFirst();
        for(int i=0;i<number-1;i++) {
            cursor.moveToNext();
        }*/
        return cursor.getString(1);
      //  Log.v("testpath", cursor.getString(1));
        //return cursor.getString(0);
    }
   /* private ArrayList<String> getPath{

    }*/

    private Cursor getMusicCursor(){
        ContentResolver resolver = getContentResolver();
       //File file=new File(Environment.getExternalStorageDirectory(),null);
        cursor=resolver.query(/*Uri.parse(file.getPath())*//*Uri.parse("file://storage/emulated/0/kgmusic/download")*/MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
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



    //发送命令，控制音乐播放
    private void sendBroadcastOnCommand(int command){
        Intent  intent = new Intent(MusicService.BROADCAST_MUSICSERVICE_CONTROL);
        intent.putExtra("command",command);

        //根据不同的命令，封装不同的数据
        switch(command){

            case MusicService.COMMAND_PLAY:
                intent.putExtra("number",number);
                //intent.putExtra("test",getPath(number));
                //intent.putExtra("test",getPath(number));
                intent.putExtra("path",getPath(number));
                break;
            case MusicService.COMMAND_PREVIOUS:
                moveNumberToPrevious();
                //intent.putExtra("path",path);
                intent.putExtra("number",number);
                intent.putExtra("path",getPath(number));
               // intent.putExtra("path",path);
                break;
            case MusicService.COMMAND_NEXT:
                //moveNumberToNext();

                if(random==true){
                    number=new Random().nextInt(7)+1;
                    /*intent.putExtra("number",new Random().nextInt(7)+1);
                    Log.v("abc","数字"+number);
                    Log.v("abc","random"+random);*/
                }
                else {
                    moveNumberToNext();
                }
                intent.putExtra("number", number);
                intent.putExtra("path", getPath(number));
                //intent.putExtra("path",path);
                break;
                case MusicService.COMMAND_SEEK_TO:
                    intent.putExtra("time",time);
                    break;
           /* case MusicService.COMMAND_RANDOM:
                random=true;
                    break;*/
           /* case MusicService.COMMAND_LOOP:
            case MusicService.COMMAND_UNLOOP:*/
                /*intent.putExtra("loop",true);*/
            case MusicService.COMMAND_PAUSE:
            case MusicService.COMMAND_STOP:
            case MusicService.COMMAND_RESUME:

            default:
                break;
        }
        sendBroadcast(intent);
    }


    //播放状态
    //private int status;
    //是否正在播放音乐
    private boolean isPlaying(){
        return status==MusicService.STATUS_PLAYING;
    }
    //是否暂停了播放音乐
    private boolean isPaused(){
        return status==MusicService.STATUS_PAUSED;
    }
    //是否停止状态
    private boolean isStopped(){
        return status==MusicService.STATUS_STOPPED;
    }
    public void onDestroy(){
        if(isStopped()){
            stopService(new Intent(this,MusicService.class));
        }
        super.onDestroy();
    }

    class StatusChangedReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            status=intent.getIntExtra("status",-1);
            switch (status){
                case MusicService.STATUS_PLAYING:
                    time=intent.getIntExtra("time",0);
                    duration=intent.getIntExtra("duration",0);
                    seekBarHandler.removeMessages(PROCESS_INCREASE);
                    seekBarHandler.sendEmptyMessageDelayed(PROCESS_INCREASE,1000);
                    seekBar.setMax(duration);
                    seekBar.setProgress(time);
                    text_Duration.setText(formatTime(duration));
                    //设置Activity标题栏文字，提示正在播放的歌曲
                    Cursor cursor=getMusicCursor();
                    cursor.moveToPosition(number-1);
                    String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                    setTitle("正在播放:"+title+"-MediaPlayer");
                    break;
                case MusicService.STATUS_PAUSED:
                    seekBarHandler.sendEmptyMessage(PROCESS_PAUSE);
                    break;
                case MusicService.STATUS_STOPPED:
                    seekBarHandler.sendEmptyMessage(PROCESS_RESET);
                    break;
                case MusicService.STATUS_COMPLETED:
                    sendBroadcastOnCommand(MusicService.COMMAND_NEXT);
                    seekBarHandler.sendEmptyMessage(PROCESS_RESET);
                    break;
                    /*if(status==MusicService.STATUS_LOOP){
                        sendBroadcastOnCommand(MusicService.COMMAND_PREVIOUS);
                    }*/
                   /* if(status==MusicService.STATUS_LOOP){
                        moveNumberToPrevious();
                    }*/


               /* case MusicService.STATUS_LOOP:
                    seekBarHandler.sendEmptyMessage(PROCESS_RESET);
                    //sendBroadcastOnCommand(MusicService.COMMAND_LOOP);
                    break;*/
                   /* time=intent.getIntExtra("time",0);
                    duration=intent.getIntExtra("duration",0);
                    seekBarHandler.removeMessages(PROCESS_INCREASE);
                    seekBarHandler.sendEmptyMessageDelayed(PROCESS_INCREASE,1000);
                    seekBar.setMax(duration);
                    seekBar.setProgress(time);B
                    text_Duration.setText(formatTime(duration));break;*/
                    default:
                        break;

            }

            //updateUI(status);
        }

       /* private void updateUI(int status){
            switch(status){
                case MusicService.STATUS_PLAYING:

            }
        }*/
    }

    //格式化：毫秒“mm:ss”
    private String formatTime(int msec){
        int minute=(msec/1000)/60;
        int second=(msec/1000)%60;
        String minuteString;
        String secondString;
        if(minute<10){
            minuteString="0"+minute;
        }else{
            minuteString=""+minute;
        }

        if(second<10){
            secondString="0"+second;
        }else{
            secondString=""+second;
        }
        return minuteString+":"+secondString;
    }

    private void initSeekBarHandler(){
        seekBarHandler=new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);

                switch(msg.what){
                    case PROCESS_INCREASE:
                        if(seekBar.getProgress()<duration){
                            //进度条前进一秒
                            seekBar.incrementProgressBy(1000);
                            seekBarHandler.sendEmptyMessageDelayed(PROCESS_INCREASE,1000);//1000毫秒之后执行这一动作
                            //修改显示当前进度的文本
                            text_Current.setText(formatTime(time));
                            time=time+1000;
                        }break;
                    case PROCESS_PAUSE:
                        seekBarHandler.removeMessages(PROCESS_INCREASE);break;
                    case PROCESS_RESET:
                        //重置进度条界面
                        seekBarHandler.removeMessages(PROCESS_INCREASE);
                        seekBar.setProgress(0);
                        text_Current.setText("00:00");break;
                }
            }
        };
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.loop:
                sendBroadcastOnCommand(MusicService.COMMAND_LOOP);
                random=false;
                break;
            case R.id.unloop:
                sendBroadcastOnCommand(MusicService.COMMAND_UNLOOP);
                random=false;
                break;
            case R.id.random:
                random=true;
                sendBroadcastOnCommand(MusicService.COMMAND_RANDOM);
                break;
            case R.id.theme:
                new AlertDialog.Builder(this)
                        .setTitle("请选择主题")
                        .setItems(R.array.theme, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] themes=MainActivity.this.getResources().getStringArray(R.array.theme);
                                setTheme(themes[which]);
                            }
                        }).show();
                break;
            case R.id.about:
                new AlertDialog.Builder(this)
                        .setTitle("MediaPlayer")
                        .setMessage(this.getString(R.string.about)).show();
                break;
                default:
                    break;
        }
        return true;
    }

    private void setTheme(String theme){
        if("花瓣".equals(theme)){
            Back.setBackgroundResource(R.drawable.back1);
        }else if("欢庆".equals(theme)){
            Back.setBackgroundResource(R.drawable.back2);
        }else if("动漫".equals(theme)){
            Back.setBackgroundResource(R.drawable.back3);
        }else if("蓝色小熊".equals(theme)){
            Back.setBackgroundResource(R.drawable.back4);
        }
    }

   /* private void setTheme(){
        root_Layout.setB
    }*/



}

