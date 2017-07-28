package com.galaxy.memov2.utils.alarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.galaxy.memov2.R;


public class AlarmPopupDialog extends Dialog {

    private Context context;
    /*view*/
    private TextView tvAlarmTitle;
    private TextView tvAlarmDetail;
    private Button btnConfirm;
    private Button btnAgain;
    private View view;
    private AlarmDialogOnClickListener listener;

    public AlarmPopupDialog(Context context) {
        super(context, R.style.FullScreenDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                |WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        initView();
        initEvent();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_alarm_popup, null);
        tvAlarmTitle = (TextView) view.findViewById(R.id.tv_alarm_dialog_title);
        tvAlarmDetail = (TextView) view.findViewById(R.id.tv_alarm_dialog_detail);
        btnConfirm = (Button) view.findViewById(R.id.btn_alarm_dialog_confirm);
        btnAgain = (Button) view.findViewById(R.id.btn_alarm_dialog_again);
        setContentView(view);
        setTitle("提示信息");
    }

    public void initEvent(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ButtonConfirmOnClick(v);
            }
        });
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ButtonAgainOnClick(v);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public AlarmPopupDialog setMessage(String message) {
        tvAlarmDetail.setText(message);
        return this;
    }

    public AlarmPopupDialog setTitle(String title) {
        tvAlarmTitle.setText(title);
        return this;
    }

    public void setAlarmDialogOnClickListener(AlarmDialogOnClickListener listener) {
        this.listener = listener;
    }
}
