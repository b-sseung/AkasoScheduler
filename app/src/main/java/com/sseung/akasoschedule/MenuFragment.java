package com.sseung.akasoschedule;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class MenuFragment extends Fragment {

    LinearLayout menu_calendar, menu_list, menu_upload, menu_setting, calendar_color, list_color;

    LinearLayout close_menu;

    TextView dday_text;

    String openActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.menu_activity, container, false);

        menu_calendar = rootView.findViewById(R.id.menu_calendar);
        menu_list = rootView.findViewById(R.id.menu_list);
        menu_upload = rootView.findViewById(R.id.menu_upload);
        menu_setting = rootView.findViewById(R.id.menu_setting);

        calendar_color = rootView.findViewById(R.id.menu_change_calendar);
        list_color = rootView.findViewById(R.id.menu_change_list);

        close_menu = rootView.findViewById(R.id.close_menu);

        dday_text = rootView.findViewById(R.id.dday_text);

        Bundle bundle = getArguments();
        openActivity = bundle.getString("click");

        Calendar calendar = Calendar.getInstance();

        Date date1 = new Date(2021, 06, 16);
        Date date2 = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        long time = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
        Log.d("tlqkf", "지난 시간 : " + time);

        if (time < 0) {
            dday_text.setText("D+" + Math.abs(time));
        } else if (time == 0) {
            dday_text.setText("D-day");
        } else {
            dday_text.setText("D-" + time);
        }


        menu_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openActivity.equals("calendar")) {
                    ((OnClickCloseButton) container.getContext()).onClickCloseButton();
                } else {
                    Intent intent = new Intent(container.getContext(), CalendarActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        menu_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openActivity.equals("list")) {
                    ((OnClickCloseButton) container.getContext()).onClickCloseButton();
                } else {
                    Intent intent = new Intent(container.getContext(), ListActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        menu_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openActivity.equals("upload")) {
                    ((OnClickCloseButton) container.getContext()).onClickCloseButton();
                } else {
                    if (NetworkStatus.getConnectivityStatus(UseFunction.mainContext) == 3) {
                        Intent intent = new Intent(container.getContext(), NoInternetMessage2.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(container.getContext(), UploadActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }
        });

        menu_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendar_color.getVisibility() == View.VISIBLE) {
                    calendar_color.setVisibility(View.INVISIBLE);
                    list_color.setVisibility(View.INVISIBLE);
                } else {
                    calendar_color.setVisibility(View.VISIBLE);
                    list_color.setVisibility(View.VISIBLE);
                }
            }
        });

        calendar_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        list_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        close_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnClickCloseButton) container.getContext()).onClickCloseButton();
            }
        });



        return rootView;


    }

    public interface OnClickCloseButton{
        public void onClickCloseButton();
    }
}
