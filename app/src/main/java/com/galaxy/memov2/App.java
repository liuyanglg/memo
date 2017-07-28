package com.galaxy.memov2;

import android.app.Application;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * Created by liuya on 2017/4/17.
 */

public class App extends Application {
//    public List<Record> allData = new ArrayList<>();

    @Override
    public void onCreate() {
//        allData=MemoDao.daoFindAllRecord(this);
        super.onCreate();

        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                    }
                })
                .install();
//        new QueryData().execute();
    }

//    class QueryData extends AsyncTask{
//        @Override
//        protected List<Record> doInBackground(Object[] params) {
//            allData=MemoDao.daoFindAllRecord(App.this);
//            Log.i("App", allData.size()+"");
//            return allData;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//        }
//    }
}
