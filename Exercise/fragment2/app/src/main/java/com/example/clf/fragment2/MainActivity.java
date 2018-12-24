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
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private RecyclerView m_wordsRecycleView;
    private List<Words> wordList=new ArrayList<Words>();
    private WordsAdapter adapter;
    //private boolean flagTwoPane;
    public String searchWord;
    public View view;
    private boolean find=true;

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
        //show();
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                SearchDialog();
                return true;
            case R.id.action_insert:
                //show();
                InsertDialog();
                // LeftFragment leftFragment=(LeftFragment) getSupportFragmentManager().findFragmentById(R.id.left_fragment);
                //replaceFragment(new LeftFragment());
               //show();
                /*leftfragment.
                leftfragment.showWords();*/
               // mDbHelper = new WordsDBHelper(this);
                //replaceFragment(new LeftFragment());
                //leftfragment.onAttach(this);
                Toast.makeText(this, "you clicked the insert menu", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_delete:
                DeleteDialog();
                //show();
                return true;
            case R.id.action_modify:
                UpdateDialog();
                //show();
                return true;
            case R.id.Search_online:
                Intent intent = new Intent(this, WebSearch.class);
                //intent.putExtra("searchWord",searchWord);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void show(){

        LeftFragment leftfragment=(LeftFragment)getSupportFragmentManager().findFragmentById(R.id.left_fragment);
        m_wordsRecycleView=(RecyclerView)leftfragment.view.findViewById(R.id.recyclerView);
        wordList=getWords();
        adapter=new WordsAdapter((ArrayList<Words>)wordList);
        adapter.setmOnItemClickLitner(new WordsAdapter.OnItemClickLitner() {
            @Override
            public void onItemClick(View view, int position) {
                Words words=wordList.get(position);

                RightFragment wordsContentFragment=(RightFragment)getSupportFragmentManager().findFragmentById(R.id.right_fragment);
                wordsContentFragment.showWordContent(words.getWord(),words.getMeaning(),words.getSample());// wordsContentFragment.showWordContent("banana","香蕉","I love banana!");

            }
        });

        m_wordsRecycleView.setAdapter(adapter);
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
                        Cursor c = SearchUseSql(strWord);
                        c.moveToFirst();
                        if(c.getCount()>0) {
                            Toast.makeText(MainActivity.this,"单词本中已存在该词，无法重复添加！", Toast.LENGTH_LONG).show();
                        }else {
                            //既可以使用Sql语句插入，也可以使用使用insert方法插入
                            InsertUserSql(strWord, strMeaning, strSample);
                            Toast.makeText(MainActivity.this,"新单词添加成功！", Toast.LENGTH_LONG).show();
                            show();
                        }
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
    //精准查找,删除修改时确定单词中是否存在该词
    private  Cursor Search(String strWordSearch) {
        /*List<Words> swordList=new ArrayList<Words>();*/

       /* SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from words where word ='" + strWordSearch +"'";
        Cursor c = db.rawQuery(sql);*/
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="select * from words where word = '"+strWordSearch+"' order by word desc";
        Cursor c=db.rawQuery(sql,null/*new String[]{"'"+strWordSearch+"'"}*/);
        return c;
    }


   //查询对话框
    private void SearchDialog() {
         //boolean find=true;
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.search, null);
        new AlertDialog.Builder(this)
                .setTitle("查找单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord = ((EditText) tableLayout.findViewById(R.id.txtSearchWord)).getText().toString();
                        searchWord=txtSearchWord;
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
                            find=false;
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
        if(!find) {
            Intent intent = new Intent(this, WebSearch.class);
            intent.putExtra("searchWord",searchWord);
            startActivity(intent);
        }

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


    private void DeleteUseSql(String strId) {
        String sql="delete from words where word='"+strId+"'";
        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);
    }

    //删除对话框
    private void DeleteDialog(/*final String strId*/) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.delete, null);
        new AlertDialog.Builder(this)
                .setTitle("删除单词")
                .setView(tableLayout)
                //.setMessage("确定要删除该单词吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtDeleteWord = ((EditText) tableLayout.findViewById(R.id.txtDeleteWord)).getText().toString();
                        Cursor c = Search(txtDeleteWord);
                        c.moveToFirst();
                        if(c.getCount()>0) {
                            DeleteUseSql(txtDeleteWord);
                            Toast.makeText(MainActivity.this,"删除成功！", Toast.LENGTH_LONG).show();
                            show();
                        }
                        else{
                            Toast.makeText(MainActivity.this,"单词本中无该词，无法删除！", Toast.LENGTH_LONG).show();

                        }
                        //setWordsListView(getAll());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()
                .show();
    }



    //使用Sql语句更新单词
    private void UpdateUseSql(String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="update words set word=?,meaning=?,sample=? where word='"+strWord+"'";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample});
    }

    //修改对话框
    private void UpdateDialog(/*final String strId, final String strWord, final String strMeaning, final String strSample*/) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.modify, null);
       /* ((EditText)tableLayout.findViewById(R.id.txtWord)).setText(strWord);
        ((EditText)tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText)tableLayout.findViewById(R.id.txtSample)).setText(strSample);*/
        new AlertDialog.Builder(this)
                .setTitle("修改单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        Cursor c = Search(strNewWord);
                        c.moveToFirst();
                        if(c.getCount()>0) {
                            UpdateUseSql(strNewWord, strNewMeaning, strNewSample);
                            Toast.makeText(MainActivity.this,"修改成功！", Toast.LENGTH_LONG).show();
                            show();
                        }else{
                            Toast.makeText(MainActivity.this,"单词本中不存在该词，请先添加才能进行修改！", Toast.LENGTH_LONG).show();
                        }
                        //setWordsListView(getAll());
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

    private List<Words> getWords() {
       /* mDbHelper.close();*/

        mDbHelper = new WordsDBHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="select * from words";
        Cursor c=db.rawQuery(sql,null);
        List<Words> words=new ArrayList<Words>();
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){/* while(c.moveToNext()){*/
            Words word=new Words();
            word.setWord(c.getString(0));
            word.setMeaning(c.getString(1));
            word.setSample(c.getString(2));
            words.add(word);
            c.moveToNext();
        }
        return words;
    }


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



