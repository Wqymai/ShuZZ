package com.wuqiyan.shuzz.widget;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.adapter.BooksListAdapter;
import com.wuqiyan.shuzz.comm.Constant;
import com.wuqiyan.shuzz.comm.SPUtils;
import com.wuqiyan.shuzz.model.BookModel;
import com.wuqiyan.shuzz.net.IturingImpl;
import com.wuqiyan.shuzz.net.OnLoadBookListener;
import com.wuqiyan.shuzz.net.OnLoadPagesListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuqiyan on 2017/6/19.
 */

public class RecycleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,OnLoadBookListener,OnLoadPagesListener{

    private RecyclerView mRecyclerView;
    private List<BookModel> mDatas=new ArrayList<>();
    private BooksListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private LinearLayoutManager mLayoutManager;
    IturingImpl ituring;
    private int firstPage = 0;
    private int currPage = 0;
    private int TotalPage;
    String type;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            type = getArguments().getString(Constant.BOOKTYPE);

            View  rootView = inflater.inflate(R.layout.fragment_section,container,false);
            mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.id_swipe_ly);
            mSwipeLayout.setColorSchemeResources(R.color.colorAccent);
            mSwipeLayout.setRefreshing(true);
            mSwipeLayout.setOnRefreshListener(this);


            ituring = new IturingImpl(getContext());
            checkFirstEnter();
            ituring.getIturingBook(type,currPage);
            ituring.setOnLoadBookListener(this);
            mAdapter = new BooksListAdapter(getContext(),1);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.addOnScrollListener(mOnScrollListener);

            return  rootView;

    }

    private RecyclerView.OnScrollListener mOnScrollListener=new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()){
                int newPage = currPage+1;
                if (newPage < TotalPage){
                    ituring.getIturingBook(type,newPage);
                    currPage++;
                }
                else {
                    mAdapter.setNoMoreData(true);
                    mAdapter.notifyDataSetChanged();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mAdapter.setHide(true);
                            mAdapter.notifyDataSetChanged();
                        }
                    },2000);
                }
            }
        }

    };



    @Override
    public void onSuccess(List<BookModel> books) {
        mSwipeLayout.setRefreshing(false);
        mDatas.addAll(books);
        mAdapter.setmBooksData(mDatas);
    }

    @Override
    public void onFailure(String error) {

    }

    @Override
    public void onRefresh() {
      ituring.getIturingBook(type,firstPage);
    }

    @Override
    public void onSuccess(int pages) {
        this.TotalPage = pages;
    }

    @Override
    public void onFailure() {

    }
    private void   checkFirstEnter(){
       String pages = new SPUtils(getContext(),"conf").getString(type,"-1");
       if (pages.equals("-1")){
           ituring.getIturingPages(type);
           ituring.setOnLoadPagesListener(this);
       }else {
           this.TotalPage = Integer.parseInt(pages);
       }
    }
}
