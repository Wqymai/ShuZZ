package com.wuqiyan.shuzz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.model.BookModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuqiyan on 17/6/22.
 */

public class BooksListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<BookModel> mBooksData=new ArrayList<>();
    private Context mContext;
    private boolean mShowFooter = true;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public BooksListAdapter(Context context){
        this.mContext=context;
    }

    public void setmBooksData(List<BookModel> data){
        this.mBooksData = data;
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {

        if (position+1 == getItemCount()){
            return TYPE_FOOTER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
            ItemHolder holder = new ItemHolder(v);
            return holder;
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_footer, parent, false);
            FootHolder holder = new FootHolder(v);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder){
            ((ItemHolder) holder).tv.setText(mBooksData.get(position).bookName);
        }

    }

    @Override
    public int getItemCount() {
        if (mBooksData.size() <= 0){
          return 0;
        }
        else {
          return mBooksData.size()+1;
        }
    }
    class ItemHolder extends RecyclerView.ViewHolder
    {

        TextView tv;

        public ItemHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.item_desc);
        }
    }
    class FootHolder extends RecyclerView.ViewHolder{

        public FootHolder(View itemView) {
            super(itemView);
        }
    }
}
