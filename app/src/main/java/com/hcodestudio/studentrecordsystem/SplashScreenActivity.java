package com.hcodestudio.studentrecordsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by hassan on 11/28/2017.
 */

public class SplashScreenActivity  extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, 5*1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

