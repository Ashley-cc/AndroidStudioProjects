package com.example.clf.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    WordsDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new WordsDBHelper(this);
     /*   String sql = "insert into  words(word,meaning,sample) values(?,?,?)";

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql, new String[]{"orange", "橙子", "I love orange!"});*/
       /*  String sql="delete  from words where word='grape'";
         SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);*/
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //String sql1="insert into  words(word,meaning,sample) values(?,?,?)";
        //Gets the data repository in write mode*/

        /*String w="banana";
        String m="香蕉";
        String s="I LOVE banana!";
        db.execSQL(sql1,new String[]{w,m,s});*/
      /*  Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        replaceFragment(new RightFragment());
        RightFragment rightfragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.right_fragment);*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                SearchDialog();
                return true;
            case R.id.action_insert:
                InsertDialog();
                // LeftFragment leftFragment=(LeftFragment) getSupportFragmentManager().findFragmentById(R.id.left_fragment);
                replaceFragment(new LeftFragment());
                //LeftFragment leftfragment=(LeftFragment)getSupportFragmentManager().findFragmentById(R.id.left_fragment);

                Toast.makeText(this, "you clicked the insert menu", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //使用Sql语句插入单词
    private void InsertUserSql(String strWord, String strMeaning, String strSample) {
        String sql = "insert into  words(word,meaning,sample) values(?,?,?)";
        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample});
    }

    //新增对话框
    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert_layout, null);
        new AlertDialog.Builder(this)
                .setTitle("新增单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        //既可以使用Sql语句插入，也可以使用使用insert方法插入
                        InsertUserSql(strWord, strMeaning, strSample);
                       // LeftFragment leftFragment=new LeftFragment();
                        //leftFragment.add
                        //ArrayList<Map<String, String>> items = getAll();
                        //setWordsListView(items);
                    }
                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()//创建对话框
                .show();//显示对话框
    }

    //使用Sql语句查找
    private  Cursor SearchUseSql(String strWordSearch) {
        /*List<Words> swordList=new ArrayList<Words>();*/
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="select * from words where word like ? order by word desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});
       /* c.moveToFirst();
        Words word=new Words();
        if(c.moveToNext()) {*/
           /* Words word = new Words();*/
          /*  word.setWord(c.getString(0));
            word.setMeaning(c.getString(1));
            word.setSample(c.getString(2));*/
            /*swordList.add(word);*/

       /* }*/
        /*if(swordList.isEmpty()){
            Toast.makeText(this, "查找不到该单词！", Toast.LENGTH_LONG).show();
        }*/

        return c;

    }


   //查询对话框
    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.search, null);
        new AlertDialog.Builder(this)
                .setTitle("查找单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord = ((EditText) tableLayout.findViewById(R.id.txtSearchWord)).getText().toString();
                        //ArrayList<Map<String, String>> items=null;
                        //items=SearchUseSql(txtSearchWord);
                        /* List<Words> swordList=new ArrayList<Words>();*/
                        //Words word=new Words();
                        //Words word=SearchUseSql(txtSearchWord);
                        Cursor c = SearchUseSql(txtSearchWord);
                        c.moveToFirst();
                        if(c.getCount()>0){
                            RightFragment wordsContentFragment = (RightFragment) getSupportFragmentManager().findFragmentById(R.id.right_fragment);
                            wordsContentFragment.showWordContent(c.getString(0), c.getString(1), c.getString(2));
                           /* Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);*/
                            // Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                            //intent.putExtras(bundle);
                            //startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"没有找到", Toast.LENGTH_LONG).show();
                        }
                    }
                })

                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .create()//创建对话框
                .show();//显示对话框

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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.left_fragment, fragment);
        transaction.addToBackStack(null);//返回上一个碎片
        transaction.commit();
    }

    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

    /*private List<Words> getWords() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="select * from words";
        Cursor c=db.rawQuery(sql,null);
        List<Words> words=null;
        Words word=new Words();
        while(c.moveToNext()){
            word.setWord(c.getString(0));
            word.setMeaning(c.getString(1));
            word.setSample(c.getString(2));
            words.add(word);
        }
        return words;
    }*/


   /* public List<Words> getWords() {
        List<Words> wordList=new ArrayList<Words>();
        //mDbHelper=new WordsDBHelper(getActivity());

        SQLiteDatabase db =mDbHelper.getReadableDatabase();
        String sql="select * from words";
        Cursor c=db.rawQuery(sql,null);

        c.moveToFirst();
        Words word=new Words();
        //Words word1=new Words();

        wordList.add(new Words("banana","香蕉","i love banana!"));
        //db.execSQL("insert into words values(?,?,?)",new Object[]{w,m,s});


        while(!c.isAfterLast()){
            word.setWord(c.getString(0));
            word.setMeaning(c.getString(1));
            word.setSample(c.getString(2));
            wordList.add(word);
        }
        return wordList;
    }*/

   /* public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){//长按菜单将调出
        getMenuInflater().inflate(R.menu.contextmenu,menu);
    }*/
}


