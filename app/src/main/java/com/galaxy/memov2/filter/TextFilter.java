package com.galaxy.memov2.filter;

import android.util.Log;
import android.widget.Filter;

import com.galaxy.memov2.adapter.RecordAdapter;
import com.galaxy.memov2.entity.Record;

import java.util.ArrayList;
import java.util.List;

public class TextFilter extends Filter {
    RecordAdapter recordAdapter = null;
    List<Record> mItems = new ArrayList<>();

    public TextFilter(RecordAdapter recordAdapter) {
        this.recordAdapter = recordAdapter;
        this.mItems = recordAdapter.getAllItem();
    }

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
        recordAdapter.notifyDataSetChanged();
    }
}