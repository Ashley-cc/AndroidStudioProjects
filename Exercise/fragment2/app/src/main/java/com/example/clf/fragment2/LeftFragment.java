package com.example.clf.fragment2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class LeftFragment extends Fragment {
    private RecyclerView m_wordsRecycleView;
    private List<Words> wordList;
    private WordsAdapter adapter;
    private boolean flagTwoPane;

    public void onAttach(Activity activity){
        super.onAttach(activity);
        wordList=getWords();
        adapter=new WordsAdapter((ArrayList<Words>)wordList);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){//加载left_fragment布局进来
        View view=inflater.inflate(R.layout.left_fragment,container,false);
        m_wordsRecycleView=(RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

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
        if(getActivity().findViewById(R.id.right_fragment)!=null){
             flagTwoPane=true;
        }else{
            flagTwoPane=false;
        }
    }

    private List<Words> getWords() {
        List<Words> wordsList = new ArrayList<Words>();
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
    }

    /*public void StartActivity(Context context,String wordsWord,String wordsMeaning,String wordsSample){
        Intent intent=new Intent(context,WordsContentActivity.class );
        intent.putExtra("word_word:",wordsMeaning);
        intent.putExtra("word_meaning:",wordsMeaning);
        intent.putExtra("word_sample:",wordsSample);
        context.startActivity(intent);
    }*/
}
