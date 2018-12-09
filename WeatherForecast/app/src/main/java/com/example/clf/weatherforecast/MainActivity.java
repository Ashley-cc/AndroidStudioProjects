package com.example.clf.weatherforecast;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity implements Runnable,View.OnClickListener{
    HttpURLConnection httpConn=null;
    InputStream din=null;
    Vector<String> cityname=new Vector<String>();
    Vector<String> low=new Vector<String>();
    Vector<String> high=new Vector<String>();
    Vector<Bitmap> bitmap=new Vector<Bitmap>();
    Vector<String> icon=new Vector<String>();
    Vector<String> summary=new Vector<String>();
    Vector<String> windDir=new Vector<String>();
    Vector<String> windPower=new Vector<String>();
    int weatherIndex[] =new int[20];
    String city="guangzhou";
    boolean bPress=false;
    boolean bHasData=false;
    LinearLayout body;
    Button find;
    EditText value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("天气查询");
        findViews();
        find.setOnClickListener(this);

    }
    public void findViews(){
        body=(LinearLayout)findViewById(R.id.my_body);
        value=(EditText)findViewById(R.id.value);
        find=(Button)findViewById(R.id.find);
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
        cityname.removeAllElements();
        low.removeAllElements();
        high.removeAllElements();
        icon.removeAllElements();
        bitmap.removeAllElements();
        summary.removeAllElements();
        parseData();
        //downImage();
        Message message=new Message();
        message.what=1;

        handler.sendMessage(message);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        body.removeAllViews();
       city=value.getText().toString();
        Toast.makeText(this,"正在查询天气......",Toast.LENGTH_LONG).show();
        Thread th=new Thread(this);
        th.start();
    }

    //获取数据
    public void parseData(){
        int i=0;
        String svalue;
        //city为城市名字的拼音
        String weatherUrl="http://flash.weather.com.cn/wmaps/xml/"+city+".xml";
        //String weatherIcon="http://m.weather.com.cn/img/c";
        try{
            URL url=new URL(weatherUrl);
            //建立天气预报查询连接
            httpConn=(HttpURLConnection)url.openConnection();
            //采用get请求方法
            httpConn.setRequestMethod("GET");
            //打开数据输入流
            din=httpConn.getInputStream();
            InputStreamReader in = new InputStreamReader(httpConn.getInputStream());
            //为输出创建BufferReader
            BufferedReader buffer = new BufferedReader(in);

            String inputLine="";
            String resultData="";
            //使用循环来读取获得的天气数据
            while((inputLine=buffer.readLine())!=null){
                //在每一行后面添加一个"/n"进行换行
                resultData+=inputLine+"/n";
            }
            //获得XmlPullParser解析器
            XmlPullParser xmlParser= Xml.newPullParser();
            ByteArrayInputStream tInputStringStream=null;
            tInputStringStream=new ByteArrayInputStream(resultData.getBytes());
            xmlParser.setInput(tInputStringStream,"UTF-8");
            //获得解析到的事件类别,这里有开始文档、结束文档、开始标签、结束标签、文本等事件
            int evtType=xmlParser.getEventType();
            while(evtType!= XmlPullParser.END_DOCUMENT){//一直循环，直到文档结束
                switch (evtType){
                    case XmlPullParser.START_TAG:
                        String tag=xmlParser.getName();
                        //如果从city标签开始，则说明有新的一条城市天气信息
                        if(tag.equalsIgnoreCase("city")){
                            //城市天气预报
                            cityname.addElement(xmlParser.getAttributeValue(null,"cityname"));
                            //天气情况概述
                            summary.addElement(xmlParser.getAttributeValue(null,"stateDetailed"));
                            //最低温度
                            low.addElement("最低："+xmlParser.getAttributeValue(null,"tem2"));
                            //最高温度
                            high.addElement("最高："+xmlParser.getAttributeValue(null,"tem1"));
                            windDir.addElement("风向： "+xmlParser.getAttributeValue(null,"windDir"));
                            windPower.addElement("风力： "+xmlParser.getAttributeValue(null,"windPower"));
                            //icon.addElement(weatherIcon+xmlParser.getAttributeValue(null,"state1")+".gif");
                        }break;

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

    //显示结果
    public void showData(){
        body.removeAllViews();//清除存储原有的查询结果的组件
        body.setOrientation(LinearLayout.VERTICAL);
       LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       params.weight=80;
       params.height=50;
       for(int i=0;i<cityname.size();i++){
           LinearLayout linearlayout1=new LinearLayout(this);
           LinearLayout linearlayout=new LinearLayout(this);
           linearlayout.setBackgroundResource(R.drawable.back2);
           linearlayout.setOrientation(LinearLayout.HORIZONTAL);
           //城市
           TextView cityView=new TextView(this);
           cityView.setLayoutParams(params);
           cityView.setText(cityname.elementAt(i));
           linearlayout1.addView(cityView);
           //天气描述
           TextView summaryView=new TextView(this);
           summaryView.setLayoutParams(params);
           summaryView.setText(summary.elementAt(i));
           linearlayout.addView(summaryView);
           //图标
          /* ImageView iconView=new ImageView(this);
           iconView.setLayoutParams(params);
           iconView.setImageBitmap(bitmap.elementAt(i));
           linearlayout.addView(iconView);*/
          //最低温度
           TextView lowView= new TextView(this);
           lowView.setLayoutParams(params);
           lowView.setText(low.elementAt(i)+"度");
           linearlayout.addView(lowView);
           //最高温度
           TextView highView= new TextView(this);
           highView.setLayoutParams(params);
           highView.setText(high.elementAt(i)+"度");
           linearlayout.addView(highView);

           //风向
           TextView windDirView= new TextView(this);
           windDirView.setLayoutParams(params);
           windDirView.setText(windDir.elementAt(i));
           linearlayout.addView(windDirView);

           //风力
           TextView windStateView= new TextView(this);
           windStateView.setLayoutParams(params);
           windStateView.setText(windPower.elementAt(i));
           linearlayout.addView(windStateView);

           body.addView(linearlayout1);
           body.addView(linearlayout);
       }
    }
}
