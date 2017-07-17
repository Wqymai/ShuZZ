package com.wuqiyan.shuzz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.adapter.BooksListAdapter;
import com.wuqiyan.shuzz.model.BookModel;
import com.wuqiyan.shuzz.net.BookAskImpl;
import com.wuqiyan.shuzz.net.OnLoadBookListener;
import com.wuqiyan.shuzz.widget.DetailActivity;
import com.wuqiyan.shuzz.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuqiyan on 2017/6/19.
 */

public class BookFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,OnLoadBookListener{

    private RecyclerView mRecyclerView;
    private List<BookModel> mDatas=new ArrayList<>();
    private BooksListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton backTop;
    private BookAskImpl bookAskImpl;
    private int currPage = 0;
    private int firstPage = 1;
    private String kw;
    private boolean hasNext = true;
    private LinearLayout llLoadFail;




    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


            currPage = firstPage;
            kw = getArguments().getString("kw");

            View  rootView = inflater.inflate(R.layout.fragment_section,container,false);
            mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.id_swipe_ly);
            mSwipeLayout.setColorSchemeResources(R.color.colorAccent);
            mSwipeLayout.setRefreshing(true);
            mSwipeLayout.setOnRefreshListener(this);

            mAdapter = new BooksListAdapter(getContext(),1);
            mAdapter.setOnItemClickListener(new BooksListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, BookModel bookModel) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("bookInfo",bookModel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.addOnScrollListener(mOnScrollListener);

            backTop = (FloatingActionButton) rootView.findViewById(R.id.backTop_fab);
            backTop.hide();
            backTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerView.scrollToPosition(0);
                    backTop.hide();
                }
            });

            //点击加载失败重新加载
            llLoadFail = (LinearLayout) rootView.findViewById(R.id.ll_loadFail);
            llLoadFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeLayout.setRefreshing(true);
                    llLoadFail.setVisibility(View.GONE);
                    onRefresh();

                }
            });
            return  rootView;

    }

    private RecyclerView.OnScrollListener mOnScrollListener=new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;
        private int firstVisibleItem;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
            if (dy < 0 ){//向下滑
                if (firstVisibleItem == 0){
                    backTop.hide();
                }
                else {
                    backTop.show();
                }
            }
            if (dy > 0){//向上滑
                backTop.hide();
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()){
                if (hasNext){

                  bookAskImpl.requestBookAskInfo(kw,currPage);
                }
                else {
                    Handler handler = new Handler();
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
        mAdapter.setNoMoreData(false);
        mAdapter.setLoadFail(false);
        mSwipeLayout.setRefreshing(false);
        mDatas.addAll(books);
        mAdapter.setmBooksData(mDatas);
    }

    @Override
    public void onFailure(String error) {

          if (mAdapter.getItemCount() <= 0){

              mSwipeLayout.setRefreshing(false);
              llLoadFail.setVisibility(View.VISIBLE);

          }
          else {
              mAdapter.setLoadFail(true);
              mAdapter.notifyDataSetChanged();
          }
    }

    @Override
    public void onPageNext(boolean hasNext) {
        this.hasNext = hasNext;
        if (hasNext){
            currPage = currPage + 1;
        }
        else {
            mAdapter.setNoMoreData(true);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onRefresh() {
        bookAskImpl.requestBookAskInfo(kw,firstPage);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {
        bookAskImpl = new BookAskImpl();
        bookAskImpl.requestBookAskInfo(kw,firstPage);
        bookAskImpl.setOnLoadBookListener(this);
    }
}
