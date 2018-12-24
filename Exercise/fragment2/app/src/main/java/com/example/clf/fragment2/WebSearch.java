package com.example.clf.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class WebSearch extends AppCompatActivity implements Runnable,View.OnClickListener{
    HttpURLConnection httpConn=null;
    InputStream din=null;
    String Word;

    LinearLayout body;
    FrameLayout main;
    Button find;
    EditText value;

    String us_phonetic=null;
    String uk_phonetic=null;
    //String explains=null;
    String errorCode=null;
    Vector<String> explains=new Vector<>();
  /*  Vector<String> uk_phonetic=new Vector<>();
    //Vector<String> days=new Vector<String>();
    Vector<String> explains=new Vector<>();*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        findViews();
        find.setOnClickListener(this);
        main.setBackgroundResource(R.drawable.back);
        //Intent intent=getIntent();
      //Word= intent.getStringExtra("searchWord");
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        body.removeAllViews();
        Word=value.getText().toString();
        Toast.makeText(this,"正在查询......",Toast.LENGTH_LONG).show();
        Thread th=new Thread(this);
        th.start();
        Log.v("abc3",Word);

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        explains.removeAllElements();
        parseData();
        Message message=new Message();
        message.what=1;

        handler.sendMessage(message);
    }

    private final Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    showData();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public void findViews(){
        body=(LinearLayout)findViewById(R.id.my_body);
        value=(EditText)findViewById(R.id.value);
        find=(Button)findViewById(R.id.find);
        main=(FrameLayout)findViewById(R.id.search);
    }

    public void parseData(){
        String weatherUrl="http://fanyi.youdao.com/openapi.do?keyfrom=webblog&&key=1223831798&type=data&doctype=xml&&version=1.1&q="+Word;
        Log.v("abcw",weatherUrl);
        try{
            URL url=new URL(weatherUrl);
            //建立天气预报查询连接
            httpConn=(HttpURLConnection)url.openConnection();
            //采用get请求方法
            httpConn.setRequestMethod("GET");

            //打开数据输入流
            din=httpConn.getInputStream();
            Log.v("abc2","未读xml");
            InputStreamReader in = new InputStreamReader(httpConn.getInputStream());
            //为输出创建BufferReader

            BufferedReader buffer = new BufferedReader(in);
            String inputLine="";
            String resultData="";
            //使用循环来读取获得的天气数据
            while((inputLine=buffer.readLine())!=null){
                //在每一行后面添加一个"/n"进行换行
                resultData+=inputLine;
            }
           // Log.v("abc",""+resultData);

            //获得XmlPullParser解析器
            XmlPullParser xmlParser= Xml.newPullParser();
            ByteArrayInputStream tInputStringStream=null;
            tInputStringStream=new ByteArrayInputStream(resultData.getBytes());

            xmlParser.setInput(tInputStringStream,"UTF-8");
            //获得解析到的事件类别,这里有开始文档、结束文档、开始标签、结束标签、文本等事件
            int evtType=xmlParser.getEventType(); //String evtType;

            while(evtType!= XmlPullParser.END_DOCUMENT){//一直循环，直到文档结束
                Log.v("abc2","读xml");
                String nodeName=xmlParser.getName();
                switch (evtType){
                    /*case "addata":*/
                    case XmlPullParser.START_TAG: {
                        if ("us-phonetic".equals(nodeName)) {
                            us_phonetic="美式音标："+xmlParser.nextText();
                        } else if ("uk-phonetic".equals(nodeName)) {
                            uk_phonetic="英式音标："+xmlParser.nextText();
                        } else if ("query".equals(nodeName)) {
                            Word=xmlParser.nextText();
                        } else if ("ex".equals(nodeName)||"key".equals(nodeName)) {
                                if("key".equals(nodeName)){
                                    explains.addElement("网络释义--词组： "+xmlParser.nextText());
                                }else {
                                    explains.addElement(xmlParser.nextText());
                                }
                                //if("ex".equals(nodeName)) {
                                //Log.v("abc","explains"+xmlParser.next());
                                //explains = explains + "\n" + xmlParser.nextText();
                                //}

                        }else if("errorCode".equals(nodeName)){
                            errorCode=xmlParser.nextText();
                            Log.v("abc1","errorcode"+xmlParser.next());
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:
                        //标签结束
                    default:break;
                }
                //如果xml没有结束,则移到下一个节点
                evtType=xmlParser.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //释放连接
            try{
                din.close();
                httpConn.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void showData(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight=80;
        params.height=80;
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.weight=80;
        params1.height=200;
        if(errorCode==null){
            new AlertDialog.Builder(this)
                    .setTitle("错误页面")//标题
                    .setMessage("找不到该词！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create()
                    .show();
        } else{
            TextView wordView = new TextView(this);
            wordView.setLayoutParams(params);
            wordView.setText(Word);
            wordView.setTextSize(20);
            wordView.setTextColor(getResources().getColor(R.color.colorPrimary));
            Log.v("word",Word);
            Log.v("word",us_phonetic);
            Log.v("word",uk_phonetic);
            //Log.v("word",explains);
            Log.v("word",Word);
            body.addView(wordView);

            TextView us_phoneticView = new TextView(this);
            us_phoneticView.setLayoutParams(params);
            us_phoneticView.setText(us_phonetic);
            us_phoneticView.setTextSize(20);
            us_phoneticView.setTextColor(getResources().getColor(R.color.colorPrimary));
            body.addView(us_phoneticView);

            TextView uk_phoneticView = new TextView(this);
            uk_phoneticView.setLayoutParams(params);
            uk_phoneticView.setText(uk_phonetic);
            uk_phoneticView.setTextSize(20);
            uk_phoneticView.setTextColor(getResources().getColor(R.color.colorPrimary));
            body.addView(uk_phoneticView);

            for(int i=0;i<explains.size();i++) {
                TextView explainsView = new TextView(this);
                explainsView.setLayoutParams(params);
                explainsView.setText(explains.elementAt(i));
                explainsView.setTextSize(20);
                explainsView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                body.addView(explainsView);
            }


        }
    }
}
