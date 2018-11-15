package com.example.clf.calculator;

/*import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}*/
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView text,text1,text2;//三个文本域
    double num1, num2;//操作数
    double sum;//运算结果
    StringBuffer temp = new StringBuffer();//输入字串
    char op;//操作符
    boolean mcount;//累加器计数标志
    boolean num2sign;//运算符标志（表示准备好输入第二个数）
    boolean comeout;//运算结果标志
    boolean Mnum;//累加器取数标志
    boolean num1alive;//操作数标志
    boolean num1is_ngt,num2is_ngt;//操作数正负标志
    boolean decimal;//小数点标志


    //初始化全局变量并设置监听器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.textView);//输入文本域
        text1 = (TextView) findViewById(R.id.textView1);//历史文本域一
        text2 = (TextView) findViewById(R.id.textView2);//历史文本域
        num1 = 0;//计数值1
        num2 = 0;//计数值2
        num2sign = false;//新计数值
        decimal = false;//小数点标记
        num1is_ngt=false;//判断第一个数的正负
        num2is_ngt=false;//第二个数的正负
        num1alive=false;//计数值一是否存在
        comeout=false;//当前是否显示计算结果
        mcount=false;//是否有累加
        Mnum=false;//是否取计数值
        sum=0;
        op='+';
        Button btn0 = (Button) findViewById(R.id.button0);
        btn0.setOnClickListener(this);
        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);
        Button btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(this);
        Button btn5 = (Button) findViewById(R.id.button5);
        btn5.setOnClickListener(this);
        Button btn6 = (Button) findViewById(R.id.button6);
        btn6.setOnClickListener(this);
        Button btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(this);
        Button btn8 = (Button) findViewById(R.id.button8);
        btn8.setOnClickListener(this);
        Button btn9 = (Button) findViewById(R.id.button9);
        btn9.setOnClickListener(this);
        Button btnpoint = (Button) findViewById(R.id.buttonpoint);
        btnpoint.setOnClickListener(this);
        Button btnadd = (Button) findViewById(R.id.buttonadd);
        btnadd.setOnClickListener(this);
        Button btnsub = (Button) findViewById(R.id.buttonsub);
        btnsub.setOnClickListener(this);
        Button btnmlt = (Button) findViewById(R.id.buttonmlt);
        btnmlt.setOnClickListener(this);
        Button btndev = (Button) findViewById(R.id.buttondev);
        btndev.setOnClickListener(this);
        Button btnmadd = (Button) findViewById(R.id.buttonmadd);
        btnmadd.setOnClickListener(this);
        Button btnmsub = (Button) findViewById(R.id.buttonmsub);
        btnmsub.setOnClickListener(this);
        Button btnmr = (Button) findViewById(R.id.buttonmr);
        btnmr.setOnClickListener(this);
        Button btnmc = (Button) findViewById(R.id.buttonmc);
        btnmc.setOnClickListener(this);
        Button btnans = (Button) findViewById(R.id.buttonans);
        btnans.setOnClickListener(this);
        Button btnsqrt = (Button) findViewById(R.id.buttonsqrt);
        btnsqrt.setOnClickListener(this);
        Button btndel = (Button) findViewById(R.id.buttondel);
        btndel.setOnClickListener(this);
        Button btnc = (Button) findViewById(R.id.buttonc);
        btnc.setOnClickListener(this);

    }

    //实现所有按钮事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                save('0');
                break;
            case R.id.button1:
                save('1');
                break;
            case R.id.button2:
                save('2');
                break;
            case R.id.button3:
                save('3');
                break;
            case R.id.button4:
                save('4');
                break;
            case R.id.button5:
                save('5');
                break;
            case R.id.button6:
                save('6');
                break;
            case R.id.button7:
                save('7');
                break;
            case R.id.button8:
                save('8');
                break;
            case R.id.button9:
                save('9');
                break;
            case R.id.buttonmlt:
                opchar('*');
                break;
            case R.id.buttonsub:
                opchar('-');
                break;
            case R.id.buttonadd:
                opchar('+');
                break;
            case R.id.buttondev:
                opchar('/');
                break;
            case R.id.buttonpoint:
                if(!decimal) {
                    decimal=true;
                    save('.');
                }

                break;
            case R.id.buttonmadd:
                M('+');
                break;
            case R.id.buttonmsub:
                M('-');
                break;
            case R.id.buttonmr:
                M('R');
                break;
            case R.id.buttonmc:
                M('C');
                break;
            case R.id.buttonc:
                start();
                text.setText("");
                text1.setText("");
                text2.setText("");
                break;
            case R.id.buttonsqrt:
                caulsqrt();
                break;
            case R.id.buttondel:
                back();
                break;
            case R.id.buttonans:
                result();
                break;

        }
    }

    //回退键
    public void back() {
        if(comeout){
            start();
            text.setText("");
        }else if(temp.length()>0 && !num2sign ) {
            temp.deleteCharAt(temp.length() - 1);
            text.setText(temp.toString());
        }
    }
    //累加器
    public void M(char o) {
        switch (o){
            case '+':
                if(comeout){
                    sum+=num1;
                    mcount=true;
                }
                else if(temp.length()>0 && !num2sign){
                    sum+=Double.parseDouble(temp.toString());
                    mcount=true;
                    Toast.makeText(MainActivity.this, "M+", Toast.LENGTH_SHORT).show();
                }

                break;
            case '-':
                if(comeout) {
                    sum -= num1;
                    mcount = true;
                }
                else  if (temp.length() > 0 && !num2sign) {
                    sum -= Double.parseDouble(temp.toString());
                    mcount = true;
                    Toast.makeText(MainActivity.this, "M-", Toast.LENGTH_SHORT).show();
                }
                break;
            case 'C':sum=0;
                /*text.setText("");
                text1.setText("");
                text2.setText("");*/
                mcount=false;
                Toast.makeText(MainActivity.this, "MC", Toast.LENGTH_SHORT).show();
                break;
            case 'R':
                if(mcount) {
                    if (comeout) {
                        num1 = sum;
                        text.setText(String.valueOf(num1));
                    } else {
                        Mnum=true;
                        int len = temp.length();
                        temp.delete(0, len);
                        if (sum==0)temp.append("0");
                        else temp.append(String.valueOf(sum));
                        text.setText(temp.toString());
                    }
                    Toast.makeText(MainActivity.this, "MR", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //开根号
    public void caulsqrt() {
        if(comeout) {
            if(num1<0)
            {
                Toast.makeText(MainActivity.this, "data shouldn't be negtive!", Toast.LENGTH_SHORT).show();

            }else {
                //num1 = Math.sqrt(num1);
                text.setText(String.valueOf(num1));
            }
        }else if(temp.length()>0){
            double current;
            current=Double.parseDouble(temp.toString());
            if(current>=0) {
                current=current*current;//current = Math.sqrt(current);
                int len = temp.length();
                temp.delete(0, len);
                temp.append(String.valueOf(current));
                text.setText(temp.toString());
            }
            else{
                Toast.makeText(MainActivity.this, "data shouldn't be negtive!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //初始化
    public void start() {
        num1 = 0;
        num2 = 0;
        num1alive=false;
        num2sign = false;
        decimal = false;
        num1is_ngt=false;
        num2is_ngt=false;
        comeout=false;
        op='+';
        int len = temp.length();
        temp.delete(0, len);//在temp中从第0个字符开始删除
    }
    //输入操作数
    public  void save(char num) {
        if(num2sign) {
            if(!comeout)
                num1 = Double.parseDouble(temp.toString());
            comeout=false;
            num1alive = true;
            text2.setText(String.valueOf(num1));
            text1.setText(String.valueOf(op));
            int len = temp.length();
            temp.delete(0, len);
            num2sign = false;
            decimal=false;
            if (num2is_ngt) temp.append('-');
            temp.append(num);
            text.setText(temp.toString());
        }else {
            if(num1is_ngt)
            {
                temp.append('-');
                num1is_ngt=false;
            }
            if(comeout){
                int len = temp.length();
                temp.delete(0, len);
                comeout=false;
            }
            if(Mnum){
                int len = temp.length();
                temp.delete(0, len);
                Mnum=false;temp.append(num);
                text.setText(temp.toString());
            }
            temp.append(num);
            text.setText(temp.toString());
        }
    }
    //输入运算符
    public void opchar(char a) {
        if(num1alive)
        {
            text2.setText("");
            result();
            num1alive=false;
        }
        int len=temp.length();
        if (len>0) {
            if (a == '-') {
                if (op == '*' || op == '/')
                    num2is_ngt = true;
                else {
                    op = a;
                    num2is_ngt=false;
                }
            } else {
                op = a;
                num2is_ngt = false;
            }
            if(comeout)text1.setText(String.valueOf(num1));
            else text1.setText(temp.toString());
            text.setText(String.valueOf(op));
            if(num2is_ngt)text.append("-");
            num2sign=true;
        }
        else{
            if(a=='-'){
                if(!num1is_ngt) {
                    num1is_ngt = true;
                    text.setText(String.valueOf("-"));
                }else {
                    num1is_ngt=false;
                    text.setText("");
                }
            }
            else {
                num1is_ngt = false;
                text.setText(String.valueOf(""));
            }
        }
    }
    //计算运算结果
    public void result() {
        if (num1alive) {
            num2 = Double.parseDouble(temp.toString());
            text1.setText(String.valueOf(op));
            text1.append(String.valueOf(num2));
            if (op == '/' && num2 == 0)
                Toast.makeText(MainActivity.this, "wrong operator!", Toast.LENGTH_SHORT).show();
            else {
                comeout=true;
                num1alive=false;
                decimal=false;
                switch (op) {
                    case '+':
                        num1 = num1 + num2;
                        break;
                    case '-':
                        num1 = num1 - num2;
                        break;
                    case '*':
                        num1 = num1 * num2;
                        break;
                    case '/':
                        num1 = num1 / num2;
                        break;
                }
                text.setText("=");
                text.append(String.valueOf(num1));
                op='+';
            }
        }
    }
}

