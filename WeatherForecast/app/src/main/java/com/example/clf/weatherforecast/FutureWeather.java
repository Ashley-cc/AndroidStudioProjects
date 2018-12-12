package com.example.clf.weatherforecast;

import android.content.DialogInterface;
import android.graphics.Color;
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
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

public class FutureWeather extends AppCompatActivity implements Runnable,View.OnClickListener{
    LinearLayout body;
    FrameLayout main;
    Button find;
    EditText value;
    HttpURLConnection httpConn=null;
    InputStream din=null;

    Vector<String> days=new Vector<String>();//日期
    Vector<String> week=new Vector<String>();//星期几
    Vector<String> citynm=new Vector<String>();//城市名称
    Vector<String> tem=new Vector<String>();//温度（高温/低温）
    Vector<String> weather=new Vector<String>();//天气情况
    Vector<String> icon=new Vector<String>();//天气图标
    Vector<String> wind=new Vector<String>();//风向
    Vector<String> winp=new Vector<String>();//风力
    Vector<String> tem_high=new Vector<String>();//最高温
    Vector<String> tem_low=new Vector<String>();//最低温
    String city="beijing";

    Calendar cal;
    String hour;
    /*String days;
    String week;
    String citynm;
    String tem;
    String weather;
    String icon;
    String wind;
    String windp;
    String tem_high;
    String tem_low;*/

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
        days.removeAllElements();
        week.removeAllElements();
        citynm.removeAllElements();
        //icon.removeAllElements();
        tem.removeAllElements();
        weather.removeAllElements();
        wind.removeAllElements();
        winp.removeAllElements();
        parseData();
        //downImage();
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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.future_main);
        findViews();
        find.setOnClickListener(this);
        main.setBackgroundResource(R.drawable.back3);
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if (cal.get(Calendar.AM_PM) == 0)
            hour = String.valueOf(cal.get(Calendar.HOUR));
        else
            hour = String.valueOf(cal.get(Calendar.HOUR)+12);

        Log.v("abc",hour);
    }
    public void findViews(){
        body=(LinearLayout)findViewById(R.id.my_body);
        value=(EditText)findViewById(R.id.value);
        find=(Button)findViewById(R.id.find);
        main=(FrameLayout)findViewById(R.id.future_main);
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
       /*if(city==null){
           new AlertDialog.Builder(this)
                   .setTitle("错误页面")//标题
                   .setMessage("请输入城市名称！")
                   .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                       }
                   })
                   .create()
                   .show();
       }*/
        Toast.makeText(this,"正在查询天气......",Toast.LENGTH_LONG).show();
        Thread th=new Thread(this);
        th.start();

    }


    //获取数据
    public void parseData(){
        int i=0;
        String svalue;
        //city为城市名字的拼音
        String weatherUrl="http://api.k780.com:88/?app=weather.future&weaid="+city+"&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=xml";//"http://flash.weather.com.cn/wmaps/xml/"+city+".xml";/*"http://api.k780.com:88/?app=weather.future&weaid="+"city"+"&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=xml";*//*"http://wthrcdn.etouch.cn/weather_mini?city=北京"*/;//"http://3g.163.com/touch/jsonp/sy/recommend/0-9.html?miss=48&refresh=B02callback=syrec4";//
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
                resultData+=inputLine;
            }
            //Log.v("abc",""+resultData);
            //获得XmlPullParser解析器
            XmlPullParser xmlParser= Xml.newPullParser();
            ByteArrayInputStream tInputStringStream=null;
            tInputStringStream=new ByteArrayInputStream(resultData.getBytes());

            xmlParser.setInput(tInputStringStream,"UTF-8");
            //获得解析到的事件类别,这里有开始文档、结束文档、开始标签、结束标签、文本等事件
            int evtType=xmlParser.getEventType(); //String evtType;
            while(evtType!= XmlPullParser.END_DOCUMENT){//一直循环，直到文档结束
                String nodeName=xmlParser.getName();
                switch (evtType){
                    /*case "addata":*/
                    case XmlPullParser.START_TAG: {
                        if ("days".equals(nodeName)) {
                            days.addElement("日期："+xmlParser.nextText());
                        } else if ("week".equals(nodeName)) {
                            week.addElement(xmlParser.nextText());
                        } else if ("citynm".equals(nodeName)) {
                            city=xmlParser.nextText();
                        } else if ("temperature".equals(nodeName)) {
                            tem.addElement("温度："+xmlParser.nextText());
                        } else if ("weather".equals(nodeName)) {
                            weather.addElement("天气："+xmlParser.nextText());
                        }else if ("wind".equals(nodeName)) {
                            wind.addElement("风向："+xmlParser.nextText());
                        }
                        else if ("winp".equals(nodeName)) {
                            winp.addElement("风力："+xmlParser.nextText());
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


    //显示结果
    public void showData(){
        body.removeAllViews();//清除存储原有的查询结果的组件
        body.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight=80;
        params.height=50;
        if(days.size()==0){
            new AlertDialog.Builder(this)
                    .setTitle("错误页面")//标题
                    .setMessage("请核对您输入的城市名称拼音是否正确！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create()
                    .show();
        }
        else{
            TextView cityView = new TextView(this);
            cityView.setLayoutParams(params);
            cityView.setText(city);
            cityView.setTextColor(getResources().getColor(R.color.colorAccent));
            body.addView(cityView);
            for(int i=0;i<days.size();i++) {
                LinearLayout linearLayout=new LinearLayout(this);
                LinearLayout Date = new LinearLayout(this);

                /*LinearLayout Weather = new LinearLayout(this);*/
                //LinearLayout Tem=new LinearLayout(this);
                LinearLayout Wind=new LinearLayout(this);
                //Tem.setOrientation(LinearLayout.HORIZONTAL);
                Wind.setOrientation(LinearLayout.HORIZONTAL);
                Date.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                //linearlayout.setBackgroundResource(R.drawable.back2);
                //linearlayout.setOrientation(LinearLayout.HORIZONTAL);
                //城市
              /*  TextView cityView = new TextView(this);
                cityView.setLayoutParams(params);
                cityView.setText(cityname.elementAt(i));
                cityView.setTextColor(getResources().getColor(R.color.colorAccent));
                City.addView(cityView);*/

                //日期
                TextView daysView = new TextView(this);
                daysView.setLayoutParams(params);
                daysView.setText(days.elementAt(i));
                daysView.setTextColor(getResources().getColor(R.color.colorPrimary));
                Date.addView(daysView);

                //body.addView(Days);
                //星期几
                TextView weekView = new TextView(this);
                weekView.setLayoutParams(params);
                weekView.setText(week.elementAt(i));
                Date.addView(weekView);
                weekView.setTextColor(getResources().getColor(R.color.colorPrimary));
                linearLayout.addView(Date);
                //Weather.addView(summaryView);
                //图标
        /*   ImageView iconView=new ImageView(this);
           iconView.setLayoutParams(params);
           iconView.setImageBitmap(bitmap.elementAt(i));
           linearlayout.addView(iconView);*/
                //温度
                TextView temView = new TextView(this);
                temView.setLayoutParams(params);
                temView.setText(tem.elementAt(i));
                linearLayout.addView(temView);
                //天气
                TextView highView = new TextView(this);
                highView.setLayoutParams(params);
                highView.setText(weather.elementAt(i));
                linearLayout.addView(highView);
                //风向
                TextView windView = new TextView(this);
                windView.setLayoutParams(params);
                windView.setText(wind.elementAt(i));
                Wind.addView(windView);
                //风力
                TextView winpView = new TextView(this);
                winpView.setLayoutParams(params);
                winpView.setText(winp.elementAt(i));
                Wind.addView(winpView);
                linearLayout.addView(Wind);
               // linearLayout.setBackgroundColor(getResources().getColor(R.color.colorItem));
                //linearLayout.setBackgroundColor(Color.parseColor("#50323232"));
                body.addView(linearLayout);
            }
        }
    }


    
}
