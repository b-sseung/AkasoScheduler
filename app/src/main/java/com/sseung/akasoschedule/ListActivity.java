package com.sseung.akasoschedule;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
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
import android.widget.ListAdapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ListActivity extends Activity implements ClickItemFragment.setClickCloseText, MenuFragment.OnClickCloseButton{

    RecyclerView list_recyclerView;
    LinearLayout button_menu, button_search;

    List_Adapter adapter;

    ArrayList<Schedule_Item> items = new ArrayList<>();

    FrameLayout fragment_layout;
    Fragment fragment1 = new ClickItemFragment();
    Fragment fragment3 = new MenuFragment();

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
        setContentView(R.layout.list_activity);

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

        list_recyclerView = findViewById(R.id.list_recyclerView);

        button_menu = findViewById(R.id.button_menu);
        button_search = findViewById(R.id.button_search);

        fragment_layout = findViewById(R.id.fragment_layout);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        list_recyclerView.setLayoutManager(manager);
        adapter = new List_Adapter();

        addDBData();

        Log.d("tlqkf", "items size : " + items.size());

        for (int i = 0; i < items.size(); i++) {
            Log.d("tlqkf", "i : " + i + ", " +  items.get(i));

            adapter.addItem(items.get(i));
        }

        list_recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(List_Adapter.ViewHolder holder, View view, int position) {
                Schedule_Item item = adapter.getItem(position);

                Log.d("tiqkf", "click : " + position);
                Log.d("tiqkf", item.getYear() + ", " + item.getMonth() + ", " + item.getDay());

                Bundle bundle = new Bundle();

                bundle.putInt("year", item.getYear());
                bundle.putInt("month", item.getMonth());
                bundle.putInt("day", item.getDay());
                bundle.putString("division", item.getDivision());
                bundle.putString("detail", item.getDetail());
                bundle.putString("name", item.getName());
                bundle.putString("time", item.getTime());
                bundle.putString("image", item.getImage());
                bundle.putString("uri", item.getUri());
                bundle.putString("sale", item.getSale());
                bundle.putString("source", item.getSource());

                fragment1 = new ClickItemFragment();
                fragment1.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment1).commit();

                fragment_layout.setVisibility(View.VISIBLE);
            }
        });

        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_layout.setBackgroundColor(Color.parseColor("#00000000"));

                Bundle bundle = new Bundle();
                bundle.putString("click", "list");

                fragment3 = new MenuFragment();
                fragment3.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment3).commit();

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
    }

    public void addDBData(){
        ScheduleDatabase database = ScheduleDatabase.getInstance(UseFunction.mainContext);

        Log.d("tlqkf", "call addDBData");

        String sql = "select _id, number, year, month, day, division, detail, name, time, image, uri, sale, source "
                + "from " + ScheduleDatabase.TABLE1 + " "
                + "order by year desc, month desc, day desc";

        Log.d("database", sql);

        Cursor cursor = database.rawQuery(sql);
        int recordCound = cursor.getCount();

        Log.d("tlqkf", String.valueOf(recordCound));

        if (recordCound == 0) {
            return;
        }

        for (int i = 0; i < recordCound; i++){
            cursor.moveToNext();

            int number = cursor.getInt(1);
            int year = cursor.getInt(2);
            int month = cursor.getInt(3);
            int day = cursor.getInt(4);
            String division = cursor.getString(5);
            String detail = cursor.getString(6);
            String name = cursor.getString(7);
            String time = cursor.getString(8);
            String image = cursor.getString(9);
            String uri = cursor.getString(10);
            String sale = cursor.getString(11);
            String source = cursor.getString(12);

            Log.d("tlqkf", "load : " + year + ",  " + month + ",  " + day + ",  " + division
                    + ",  " + detail + ",  " + name + ",  " + time + ",  " + image + ",  " + uri + ",  " + sale + ",  " + source);


            items.add(new Schedule_Item(year, month, day, division, detail, name, time, image, uri, sale, source));
        }
    }

    @Override
    public void onClickCloseText() {
        onWindowFocusChanged(true);
        getFragmentManager().beginTransaction().remove(fragment1).commit();
        fragment_layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {

        if (fragment_layout.getVisibility() == View.VISIBLE) {
            onWindowFocusChanged(true);
            getFragmentManager().beginTransaction().remove(fragment1).commit();
            getFragmentManager().beginTransaction().remove(fragment3).commit();
            fragment_layout.setVisibility(View.INVISIBLE);
        } else {
            finish();
        }
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
                getFragmentManager().beginTransaction().remove(fragment3).commit();
                fragment_layout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fragment_layout.startAnimation(outAnim);
    }
}
