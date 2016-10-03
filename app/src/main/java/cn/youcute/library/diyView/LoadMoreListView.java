package cn.youcute.library.diyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import cn.youcute.library.R;

/**
 * 上拉加载更多的listView,配合Google官方的下拉刷新使用
 * Created by chengGuo on 2016/3/21.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "RefreshListView";
    /**
     * 底部加载更多部分
     */
    private boolean isScrollToBottom;//判断是不是滑到了底部
    private View footerView; //底部的footer   view
    private int footerViewHeight; //底部view的高度
    private boolean isLoadingMore = false; //判断是不是"加载更多"
    private TextView tvInfo; //底部提示信息

    /**
     * listView的接口，监听listView的下来刷新和上拉加载更多
     */
    private OnRefreshListener mOnRefreshListener;


    public LoadMoreListView(Context context) {
        super(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView();
        this.setOnScrollListener(this);
    }


    /**
     * 初始化底部view
     */
    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.list_foot_view, null);
        tvInfo = (TextView) footerView.findViewById(R.id.tv_foot_info);
        //设置（0，0）以便系统测量footerView的宽高
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        this.addFooterView(footerView);
    }

    /**
     * 监听ListView滚动的状态变化，如果滑到了底部，就“加载更多..."
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            if (isScrollToBottom && !isLoadingMore) {
                isLoadingMore = true;
                footerView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());

                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onLoadingMore();
                }
                tvInfo.setText("正在加载更多");
            }
        }
    }

    /**
     * 监听listView滚动的状态变化，判断当前是不是滑到了底部
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isScrollToBottom = getLastVisiblePosition() == (totalItemCount - 1);
    }

    /**
     * 设置监听接口，当为
     *
     * @param listener 监听接口
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }


    /**
     * 为外界提供的方法，当Activity中的加载更多数据加载完后，就调用这个方法来隐藏底部的footerView
     */
    public void loadMoreComplete() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }

    /**
     * 没有更多加载
     */
    public void noMoreLoad() {
        tvInfo.setText("已加载全部");
        loadMoreComplete();
    }

    /**
     * 设置接口，供外界实现，监听listView的刷新和加载更多的状态
     */
    public interface OnRefreshListener {
        /**
         * 上拉加载更多
         */
        void onLoadingMore();
    }
}
