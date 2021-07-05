package com.sseung.akasoschedule;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CalendarActivity extends Activity
        implements ClickItemFragment.setClickCloseText, SearchDateFragment.OnClickSearchListener, MenuFragment.OnClickCloseButton{

    ImageView[] days = new ImageView[43];
    int[] days_number = new int[43];
    ImageView[] select_circles = new ImageView[43];
    ImageView[] mini_circles = new ImageView[43];
    int[] blacks = new int[32];
    int[] reds = new int[32];
    int circle, no_select_mini, yes_select_mini, day_blank;

    TextView now_text, next_text, pre_text, no_schedule_text;
    LinearLayout button_search, button_menu;

    Calendar calendar = Calendar.getInstance();
    int open_year, open_month, today, start_day, last_day;

    int clickPosition = 0;

    RecyclerView calendar_list;
    Schedule_Adapter adapter;

    ArrayList<Schedule_Item> items = new ArrayList<>();
    HashMap<String, ArrayList<Schedule_Item>> list_items = new HashMap<>();

    Fragment fragment1 = new ClickItemFragment();
    Fragment fragment2 = new SearchDateFragment();
    Fragment fragment3 = new MenuFragment();

    FrameLayout fragment_layout;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

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

        no_select_mini = R.drawable.no_select_mini_circle;
        yes_select_mini = R.drawable.select_mini_circle;
        day_blank = R.drawable.day_blank;
        circle = R.drawable.select_day_circle;

        button_search = findViewById(R.id.button_search);
        button_menu = findViewById(R.id.button_menu);

        now_text = findViewById(R.id.now_text);
        next_text = findViewById(R.id.next_text);
        pre_text = findViewById(R.id.pre_text);

        no_schedule_text = findViewById(R.id.no_schedule_text);
        fragment_layout = findViewById(R.id.fragment_layout);

        calendar_list = findViewById(R.id.calendar_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        calendar_list.setLayoutManager(layoutManager);
        adapter = new Schedule_Adapter();
        calendar_list.setAdapter(adapter);

//        items.add(new Schedule_Item(2021, 5, 28, "방송", "예능", "TBS 世界ふしぎ発見！", "20:00", "", "", "", ""));
//        items.add(new Schedule_Item(2021, 5, 27, "미디어", "잡지", "CLASSY 7월호", "", "", "", "알라딘", ""));
//        items.add(new Schedule_Item(2021, 5, 04, "미디어", "잡지", "CLASSY 6월호", "",  "", "", "예스24", ""));

        today = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= 42; i++){
            String num = Integer.toString(i);

            if (i < 10) num = "0" + num;

            int day_layout = getResources().getIdentifier("day_" + num, "id", getApplicationContext().getPackageName());
            int circle_layout = getResources().getIdentifier("select_circle_" + num, "id", getApplicationContext().getPackageName());
            int mini_layout = getResources().getIdentifier("mini_circle_" + num, "id", getApplicationContext().getPackageName());

            days[i] = (ImageView) findViewById(day_layout);
            select_circles[i] = (ImageView) findViewById(circle_layout);
            mini_circles[i] = (ImageView) findViewById(mini_layout);

            select_circles[i].setVisibility(View.INVISIBLE);
            mini_circles[i].setVisibility(View.INVISIBLE);

            int position = i;
            days[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dayClickListener(position);
                }
            });
        }

        for (int i = 1; i <= 31; i++){
            String num = Integer.toString(i);

            if (i < 10) num = "0" + num;

            int black = getResources().getIdentifier("day_black_" + num, "drawable", getApplicationContext().getPackageName());
            int red = getResources().getIdentifier("day_red_" + num, "drawable", getApplicationContext().getPackageName());

            blacks[i] = black;
            reds[i] = red;
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        open_year = calendar.get(Calendar.YEAR);
        open_month = calendar.get(Calendar.MONTH) + 1;
        start_day = calendar.get(Calendar.DAY_OF_WEEK);
        last_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        now_text.setText(open_year + "년 " + open_month + "월");

        if (NetworkStatus.getConnectivityStatus(UseFunction.mainContext) == 3){
            Intent intent = new Intent(getApplicationContext(), NoInternetMessage.class);
            startActivity(intent);

            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }

        addDBData();
        Log.d("tlqkf", "items size : " + items.size());

        setAllData();

        setCalendar(start_day, last_day);
        setItemAdapter(today);

        adapter.setOnItemClickListener(new OnScheduleItemClickListener() {
            @Override
            public void onItemClick(Schedule_Adapter.ViewHolder holder, View view, int position) {
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

        pre_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;

                open_year = year;
                open_month = month;
                setCalendar(calendar.get(Calendar.DAY_OF_WEEK), calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                now_text.setText(year + "년 " + month + "월");
            }
        });

        next_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, +1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;

                open_year = year;
                open_month = month;
                setCalendar(calendar.get(Calendar.DAY_OF_WEEK), calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                now_text.setText(year + "년 " + month + "월");
            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putInt("year", open_year);
                bundle.putInt("month", open_month);
                bundle.putInt("day", days_number[clickPosition]);

                fragment2 = new SearchDateFragment();
                fragment2.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment2).commit();

                fragment_layout.setVisibility(View.VISIBLE);

                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.picker_in);
                fragment_layout.startAnimation(anim);
//                Intent intent = new Intent(getApplicationContext(), SearchDateActivity.class);
//                intent.putExtra("year", open_year);
//                intent.putExtra("month", open_month);
//                intent.putExtra("day", days_number[clickPosition]);
//
//                Log.d("tlqkf", "search : " + open_year + ", " + open_month + ", " + clickPosition + ", " + days_number[clickPosition]);
//                startActivityForResult(intent, 101);
            }
        });

        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_layout.setBackgroundColor(Color.parseColor("#00000000"));

                Bundle bundle = new Bundle();
                bundle.putString("click", "calendar");

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

    public void dayClickListener(int position){
        Log.d("tlqkf", "position : " + position + ", " + days_number[position]);

        if (days_number[position] != 0){

            no_schedule_text.setVisibility(View.INVISIBLE);
            select_circles[clickPosition].setVisibility(View.INVISIBLE);
            if (mini_circles[clickPosition].getVisibility() == View.VISIBLE) mini_circles[clickPosition].setImageResource(no_select_mini);

            select_circles[position].setImageResource(circle);
            select_circles[position].setVisibility(View.VISIBLE);
            mini_circles[position].setImageResource(yes_select_mini);

            clickPosition = position;

            if (mini_circles[position].getVisibility() == View.INVISIBLE) no_schedule_text.setVisibility(View.VISIBLE);

            setItemAdapter(days_number[position]);
        }
    }

    public void setItemAdapter(int position) {
        ArrayList<Schedule_Item> temp = list_items.get(Integer.toString(open_year) + Integer.toString(open_month) + Integer.toString(position));

        if (temp == null) temp = new ArrayList<>();

        Log.d("tlqkf", "temp : " + Integer.toString(open_year) + Integer.toString(open_month) + Integer.toString(position));
//        Log.d("tlqkf", "temp size : " + temp.size());

        adapter.setItems(temp);
        adapter.notifyDataSetChanged();
    }

    public void setAllData(){
        for (int i = 0; i < items.size(); i++) {
            Schedule_Item item = items.get(i);

            String st = "" + item.getYear() + item.getMonth() + item.getDay();

            ArrayList<Schedule_Item> temp = (list_items.get(st) != null) ? list_items.get(st) : new ArrayList<>();

            temp.add(item);

            list_items.put(st, temp);
        }
    }

    public void setRecyclerData(){
        for (int i = 1; i <= last_day; i++){
            String st = "" + open_year + open_month + i;

            Log.d("tlqkf", "date : " + st);

            if (list_items.get(st) != null) {
                if (mini_circles[i + start_day - 1].getVisibility() == View.INVISIBLE) {
                    mini_circles[i + start_day - 1].setVisibility(View.VISIBLE);
                    mini_circles[i + start_day - 1].setImageResource(no_select_mini);
                }
            }
        }
    }

    public void allInvisible(){
        for (int i = 1; i <= 42; i++) {
            select_circles[i].setVisibility(View.INVISIBLE);
            mini_circles[i].setVisibility(View.INVISIBLE);
        }
    }

    public void setCalendar(int start, int last){
        allInvisible();

        start_day = start;
        last_day = last;

        for (int i = 1; i < start; i++){
            days[i].setImageResource(day_blank);
            days_number[i] = 0;
            Log.d("tlqkf", "1 : " + i);
        }

        for (int i = 0; i < last; i++){
            days[i + start].setImageResource(blacks[i + 1]);
            if (i + 1 == today) {
                select_circles[i + start].setImageResource(R.drawable.today);
                select_circles[i + start].setVisibility(View.VISIBLE);
                clickPosition = i + start;
                Log.d("tlqkf", "today : " + i + ", " + today);
            }
            days_number[i + start] = i + 1;
            Log.d("tlqkf", "2 : " + i + ", " + (i + start));
        }

        for (int i = start + last; i < 43; i++){
            days[i].setImageResource(day_blank);
            days_number[i] = 0;
            Log.d("tlqkf", "3 : " + i);
        }

        setRecyclerData();

    }

    @Override
    public void onClickCloseText() {
        onWindowFocusChanged(true);
        getFragmentManager().beginTransaction().remove(fragment1).commit();
        fragment_layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickSearchListener(boolean value, int year, int month, int day) {
        onWindowFocusChanged(true);
        getFragmentManager().beginTransaction().remove(fragment2).commit();
        fragment_layout.setVisibility(View.INVISIBLE);

        if (!value) return;

        allInvisible();

        calendar.set(year, month, 1);

        open_year = year;
        open_month = month + 1;
        setCalendar(calendar.get(Calendar.DAY_OF_WEEK), calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        now_text.setText(open_year + "년 " + open_month + "월");

        dayClickListener(day + calendar.get(Calendar.DAY_OF_WEEK) - 1);
        setItemAdapter(day);
    }

    @Override
    public void onBackPressed() {

        if (fragment_layout.getVisibility() == View.VISIBLE) {
            onWindowFocusChanged(true);
            getFragmentManager().beginTransaction().remove(fragment1).commit();
            getFragmentManager().beginTransaction().remove(fragment2).commit();
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