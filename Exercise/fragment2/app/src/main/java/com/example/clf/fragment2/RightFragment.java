package com.example.clf.fragment2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RightFragment extends Fragment {
    private View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.right_fragment,container,false);
        return view;
    }
    public void showWordContent(String newsWord,String newsMeaning,String newsSample){
        //View visibilityLayout=view.findViewById(R.id.visibility_layout);
        //visibilityLayout.setVisibility(View.VISIBLE);
        TextView wordsWordText=(TextView)view.findViewById(R.id.words_word);
        TextView wordsMeaningText=(TextView)view.findViewById(R.id.words_meaning);
        TextView wordsSampleText=(TextView)view.findViewById(R.id.words_sample);
        wordsWordText.setText(newsWord);
        wordsMeaningText.setText(newsMeaning);
        wordsSampleText.setText(newsSample);
    }
}
