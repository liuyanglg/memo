package com.galaxy.memov2.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.galaxy.memov2.R;
import com.galaxy.memov2.asynctask.DaoAsyncTask;
import com.galaxy.memov2.utils.database.MemoDao;
import com.galaxy.memov2.entity.Record;
import com.galaxy.memov2.listener.BackPressListener;
import com.galaxy.memov2.ui.fragment.MainFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

import static com.galaxy.memov2.entity.OperateConstant._FIND_ALL;

public class MainActivity extends SupportActivity {
    /*日志过滤关键字*/
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.main_container, MainFragment.newInstance());
        }
        // 可以监听该Activity下的所有Fragment的18个 生命周期方法
        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentSupportVisible(SupportFragment fragment) {
                Log.i("MainActivity", "onFragmentSupportVisible--->" + fragment.getClass().getSimpleName());
            }
            @Override
            public void onFragmentCreated(SupportFragment fragment, Bundle savedInstanceState) {
                super.onFragmentCreated(fragment, savedInstanceState);
            }
            // 省略其余生命周期方法
        });
    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        //add  只能在activity中监听返回按键，加一个Listener在Fragment中监听
        Log.i("stack", "fragment stack: "+getSupportFragmentManager().getBackStackEntryCount());
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

}

