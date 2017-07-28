package com.galaxy.memov2.adapter;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.galaxy.memov2.R;
import com.galaxy.memov2.asynctask.DaoAsyncTask;
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
public class RecordAdapter extends RecyclerView.Adapter<Holder> implements Filterable {
    /*日志过滤关键字*/
    public final static String TAG = "RecordAdapter";
    /*interface */
    private RecyclerViewOnItemClickListener mClickListener;
    /*状态标志*/
    private boolean showCheckBox = false;//控制选择框是否显示
    /*基本变量*/
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Record> mItems = new ArrayList<>();
    /*过滤器*/
    private TextFilter textFilter;
    private List<Record> tempItems = new ArrayList<>();//保存过滤前的list
    /*数据库操作异步任务*/

    public RecordAdapter() {
    }

    public RecordAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public static RecordAdapter getInstance() {
        RecordAdapter recordAdapter = null;
        if (recordAdapter == null) {
            recordAdapter = new RecordAdapter();
        }
        return recordAdapter;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fm_one_item, parent, false);
        final Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final Record item = mItems.get(position);
        holder.tvDetail.setText(item.detail);
        holder.tvCreateDate.setText(DateUtils.DateToStr(item.createDate));
        if(item.alarmDate!=null&&item.alarmDate.getTime()> System.currentTimeMillis()) {
            holder.tvAlarmDate.setText(DateUtils.DateToStr(item.alarmDate));
        }else {
            holder.tvAlarmDate.setText("已过期");
        }
        if(item.alarmDate==null){
            holder.tvAlarmDate.setVisibility(View.INVISIBLE);
            holder.imgAlarm.setVisibility(View.INVISIBLE);
        }else {
            holder.tvAlarmDate.setVisibility(View.VISIBLE);
            holder.imgAlarm.setVisibility(View.VISIBLE);
        }
        if (showCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.checkBox.setChecked(item.isSelect);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    item.isSelect = holder.checkBox.isChecked();
                    mClickListener.checkBoxClick(position);
                }
            }
        });

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
        return mItems.size();
    }

    /*Item适配数据*/
    public void setData(List<Record> beans) {
        mItems.clear();
        mItems.addAll(beans);
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

    /*增删改查*/
    public void addRecord(Record record, int position) {
        mItems.add(position, record);
        record.isSelect = false;
        new DaoAsyncTask(mContext, record).execute(_ADD);
        notifyItemInserted(position);
    }

    public void deleteRecord(Record record) {
        int pos = 0;
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).id == record.id)
                pos = i;
        }
        mItems.remove(record);
        new DaoAsyncTask(mContext, record).execute(_DELETE);
        notifyItemRemoved(pos);
    }

    public void updateRecord(Record record) {
        new DaoAsyncTask(mContext, record).execute(_UPDATE);
        notifyDataSetChanged();
    }

    public void moveRecord(Record record) {
        updateRecord(record);
        int pos = 0;
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).id == record.id)
                pos = i;
        }
        mItems.remove(record);
        notifyItemRemoved(pos);
    }

    /**
     * 相关get set 方法
     */
    public Record getRecord(int position) {
        return mItems.get(position);
    }

    public List<Record> getAllItem() {
        return mItems;
    }


    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        mClickListener = listener;
    }

    /*library方法*/
    public void refreshRecord(Record bean) {
        int index = mItems.indexOf(bean);
        if (index < 0) return;
        notifyItemChanged(index);
    }

    /*过滤器*/
    public class TextFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence query) {
            Log.i("ListFirst", "query is " + query);
            List<Record> foundRecordList = new ArrayList<>();
            if (query != null && query.toString().trim().length() > 0) {
                for (int i = 0; i < mItems.size(); i++) {
                    Record record = mItems.get(i);
                    String text = record.detail;
                    if (text.contains(query)) {
                        foundRecordList.add(record);
                    }
                }
            } else {
                foundRecordList = mItems;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.count = foundRecordList.size();
            filterResults.values = foundRecordList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mItems = (List<Record>) results.values;
//            if (results.count > 0) {
            notifyDataSetChanged();
//            }
        }
    }

    @Override
    public Filter getFilter() {
        if (textFilter == null) {
            textFilter = new TextFilter();
        }
        return textFilter;
    }
}
