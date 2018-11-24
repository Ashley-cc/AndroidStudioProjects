package com.example.clf.fragment2;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder>{

    private ArrayList<Words>mDataset;
    private int itemCounter=1;
    public WordsAdapter(ArrayList<Words> dataset){
        mDataset = new ArrayList<Words>(dataset);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(),android.R.layout.simple_list_item_1,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
         viewHolder.mTextView.setText(mDataset.get(position).getWord());
         if(mOnItemClickLitner!=null) {
             viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mOnItemClickLitner.onItemClick(viewHolder.itemView, position);
                 }
             });
         }
    }



    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View itemView){
            super(itemView);
            mTextView=(TextView)itemView;
        }
    }

    public interface OnItemClickLitner{
        void onItemClick(View view,int position);
    }
    private OnItemClickLitner mOnItemClickLitner;
    public void setmOnItemClickLitner(OnItemClickLitner mOnItemClickLitner){
        this.mOnItemClickLitner=mOnItemClickLitner;
    }
}
