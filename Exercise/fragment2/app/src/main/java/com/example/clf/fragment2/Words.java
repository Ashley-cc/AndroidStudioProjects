package com.example.clf.fragment2;

import android.provider.BaseColumns;

public class Words {
    private String word;
    private String meaning;
    private String sample;

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getWord() { return word; }

    public String getMeaning() {
        return meaning;
    }

    public String getSample() {
        return sample;
    }


    public Words(){}
    public Words(String word,String meaning,String sample){
        this.word=word;
        this.meaning=meaning;
        this.sample=sample;
    }
    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME="words";
        public static final String COLUMN_NAME_WORD="word";//列：单词
        public static final String COLUMN_NAME_MEANING="meaning";//列：单词含义
        public static final String COLUMN_NAME_SAMPLE="sample";//单词示例
    }
}
