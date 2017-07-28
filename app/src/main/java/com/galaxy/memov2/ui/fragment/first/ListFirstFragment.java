package com.galaxy.memov2.ui.fragment.first;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.galaxy.memov2.R;
import com.galaxy.memov2.adapter.RecordAdapter;
import com.galaxy.memov2.asynctask.DaoAsyncTask;
import com.galaxy.memov2.base.BaseMainFragment;
import com.galaxy.memov2.entity.Record;
import com.galaxy.memov2.event.StartBrotherEvent;
import com.galaxy.memov2.event.TabSelectedEvent;
import com.galaxy.memov2.listener.RecyclerViewOnItemClickListener;
import com.galaxy.memov2.ui.fragment.MainFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.yokeyword.fragmentation.galaxy.OnBackPressListener;

import static com.galaxy.memov2.entity.OperateConstant._ADD;
import static com.galaxy.memov2.entity.OperateConstant._DELETE;
import static com.galaxy.memov2.entity.OperateConstant._FIND_UN_FINISH;
import static com.galaxy.memov2.entity.OperateConstant._MOVE;
import static com.galaxy.memov2.entity.OperateConstant._UPDATE;

/**
 * Created by liuya on 2017/4/18.
 */

public class ListFirstFragment extends BaseMainFragment implements SwipeRefreshLayout.OnRefreshListener {
    /*日志过滤关键字*/
    public final static String TAG = "ListSecondFragment";
    /*library*/
    private boolean mInAtTop = true;
    private int mScrollTotal;
    private RecordAdapter recordAdapter;
    /*view*/
    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    /*FloatingActionButton*/
    private FloatingActionMenu fabOperateMenu;
    private FloatingActionButton fabCheck;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabDelete;
    /*MaterialSearchView*/
    private MaterialSearchView searchView;
    /*保存数据库查询数据*/
    List<Record> initRecords = new ArrayList<>();
    /*编辑状态标志*/
    private boolean isEdit = false;//是否可编辑
    private boolean isSelectAll = false;//是否全选
    private HashSet<Record> positionSet = new HashSet<>();//记录选择项
    /*搜索保存原始数据*/
    private boolean isSearchSubmit = false;//点击搜索后搜素框会消失，按返回键恢复数据
    private List<Record> tempRecordList = new ArrayList<>();

    /*创建实例*/
    public static ListFirstFragment newInstance() {
        Bundle args = new Bundle();
        ListFirstFragment fragment = new ListFirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        //使用ActionBar时必须加入才能显示
        //setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_one, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        setEventListener();
    }

    private void initView(View view) {
        intiRecyclerView(view);
        initRefreshView(view);
        initToolbarView(view);
        initFabView(view);
        initSearchView(view);
    }

    private void setEventListener() {
        initToolbarEvent();
        initBackKeyPressEvent();
        initRecyclerViewEvent();
        initFabEvent();
        initSearchViewEvent();
    }

    /* 初始化数据*/
    private void initData() {
        new DaoAsyncTask(getActivity(), initRecords).execute(_FIND_UN_FINISH);
    }

    public void intiRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.f1_view_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.setHasFixedSize(true);
        final int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, space);
            }
        });
        recordAdapter = new RecordAdapter(_mActivity);
        recyclerView.setAdapter(recordAdapter);
        recordAdapter.setData(initRecords);
    }

    public void initRefreshView(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.f1_view_refresh);
        mRefreshLayout.setOnRefreshListener(this);
    }

    public void initToolbarView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.f1_toolbar);
        mToolbar.inflateMenu(R.menu.fm_one_menu_search);
        mToolbar.setTitle(R.string.app_title);
        //_mActivity.setSupportActionBar(mToolbar);
    }

    public void initFabView(View view) {
        fabOperateMenu = (FloatingActionMenu) view.findViewById(R.id.f1_fab_op_menu);
        fabAdd = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.f1_fab_add);
        fabDelete = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.f1_fab_delete);
        fabEdit = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.f1_fab_edit);
        fabCheck = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.f1_fab_check);
    }

    public void initSearchView(View view) {
        searchView = (MaterialSearchView) view.findViewById(R.id.f1_view_search);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
    }

    public void initToolbarEvent() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search) {
                    searchView.showSearch();
                }
                return false;
            }
        });
    }

    public void initRecyclerViewEvent() {
        /*library*/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mInAtTop = true;
                } else {
                    mInAtTop = false;
                }
            }
        });

        recordAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            //RecyclerViewOnClickListener
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                // 因为启动的MsgFragment是MainFragment的兄弟Fragment,所以需要MainFragment.start()
                // 这里我使用EventBus通知父MainFragment处理跳转(解耦),
                if (!isEdit) {
                    EventBus.getDefault().post(new StartBrotherEvent(DetailFirstFragment.newInstance(recordAdapter.getRecord(position))));
                }
            }

            //checkBox点击事件
            @Override
            public void checkBoxClick(int position) {
                isSelectAll = false;
                selectSingle(position);
            }
        });
    }

    public void initBackKeyPressEvent() {
        /*监听返回键*/
        _mActivity.setOnBackPressListener(new OnBackPressListener() {
            @Override
            public boolean fragmentBackPress() {
                Log.i("SupportActivity", "close search");
                if (searchView.isSearchOpen()) {
                    searchView.closeSearch();
                    return false;
                }
                return true;
            }
        });
    }

    public void initFabEvent() {
        //FloatingActionButton
        fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit == false) {
                    isEdit = true;
                    fabAdd.setEnabled(false);
                    openCheckBox();
                }
                if (!isSelectAll) {
                    selectAll();
                } else {
                    unSelectAll();
                }
            }
        });
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit == false) {
                    isEdit = true;
                    isSelectAll = false;
                    fabAdd.setEnabled(false);
                    openCheckBox();
                } else {
                    isEdit = false;
                    fabAdd.setEnabled(true);
                    closeCheckBox();
                    unSelectAll();
                }
            }
        });
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record record = new Record();
                EventBus.getDefault().post(new StartBrotherEvent(DetailFirstFragment.newInstance(record)));
            }
        });

        fabOperateMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (!opened) {
                    isSelectAll = false;
                    unSelectAll();
                    isEdit = false;
                    fabAdd.setEnabled(true);
                    closeCheckBox();
                }
            }
        });
    }

    public void initSearchViewEvent() {
        //MaterialSearchView
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, newText);
                searching(newText);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                fabOperateMenu.setVisibility(View.INVISIBLE);
                searchBegin();
            }

            @Override
            public void onSearchViewClosed() {
                fabOperateMenu.setVisibility(View.VISIBLE);
                searchEnd();
            }
        });
    }

    /**
     * 自定方法
     */
    /*关闭checkbox*/
    private void closeCheckBox() {
        recordAdapter.setShowCheckBox(false);
    }

    /*打开checkbox*/
    private void openCheckBox() {
        recordAdapter.setShowCheckBox(true);
    }

    /*删除*/
    void delete() {
        for (Record record : positionSet) {
            recordAdapter.deleteRecord(record);
        }
        recordAdapter.notifyDataSetChanged();
        positionSet.clear();
    }

    /*单选*/
    private void selectSingle(int position) {
        Record record = recordAdapter.getRecord(position);
        if (positionSet.contains(record)) {
            positionSet.remove(record);
        } else {
            positionSet.add(record);
        }
    }

    /*全选*/
    void selectAll() {
        isSelectAll = true;
        Record record;
        for (int i = 0; i < recordAdapter.getItemCount(); i++) {
            record = recordAdapter.getRecord(i);
            record.isSelect = true;
            if (!positionSet.contains(record))
                positionSet.add(record);
        }
        recordAdapter.notifyDataSetChanged();
    }

    /*清空*/
    void unSelectAll() {
        isSelectAll = false;
        Record record;
        for (int i = 0; i < recordAdapter.getItemCount(); i++) {
            record = recordAdapter.getRecord(i);
            record.isSelect = false;
            if (positionSet.contains(record)) {
                positionSet.remove(record);
            }
        }
        recordAdapter.notifyDataSetChanged();
    }

    /**
     * 事件相关
     */
    //下拉刷新事件
    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh");
        initRecords.clear();
        recordAdapter.setData(initRecords);
        recordAdapter.notifyDataSetChanged();
        new DaoAsyncTask(getActivity(),initRecords).execute(_FIND_UN_FINISH);
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                recordAdapter.setData(initRecords);
                recordAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    /**
     * EventBus
     */
    /*接收新建的Record*/
    @Subscribe
    public void receiveRecord(Record record) {
        Log.i("f2", "receive a massage");
        Log.i("f2", "op status is " + record.opStatus);

            if (record.opStatus == _ADD) {
                recordAdapter.addRecord(record, 0);
                scrollToTop();
            } else if (record.opStatus == _UPDATE) {
                recordAdapter.updateRecord(record);
            } else if (record.opStatus == _DELETE) {
                recordAdapter.deleteRecord(record);
            }else if(record.opStatus==_MOVE){
                recordAdapter.moveRecord(record);
            }

    }

    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FIRST) return;
        if (mInAtTop) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
        } else {
            scrollToTop();
        }
    }

    private void scrollToTop() {
        Log.i("f2", "scroll to top");
        recyclerView.smoothScrollToPosition(0);
    }


    /**
     * Fragment生命周期
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }

    private void searchBegin() {
//        Log.i(TAG, "getItemCount 0: " + recordAdapter.getItemCount());
        tempRecordList.clear();
        tempRecordList.addAll(recordAdapter.getAllItem());
//        Log.i(TAG, "tempRecordList size(): " + tempRecordList.size());
    }

    private void searching(String query) {
        /*SearchView的Shown 和 QueryChange执行顺序有问题，导致List size=0*/
        if (tempRecordList.size() > 0)
            recordAdapter.setData(tempRecordList);
        recordAdapter.getFilter().filter(query);
    }

    private void searchEnd() {
        recordAdapter.setData(tempRecordList);
        tempRecordList.clear();
    }

}
