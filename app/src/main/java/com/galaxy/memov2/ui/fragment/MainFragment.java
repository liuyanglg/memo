package com.galaxy.memov2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.galaxy.memov2.R;
import com.galaxy.memov2.base.BaseFragment;
import com.galaxy.memov2.event.StartBrotherEvent;
import com.galaxy.memov2.event.TabSelectedEvent;
import com.galaxy.memov2.ui.fragment.first.ListFirstFragment;
import com.galaxy.memov2.ui.fragment.second.ListSecondFragment;
import com.galaxy.memov2.ui.view.BottomBar;
import com.galaxy.memov2.ui.view.BottomBarTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by liuya on 2017/4/17.
 */

public class MainFragment extends BaseFragment {
    private static final int REQ_MSG = 10;
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    private SupportFragment[] mFragments = new SupportFragment[2];
    private BottomBar mBottomBar;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState == null) {
            mFragments[FIRST] = ListFirstFragment.newInstance();
            mFragments[SECOND] = ListSecondFragment.newInstance();
            loadMultipleRootFragment(R.id.fragment_main_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findChildFragment(ListFirstFragment.class);
            mFragments[SECOND] = findChildFragment(ListSecondFragment.class);
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);
        mBottomBar = (BottomBar) view.findViewById(R.id.f1_bottomBar);
        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_alarm_on_01, "待办"))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_flower, "完成"));
        // 模拟未读消息
        mBottomBar.getItem(FIRST).setUnreadCount(0);
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
                BottomBarTab tab = mBottomBar.getItem(FIRST);
                if (position == FIRST) {
                    tab.setUnreadCount(0);
                } else {
                    tab.setUnreadCount(0);
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {
        }
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
