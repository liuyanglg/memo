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
public class FinishRecordAdapter extends RecyclerView.Adapter<FinishRecordHolder> implements Filterable {
    /*日志过滤关键字*/
    public final static String TAG = "FinishRecordAdapter";
    /*interface */
    private RecyclerViewOnItemClickListener mClickListener;
    /*状态标志*/
    private boolean showCheckBox = false;//控制选择框是否显示
    /*基本变量*/
    private Context context;
    private LayoutInflater inflater;
    private List<Record> records = new ArrayList<>();
    /*过滤器*/
    private TextFilter textFilter;
    /*数据库操作异步任务*/

    public FinishRecordAdapter() {
    }

    public FinishRecordAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public static FinishRecordAdapter getInstance() {
        FinishRecordAdapter recordAdapter = null;
        if (recordAdapter == null) {
            recordAdapter = new FinishRecordAdapter();
        }
        return recordAdapter;
    }

    @Override
    public FinishRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fm_three_item, parent, false);
        final FinishRecordHolder holder = new FinishRecordHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FinishRecordHolder holder, final int position) {
        final Record item = records.get(position);
        holder.tvDetail.setText(item.detail);
        holder.tvCreateDate.setText(DateUtils.DateToStr(item.createDate));
//        holder.tvRateStar.setText(item.rate+"X");
        if(item.rate==0) {
            holder.ratingBar.setVisibility(View.INVISIBLE);
        }else {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(item.rate);
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
        return records.size();
    }
    /*Item适配数据*/
    public void setData(List<Record> beans) {
        records.clear();
        records.addAll(beans);
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
        records.add(position, record);
        record.isSelect = false;
        new DaoAsyncTask(context, record).execute(_ADD);
        notifyItemInserted(position);
    }

    public void removeRecord(Record record) {
        int pos = 0;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).id == record.id)
                pos = i;
        }
        records.remove(record);
        new DaoAsyncTask(context, record).execute(_DELETE);
        notifyItemRemoved(pos);
    }
    public void updateRecord(Record record) {
        new DaoAsyncTask(context, record).execute(_UPDATE);
        notifyDataSetChanged();
    }

    public void moveRecord(Record record) {
        records.add(0,record);
        record.isSelect = false;
        notifyItemInserted(0);
        notifyDataSetChanged();
    }

    /**
     * 相关get set 方法
     */
    public Record getRecord(int position) {
        return records.get(position);
    }

    public List<Record> getAllRecord() {
        return records;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        mClickListener = listener;
    }

    /*library方法*/
    public void refreshRecord(Record bean) {
        int index = records.indexOf(bean);
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
                for (int i = 0; i < records.size(); i++) {
                    Record record = records.get(i);
                    String text = record.detail;
                    if (text.contains(query)) {
                        foundRecordList.add(record);
                    }
                }
            } else {
                foundRecordList = records;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.count = foundRecordList.size();
            filterResults.values = foundRecordList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            records = (List<Record>) results.values;
            notifyDataSetChanged();
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
