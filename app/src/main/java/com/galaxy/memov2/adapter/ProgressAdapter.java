package com.galaxy.memov2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.galaxy.memov2.R;
import com.galaxy.memov2.asynctask.DaoAsyncTask;
import com.galaxy.memov2.entity.Progress;
import com.galaxy.memov2.entity.Record;
import com.galaxy.memov2.listener.RecyclerViewOnItemClickListener;
import com.galaxy.memov2.utils.date.DateUtils;

import java.util.ArrayList;
import java.util.List;

import static com.galaxy.memov2.entity.OperateConstant._ADD;
import static com.galaxy.memov2.entity.OperateConstant._DELETE;
import static com.galaxy.memov2.entity.OperateConstant._UPDATE;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class ProgressAdapter extends RecyclerView.Adapter<ProgressHolder>  {
    /*日志过滤关键字*/
    public final static String TAG = "ProgressAdapter";
    /*interface */
    private RecyclerViewOnItemClickListener mClickListener;
    /*状态标志*/
    private boolean showCheckBox = false;//控制选择框是否显示
    /*基本变量*/
    private Context context;
    private LayoutInflater inflater;
    private List<Progress> progresses = new ArrayList<>();


    public ProgressAdapter() {
    }

    public ProgressAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public static ProgressAdapter getInstance() {
        ProgressAdapter recordAdapter = null;
        if (recordAdapter == null) {
            recordAdapter = new ProgressAdapter();
        }
        return recordAdapter;
    }

    @Override
    public ProgressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dilog_progress_item, parent, false);
        final ProgressHolder holder = new ProgressHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProgressHolder holder, final int position) {
        final Progress item = progresses.get(position);
        holder.tvProgress.setText(item.progressName);
        if(item.isSelect==false){
            holder.imgFlag.setVisibility(View.INVISIBLE);
        }else {
            holder.imgFlag.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(holder.getAdapterPosition(), v, holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return progresses.size();
    }
    /*Item适配数据*/
    public void setData(List<Progress> beans) {
        progresses.clear();
        progresses.addAll(beans);
        notifyDataSetChanged();
    }
    /**
     * 自定义方法
     */
    /*是否显示CheckBox*/
    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
        notifyDataSetChanged();
    }

    /**
     * 相关get set 方法
     */

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        mClickListener = listener;
    }
}
