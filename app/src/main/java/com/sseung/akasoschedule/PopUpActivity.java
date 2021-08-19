package com.sseung.akasoschedule;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class PopUpActivity extends Activity {

    LinearLayout click_checkbox, click_close;
    ImageView checkbox;

    boolean check = false;

    VideoView videoView;
    View decorView;
    int	uiOption;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if( hasFocus ) {
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_activity);


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

        click_checkbox = findViewById(R.id.click_checkbox);
        click_close = findViewById(R.id.click_close);

        checkbox = findViewById(R.id.checkbox);

//        videoView = findViewById(R.id.video);

//        MediaController controller = new MediaController(this);
//        videoView.setMediaController(controller);
//
//        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video2);
//        videoView.setVideoURI(videoUri);
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                videoView.seekTo(0);
//                videoView.start();
//            }
//        });

        click_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    checkbox.setImageResource(R.drawable.no_select_checkbox);
                } else {
                    checkbox.setImageResource(R.drawable.select_checkbox);
                }

                check = !check;
            }
        });

        click_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    updatePopUp("false");
                } else {
                    updatePopUp("true");
                }

                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                finish();
            }
        });
    }

    public void updatePopUp(String value) {
        ScheduleDatabase database = ScheduleDatabase.getInstance(UseFunction.mainContext);

        Calendar calendar = Calendar.getInstance();

        Log.d("tlqkf", calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.MONTH) + ", " + calendar.get(Calendar.DAY_OF_MONTH));

        String sql = "update " + ScheduleDatabase.TABLE2 +
                " set "
                + " year = " + calendar.get(Calendar.YEAR) + ""
                + ", month = " + calendar.get(Calendar.MONTH) + ""
                + ", day = " + calendar.get(Calendar.DAY_OF_MONTH) + ""
                + ", value = '" + value + "'";

        Log.d("tlqkf", "sql : " + sql);

        database.execSQL(sql);
    }
}
