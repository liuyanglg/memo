package com.galaxy.memov2.utils.database;

import android.content.Context;
import android.util.Log;

/**
 * Created by liuya on 2017/4/7.
 */

public class MemoDBFactory {
    public static MemoDBHelper memoDBHelper=null;
    public static MemoDBHelper getInstance(Context context){
        if(memoDBHelper==null){
            Log.i("MemoDBFactory","create memoDb by dbFactory");
            memoDBHelper = new MemoDBHelper(context);
        }
        return memoDBHelper;
    }

}
