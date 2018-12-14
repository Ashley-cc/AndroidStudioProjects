package com.example.clf.weatherforecast;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

public class choseItem extends AppCompatActivity implements View.OnClickListener {
    Button in_time;
    Button future;
    LinearLayout start;
    Button button1;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_item);
        findButtons();
        in_time.setOnClickListener(this);
        future.setOnClickListener(this);
        start.setBackgroundResource(R.drawable.back4);

      /*  button1.setOnClickListener(this);
        button2.setOnClickListener(this);*/

      /*  final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.chose_layout, null);
        new AlertDialog.Builder(this)
                .setTitle("新增单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动
              *//*  .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        }
                        // LeftFragment leftFragment=new LeftFragment();
                        //leftFragment.add
                        //ArrayList<Map<String, String>> items = getAll();
                        //setWordsListView(items);

                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })*//*
                .create()//创建对话框
                .show();//显示对话框*/
    }


    public void findButtons(){
        in_time=(Button)findViewById(R.id.in_time);
        future=(Button)findViewById(R.id.future);
        start=(LinearLayout)findViewById(R.id.start);
      /*  button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);*/
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.in_time:
                Intent intent1=new Intent(this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.future:
                Intent intent2=new Intent(this,FutureWeather.class);
                startActivity(intent2);
                break;
           /* case R.id.button1:
                Intent intent3=new Intent(this,MainActivity.class);
                startActivity(intent3);
                break;
            case R.id.button2:
                Intent intent4=new Intent(this,FutureWeather.class);
                startActivity(intent4);
                break;*/
        }
    }





}
