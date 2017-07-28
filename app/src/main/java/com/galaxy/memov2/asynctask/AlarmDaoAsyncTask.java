package com.galaxy.memov2.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.galaxy.memov2.entity.Alarm;
import com.galaxy.memov2.utils.database.AlarmDao;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.galaxy.memov2.entity.OperateConstant._ADD;
import static com.galaxy.memov2.entity.OperateConstant._DELETE;
import static com.galaxy.memov2.entity.OperateConstant._DELETE_BY_ID;
import static com.galaxy.memov2.entity.OperateConstant._FIND_ALL;
import static com.galaxy.memov2.entity.OperateConstant._SAVE_OR_UPDATE;
import static com.galaxy.memov2.entity.OperateConstant._UPDATE;
import static com.galaxy.memov2.ui.fragment.first.ListFirstFragment.TAG;

/**
 * Created by liuya on 2017/4/24.
 */

public class AlarmDaoAsyncTask extends AsyncTask<Integer, Void, List<Alarm>> {
    public static String TAG = "AlarmDaoAsyncTask";
    private List<Alarm> queryList = new ArrayList<>();
    private int operateCode;
    private Context context;
    private Alarm alarm;

    public AlarmDaoAsyncTask(Context context, List<Alarm> queryList) {
        super();
        this.context = context;
        this.queryList = queryList;
    }

    public AlarmDaoAsyncTask(Context context, Alarm alarm) {
        super();
        this.context = context;
        this.alarm = alarm;
    }

    @Override
    protected List<Alarm> doInBackground(Integer... params) {
        operateCode = params[0];
        List<Alarm> alarms = new ArrayList<>();
        switch (operateCode) {
            case _ADD:
                AlarmDao.daoAddAlarm(context, alarm);
                break;
            case _UPDATE:
                AlarmDao.daoUpdateAlarm(context, alarm);
                break;
            case _DELETE:
                AlarmDao.daoDeleteAlarm(context, alarm);
                break;
            case _DELETE_BY_ID:
                AlarmDao.daoDeleteByID(context, alarm.alarmId);
                break;
            case _FIND_ALL:
                queryList.clear();
                queryList.addAll(AlarmDao.daoFindAllAlarm(context));
                Log.i(TAG, "alarm int async size is"+queryList.size());
                break;
            case _SAVE_OR_UPDATE:
                AlarmDao.saveOrUpdate(context,alarm);
                break;
        }
        return queryList;
    }

    @Override
    protected void onPostExecute(List<Alarm> alarms) {
        super.onPostExecute(alarms);
        if(operateCode==_FIND_ALL) {
//            EventBus.getDefault().post(queryList);
            Log.i(TAG, "QueryAllTask send a massage");
        }
    }
}
