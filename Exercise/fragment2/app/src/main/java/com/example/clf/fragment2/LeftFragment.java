package com.example.clf.fragment2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeftFragment extends Fragment {
    private RecyclerView m_wordsRecycleView;
    private List<Words> wordList=new ArrayList<Words>();
    private WordsAdapter adapter;
    //private boolean flagTwoPane;
   WordsDBHelper mDbHelper;



    public void onAttach(Activity activity){
        super.onAttach(activity);
        mDbHelper = new WordsDBHelper(getActivity());
       /* MainActivity activity1 = (MainActivity ) getActivity();
        wordList=activity1.getWords();*/
       wordList=getWords();
        adapter=new WordsAdapter((ArrayList<Words>)wordList);
      /*  wordList=getWords();
        adapter=new WordsAdapter((ArrayList<Words>)wordList);*/
        //mDbHelper = new WordsDBHelper();
        //mDbHelper = new WordsDBHelper(this);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){//加载left_fragment布局进来
        View view=inflater.inflate(R.layout.left_fragment,container,false);
        m_wordsRecycleView=(RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
       // mDbHelper=new WordsDBHelper(view.getContext(), "Activity.mDbhelper", null, 1);
        m_wordsRecycleView.setLayoutManager(layoutManager);
        adapter.setmOnItemClickLitner(new WordsAdapter.OnItemClickLitner() {
            @Override
            public void onItemClick(View view, int position) {
                Words words=wordList.get(position);
              /*  if(flagTwoPane){*/
                    RightFragment wordsContentFragment=(RightFragment)getFragmentManager().findFragmentById(R.id.right_fragment);
                   wordsContentFragment.showWordContent(words.getWord(),words.getMeaning(),words.getSample());// wordsContentFragment.showWordContent("banana","香蕉","I love banana!");
                /*}else{
                    StartActivity(getActivity(),words.getWord(),words.getMeaning(),words.getSample());
                    System.out.print(words.getWord());
                }*/
            }
        });
        m_wordsRecycleView.setAdapter(adapter);
        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


    }

    /*public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.right_fragment)!=null){
             flagTwoPane=true;
        }else{
            flagTwoPane=false;
        }
    }*/

    /*private void addWords(){

    }*/

    /*private List<Words> getWords() {
        List<Words> wordsList = new ArrayList<Words>();
      *//*  private ArrayList<Map<String, String>> getAll(){
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String sql="select * from words";
            Cursor c=db.rawQuery(sql,null)*//*

        Words []word=new Words[1000];
        Words word1 = new Words();
        word1.setWord("apple");
        word1.setMeaning("苹果");
        word1.setSample("This is an apple!");
        wordsList.add(word1);
        Words word2 = new Words();
        word2.setWord("orange");
        word2.setMeaning("橙子");
        word2.setSample("This is an orange!");
        wordsList.add(word2);
        return wordsList;
    }*/

    private List<Words> getWords() {
        List<Words> wordList=new ArrayList<Words>();
        //mDbHelper=new WordsDBHelper(getActivity());

        SQLiteDatabase db =mDbHelper.getReadableDatabase();
        String sql="select * from words";
        Cursor c=db.rawQuery(sql,null);

        c.moveToFirst();

        //Words word1=new Words();

        wordList.add(new Words("banana","香蕉","i love banana!"));
        wordList.add(new Words("apple","苹果","Here is an Apple Tree!"));
        //db.execSQL("insert into words values(?,?,?)",new Object[]{w,m,s});


      /*  while(!c.isAfterLast()){*/while(c.moveToNext()){
            Words word=new Words();
            word.setWord(c.getString(0));
            word.setMeaning(c.getString(1));
            word.setSample(c.getString(2));
            wordList.add(word);
        }
        c.close();
        return wordList;
    }


    /*public void StartActivity(Context context,String wordsWord,String wordsMeaning,String wordsSample){
        Intent intent=new Intent(context,WordsContentActivity.class );
        intent.putExtra("word_word:",wordsMeaning);
        intent.putExtra("word_meaning:",wordsMeaning);
        intent.putExtra("word_sample:",wordsSample);
        context.startActivity(intent);
    }*/
}
