 package com.sseung.akasoschedule;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends Activity implements MenuFragment.OnClickCloseButton, UploadFragment.setUploadClickListener{

    LinearLayout button_search, button_menu, click_write;
    RecyclerView upload_recycler;

    FrameLayout fragment_layout;

    Fragment fragment1 = new MenuFragment();
    Fragment fragment2 = new UploadFragment();

    Upload_Adapter adapter;
    HashMap<Integer, Upload_Item> upload_list = new HashMap<>();

    View decorView;
    int	uiOption;

    private AdView mAdView;

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
        setContentView(R.layout.upload_activity);

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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        button_search = findViewById(R.id.button_search);
        button_menu = findViewById(R.id.button_menu);

        click_write = findViewById(R.id.click_write);
        fragment_layout = findViewById(R.id.fragment_layout);

        upload_list = UseFunction.upload_list;

        upload_recycler = findViewById(R.id.upload_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        upload_recycler.setLayoutManager(manager);

//
//        adapter.addItem(new Upload_Item(2021, 6, 16, 2021, 6, 16, "테스트1", "테스트1", "검토중"));
//        adapter.addItem(new Upload_Item(2021, 6, 15, 2021, 6, 17, "테스트2", "테스트2", "검토중"));
//        adapter.addItem(new Upload_Item(2021, 5, 12, 2021, 6, 18, "테스트3", "테스트3", "검토중"));

        sortList();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yy=MM-dd");

        Log.d("tlqkf", calendar.get(Calendar.DATE) + ", " + format.format(calendar.get(Calendar.DATE)));

        adapter.setOnItemClickListener(new OnUploadItemClickListener() {
            @Override
            public void onItemClick(Upload_Adapter.ViewHolder holder, View view, int position) {

            }
        });

        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_layout.setBackgroundColor(Color.parseColor("#00000000"));

                Bundle bundle = new Bundle();
                bundle.putString("click", "upload");

                fragment1 = new MenuFragment();
                fragment1.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment1).commit();

                fragment_layout.setVisibility(View.VISIBLE);

                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_in);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fragment_layout.setBackgroundColor(Color.parseColor("#8C000000"));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                fragment_layout.startAnimation(anim);
            }
        });

        click_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_layout.setVisibility(View.VISIBLE);

                fragment2 = new UploadFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment2).commit();

                fragment_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void sortList(){
        adapter = new Upload_Adapter();

        Object[] list_sort = upload_list.keySet().toArray();
        Arrays.sort(list_sort);

        for (Integer number : upload_list.keySet()){
            adapter.addItem(upload_list.get(number));
        }

        upload_recycler.setAdapter(adapter);
    }

    @Override
    public void onClickCloseButton() {
        fragment_layout.setBackgroundColor(Color.parseColor("#00000000"));
        Animation outAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_out);

        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getFragmentManager().beginTransaction().remove(fragment1).commit();
                fragment_layout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fragment_layout.startAnimation(outAnim);
    }

    @Override
    public void clickOnSaveButton(Upload_Item item) {
        onWindowFocusChanged(true);
        getFragmentManager().beginTransaction().remove(fragment2).commit();
        fragment_layout.setVisibility(View.INVISIBLE);

        sortList();
    }

    @Override
    public void clickOnCancelButton() {
        onWindowFocusChanged(true);
        getFragmentManager().beginTransaction().remove(fragment2).commit();
        fragment_layout.setVisibility(View.INVISIBLE);
    }
}
