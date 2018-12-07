package com.example.clf.mediaplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service{
    //播放控制命令，标识操作
    public static final int COMMAND_UNKNOWN = -1;
    public static final int COMMAND_PLAY = 0;
    public static final int COMMAND_PAUSE=1;
    public static final int COMMAND_STOP=2;
    public static final int COMMAND_RESUME=3;
    public static final int COMMAND_PREVIOUS=4;
    public static final int COMMAND_NEXT=5;
    public static final int COMMAND_CHECK_IS_PLAYING=6;
    public static final int COMMAND_SEEK_TO=7;
    public static final int COMMAND_LOOP=8;
    public static final int COMMAND_UNLOOP=9;
    public static final int COMMAND_RANDOM=10;
    //播放状态
    public static final int STATUS_PLAYING=0;
    public static final int STATUS_PAUSED=1;
    public static final int STATUS_STOPPED=2;
    public static final int STATUS_COMPLETED=3;
    public static final int STATUS_LOOP=4;
    //广播标识
    public static final String BROADCAST_MUSICSERVICE_CONTROL="MusicService.ACTION_CONTROL";
    public static final String BROADCAST_MUSICSERVICE_UPDATE_STATUS="MusicService.ACTION_UPDATE";
    //广播接收器
    private CommandReceiver receiver;
    private MediaPlayer player;
   String path= "/storage/emulated/0/kgmusic/download/群星 - Give Me Your Love Tonight.mp3";
    String test;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        bindCommandReceiver();
        player = new MediaPlayer();
        Toast.makeText(this,"MusicService.onCreate()",Toast.LENGTH_LONG).show();
    }
    public void onStart(Intent intent,int startId){
        super.onStart(intent,startId);
    }

    public void onDestroy(){
        if(player!=null){
            player.release();
        }
        super.onDestroy();
    }




    private void load(int number) {
        //player=new MediaPlayer();
        //this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //this.player.setDataSource(musicUri); // 为本地的绝对路径，如/sdcard/abc/123.wav
        //this.player.prepare();
        //player.prepare();
        //player.start();
        //player.reset();
        if(player!=null&&player.isPlaying())
            player.release();
        try{

 /*player=new MediaPlayer();
            File file=new File(Environment.getExternalStorageDirectory(),null)G;
            player.setDataSource(file.getPath());
            player.prepare();*/

            Uri musicUri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, /*"小星星.mp3"*/""+ number);
         //   player = MediaPlayer.create(this,musicUri);

            Log.v("abc",musicUri.toString());
            //int number = intent.getIntExtra("number",1);
           // String path=this.getIntExtra("path",path);
            player.reset();
            player.setDataSource(this, Uri.parse(path));
            Log.v("service test","11"+path);

            //player.setDataSource(this, Uri.parse("/storage/emulated/0/kgmusic/download/群星 - Give Me Your Love Tonight.mp3"));
            player.prepare();
/*Uri.parse("file://storage/emulated/0/kgmusic/download") musicUri)
;*/

            //player.prepare();
  /*player=new MediaPlayer();

           player.reset();
           player.setDataSource("content://media/external/audio/media");
           player.prepareAsync();
           player.start();*/
            player.setOnCompletionListener(completionListener);//注册监听器
            // player.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//播放结束监听器
    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if(mp.isLooping()||loop==true){

                Log.v("abc","循环");
                //sendBroadcastOnStatusChanged(MusicService.STATUS_LOOP);
                replay();
            }
            else {
                Log.v("abc","播放完成");
                sendBroadcastOnStatusChanged(MusicService.STATUS_COMPLETED);

            }
        }
    };

    private void play(int number){
        if(player!=null && player.isPlaying()){
            player.stop();
        }
        load(number);
        player.start();
        sendBroadcastOnStatusChanged(MusicService.STATUS_PLAYING);
    }

    private void pause(){
        if(player.isPlaying()){
            player.pause();
            sendBroadcastOnStatusChanged(MusicService.STATUS_PAUSED);
        }
    }

    private void stop(){
        if(player!=null){
            player.stop();
            sendBroadcastOnStatusChanged(MusicService.STATUS_STOPPED);
        }
    }
//恢复播放
    private void resume(){
        player.start();
        sendBroadcastOnStatusChanged(MusicService.STATUS_PLAYING);
    }
//重新播放
    private void replay(){
        player.start();
        //sendBroadcastOnStatusChanged(MusicService.STATUS_STOPPED);
        sendBroadcastOnStatusChanged(MusicService.STATUS_PLAYING);
    }

    private void loop(){
        player.setLooping(true);
        //sendBroadcastOnStatusChanged(MusicService.STATUS_LOOP);
    }
    private void unloop(){
        player.setLooping(false);
        //sendBroadcastOnStatusChanged(MusicService.STATUS_LOOP);
    }
   //跳转至播放的位置
    private void seekTo(int time){
        if(player!=null){
            player.seekTo(time);
        }
    }
boolean loop=false;
 /*boolean random=false;*/
   class CommandReceiver extends BroadcastReceiver{
        public void onReceive(Context context,Intent intent){
            //unregisterReceiver(this);
            //获取命令
            //String path = intent.getIntExtra("path","/storage/emulated/0/kgmusic/download/群星 - Give Me Your Love Tonight.mp3");
            //int number = intent.getIntExtra("number",1);
            int command = intent.getIntExtra("command",COMMAND_UNKNOWN);
            //执行命令
            switch (command){
                case COMMAND_SEEK_TO:
                    seekTo(intent.getIntExtra("time",0));
                    break;
                case COMMAND_PLAY:
                case COMMAND_PREVIOUS:
                case COMMAND_NEXT:
                    int number = intent.getIntExtra("number",1);
                     path = intent.getStringExtra("path");
                    //test=intent.getStringExtra("test");
                  // Log.v("service test","11"+test);
                    Toast.makeText(MusicService.this,"正在播放第"+number+"首",Toast.LENGTH_LONG).show();
                    play(number);
                    break;
                case COMMAND_PAUSE:
                    pause();
                    break;
                case COMMAND_STOP:
                    stop();
                    break;
                case COMMAND_RESUME:
                    resume();
                    break;
                case COMMAND_LOOP:
                   /* int number = intent.getIntExtra("number",1);
                    path = intent.getStringExtra("path");
                    //test=intent.getStringExtra("test");
                    // Log.v("service test","11"+test);
                    Toast.makeText(MusicService.this,"正在播放第"+number+"首",Toast.LENGTH_LONG).show();
                    play(number);*/
                    loop=true;
                    break;
                case COMMAND_UNLOOP:
                    loop=false;
                    //unloop();
                    break;
              /*  case COMMAND_RANDOM:
                    random=true;
                    break;*/
                case COMMAND_RANDOM:
                    loop=false;
                    break;
                case COMMAND_CHECK_IS_PLAYING:
                    if(player.isPlaying()){
                        sendBroadcastOnStatusChanged(MusicService.STATUS_PLAYING);
                    }
                    break;
                case COMMAND_UNKNOWN:
                default:
                    break;
            }
        }
   }

   private void bindCommandReceiver(){
        receiver = new CommandReceiver();
       IntentFilter filter = new IntentFilter(BROADCAST_MUSICSERVICE_CONTROL);
       registerReceiver(receiver,filter);
   }

   private void sendBroadcastOnStatusChanged(int status){
        Intent intent=new Intent(BROADCAST_MUSICSERVICE_UPDATE_STATUS);
        intent.putExtra("status",status);
        if(status==STATUS_PLAYING){
            intent.putExtra("time",player.getCurrentPosition());
            intent.putExtra("duration",player.getDuration());
        }
        sendBroadcast(intent);
    }

}
