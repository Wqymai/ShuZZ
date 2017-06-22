package com.wuqiyan.shuzz.widget;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuqiyan.shuzz.Adapter.BooksListAdapter;
import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.model.BookModel;
import com.wuqiyan.shuzz.net.IturingImpl;
import com.wuqiyan.shuzz.net.OnLoadBookListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuqiyan on 2017/6/19.
 */

public class RecycleFragment extends Fragment implements OnLoadBookListener{

    private RecyclerView mRecyclerView;
    private List<BookModel> mDatas=new ArrayList<>();
    private BooksListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private LinearLayoutManager mLayoutManager;
    private static final int REFRESH_COMPLETE = 0X110;
    IturingImpl ituring;
    private int currPage = 0;
    private int TotalPage = 2;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    Log.i("TAG","刷新成功...");
                    mSwipeLayout.setRefreshing(false);
                    break;

            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View  rootView = inflater.inflate(R.layout.fragment_section,container,false);
            mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.id_swipe_ly);
            mSwipeLayout.setColorSchemeResources(R.color.colorAccent);
            mSwipeLayout.setRefreshing(true);


            mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i("TAG","正在刷新...");
                    mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
                }
            });

            ituring = new IturingImpl(getContext());
            ituring.getAndroid_Ituring(currPage);
            ituring.setOnLoadBookListener(this);
            mAdapter = new BooksListAdapter(getContext());

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
                    ituring.getAndroid_Ituring(newPage);
                    currPage++;
                }
                else {

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

}
