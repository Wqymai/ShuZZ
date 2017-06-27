package com.wuqiyan.shuzz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
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
    private boolean noMoreData = false;
    private boolean hide = false;
    private int type;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_NOMORE_DATA = 2;

    public BooksListAdapter(Context context,int type){
        this.mContext = context;
        this.type = type;
    }

    public void setmBooksData(List<BookModel> data){
        this.mBooksData = data;
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (position+1 == getItemCount() && !hide){
            if (noMoreData){
                return TYPE_NOMORE_DATA;
            }
            return TYPE_FOOTER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookslist_item, parent, false);
            ItemHolder holder = new ItemHolder(v);
            return holder;
        }
        else if (viewType == TYPE_FOOTER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_footer, parent, false);
            FootHolder holder = new FootHolder(v);
            return holder;
        }
        else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_nomore_data, parent, false);
            NODataHolder holder = new NODataHolder(v);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder){
            ((ItemHolder) holder).bkname.setText(mBooksData.get(position).bookName);
            ((ItemHolder) holder).desc.setText(mBooksData.get(position).desc);


            Picasso.with(mContext).load(mBooksData.get(position).bookImgUrl)
                    .resize(dip2px(80), dip2px(110))
                    .centerCrop()
                    .into(((ItemHolder) holder).img);
        }

    }


    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public void setNoMoreData(boolean noMoreData){
        this.noMoreData = noMoreData;
    }
    public void setHide(boolean hide){
        this.hide = hide;
    }

    @Override
    public int getItemCount() {

        if (mBooksData.size() <= 0){
          return 0;
        }
        else {
            if (hide){
                return mBooksData.size();
            }
            return mBooksData.size()+1;
        }
    }
    class ItemHolder extends RecyclerView.ViewHolder
    {

        TextView bkname;
        TextView desc;
        ImageView img;

        public ItemHolder(View view)
        {
            super(view);
            desc = (TextView) view.findViewById(R.id.item_desc);
            bkname= (TextView) view.findViewById(R.id.item_bkname);
            img= (ImageView) view.findViewById(R.id.item_image);
        }
    }
    class FootHolder extends RecyclerView.ViewHolder{

        public FootHolder(View itemView) {
            super(itemView);
        }
    }
    class NODataHolder extends RecyclerView.ViewHolder{

        public NODataHolder(View itemView) {
            super(itemView);
        }
    }
}
