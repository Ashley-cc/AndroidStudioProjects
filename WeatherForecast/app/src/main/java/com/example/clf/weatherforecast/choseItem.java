package com.example.clf.weatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class choseItem extends AppCompatActivity implements View.OnClickListener {
    Button in_time;
    Button future;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_item);
        findButtons();
        in_time.setOnClickListener(this);
        future.setOnClickListener(this);
    }


    public void findButtons(){
        in_time=(Button)findViewById(R.id.in_time);
        future=(Button)findViewById(R.id.future);
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
        }
    }
}
