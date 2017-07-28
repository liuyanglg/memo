package com.galaxy.memov2.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.galaxy.memov2.utils.database.AlarmDao;
import com.galaxy.memov2.utils.database.MemoDao;
import com.galaxy.memov2.entity.Record;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.galaxy.memov2.entity.OperateConstant._ADD;
import static com.galaxy.memov2.entity.OperateConstant._DELETE;
import static com.galaxy.memov2.entity.OperateConstant._FIND_ALL;
import static com.galaxy.memov2.entity.OperateConstant._FIND_FINISH;
import static com.galaxy.memov2.entity.OperateConstant._FIND_UN_FINISH;
import static com.galaxy.memov2.entity.OperateConstant._UPDATE;
import static com.galaxy.memov2.ui.fragment.first.ListFirstFragment.TAG;
import static com.galaxy.memov2.utils.Constant.IS_FINISH;

/**
 * Created by liuya on 2017/4/24.
 */

public class DaoAsyncTask extends AsyncTask<Integer, Void, List<Record>> {
    private List<Record> queryList = new ArrayList<>();
    private int operateCode;
    private Context context;
    private Record record;

    public DaoAsyncTask(Context context, List<Record> queryList) {
        super();
        this.context = context;
        this.queryList = queryList;
    }

    public DaoAsyncTask(Context context, Record record) {
        super();
        this.context = context;
        this.record = record;
    }

    @Override
    protected List<Record> doInBackground(Integer... params) {
        operateCode = params[0];
        List<Record> records = new ArrayList<>();
        switch (operateCode) {
            case _ADD:
                MemoDao.daoAddRecord(context, record);
                break;
            case _UPDATE:
                MemoDao.daoUpdateRecord(context, record);
                break;
            case _DELETE:
                MemoDao.daoDeleteRecord(context, record);
                AlarmDao.daoDeleteByID(context, record.id);
                break;
            case _FIND_ALL:
                queryList=MemoDao.daoFindAllRecord(context);
                break;
            case _FIND_FINISH:
                records=MemoDao.daoFindAllRecord(context);
                for(int i=0;i<records.size();i++){
                    Record record = records.get(i);
                    record.isSelect = false;
                    if(record.isFinish==IS_FINISH){
                        queryList.add(record);
                    }
                }
                break;
            case _FIND_UN_FINISH:
                records=MemoDao.daoFindAllRecord(context);
                for(int i=0;i<records.size();i++){
                    Record record = records.get(i);
                    record.isSelect = false;
                    if(record.isFinish!=IS_FINISH){
                        queryList.add(record);
                    }
                }
                break;
        }
        return queryList;
    }

    @Override
    protected void onPostExecute(List<Record> records) {
        super.onPostExecute(records);
        if(operateCode==_FIND_ALL) {
            EventBus.getDefault().post(queryList);
            Log.i(TAG, "QueryAllTask send a massage");
        }
    }
}
