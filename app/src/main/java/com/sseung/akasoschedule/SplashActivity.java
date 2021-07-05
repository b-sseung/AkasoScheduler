package com.sseung.akasoschedule;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    boolean value = true;
    ScheduleDatabase database;

    View decorView;
    int	uiOption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        //상단바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility( uiOption );

//        UseAll.window = getWindow();
//        UseAll.hideNavigationBar();

        if (UseFunction.mainContext == null) UseFunction.mainContext = this;
        database = ScheduleDatabase.getInstance(UseFunction.mainContext);
        database.open();

        boolean popup = UseFunction.getloadPopUp();

        if (NetworkStatus.getConnectivityStatus(UseFunction.mainContext) != 3){
            FireBaseFunction.readData();
            FireBaseFunction.readUploadData();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (popup) {
                    Intent intent = new Intent(getApplicationContext(), PopUpActivity.class);
                    if (value) startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                    if (value) startActivity(intent);
                }

                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                finish();
            }
        }, 4000);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if( hasFocus ) {
            decorView.setSystemUiVisibility(uiOption);
        }
    }
}
