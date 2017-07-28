package com.galaxy.memov2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.galaxy.memov2.R;
import com.galaxy.memov2.service.AlarmService;

public class ServiceActivity extends AppCompatActivity {
    Handler handler = new Handler();
    PowerManager pm;
    PowerManager.WakeLock wl;

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service);
        button = (Button) findViewById(R.id.start_service);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(ServiceActivity.this, AlarmService.class);
                startService(service);
            }
        });

    }

    public void closeScreen() {
        wl.release();
    }

}
