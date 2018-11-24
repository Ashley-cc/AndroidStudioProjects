package com.example.clf.fragment2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        replaceFragment(new RightFragment());
        RightFragment rightfragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.right_fragment);*/
}

   /* public void onClick(View v){
        switch(v.getId()){
            case R.id.button:
               // replaceFragment(new AnotherRightFragment());
                break;
            default:
                break;
        }
    }*/

   /* private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_fragment,fragment);
        transaction.addToBackStack(null);//返回上一个碎片
        transaction.commit();
    }*/
}
