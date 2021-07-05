package com.sseung.akasoschedule;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;

import java.util.Calendar;
import java.util.Date;

public class SearchDateFragment extends Fragment {

    DatePicker datePicker;
    ImageView close_image;
    TextView clear_text, select_text;
    EditText year_edit, month_edit, day_edit;

    String[] st = new String[]{"* 연도는 최소 1994년 이후부터 입력이 가능합니다.",
            "* 월은 1에서 12 사이에서만 입력이 가능합니다.", "* 일은 1에서 31 사이에서만 입력이 가능합니다.", "해당 월의 마지막 일자는 ", "입니다."};
    TextView message1, message2, message3;
    ImageView choice_calendar;

    InputMethodManager inputManager;
    int select_year, select_month, select_day;

    Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_date_acivity, container, false);

        Bundle bundle = getArguments();
        select_year = bundle.getInt("year");
        select_month = bundle.getInt("month") - 1;
        select_day = bundle.getInt("day", 1);

        Log.d("tlqkf", select_year + ", " + select_month + ", " + select_day);
        calendar.set(select_year, select_month, select_day);

        datePicker = rootView.findViewById(R.id.datePicker);
        close_image = rootView.findViewById(R.id.close_datePicker);
        clear_text = rootView.findViewById(R.id.clear_datePicker);
        select_text = rootView.findViewById(R.id.select_datePicker);

        year_edit = rootView.findViewById(R.id.edit_year);
        month_edit = rootView.findViewById(R.id.edit_month);
        day_edit = rootView.findViewById(R.id.edit_day);

        year_edit.setText("" + select_year);
        month_edit.setText("" + (select_month + 1));
        day_edit.setText("" + select_day);

        year_edit.setSelection(year_edit.getText().length());
        month_edit.setSelection(month_edit.getText().length());
        day_edit.setSelection(day_edit.getText().length());

        message1 = rootView.findViewById(R.id.message1);
        message2 = rootView.findViewById(R.id.message2);
        message3 = rootView.findViewById(R.id.message3);
        choice_calendar = rootView.findViewById(R.id.choice_date);

        datePicker.updateDate(select_year, select_month, select_day);

        inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        choice_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_text.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.VISIBLE);
                inputManager.hideSoftInputFromWindow(year_edit.getWindowToken(), 0);
                inputManager.hideSoftInputFromWindow(month_edit.getWindowToken(), 0);
                inputManager.hideSoftInputFromWindow(day_edit.getWindowToken(), 0);
            }
        });

        select_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year_edit.setText("" + datePicker.getYear());
                month_edit.setText("" + (datePicker.getMonth() + 1));
                day_edit.setText("" + datePicker.getDayOfMonth());

                select_text.setVisibility(View.INVISIBLE);
                datePicker.setVisibility(View.INVISIBLE);
            }
        });

        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean value = true;

                message1.setVisibility(View.INVISIBLE);
                message2.setVisibility(View.INVISIBLE);
                message3.setVisibility(View.INVISIBLE);

                select_year = Integer.parseInt(year_edit.getText().toString());
                select_month = Integer.parseInt(month_edit.getText().toString()) - 1;
                select_day = Integer.parseInt(day_edit.getText().toString());

                calendar.set(select_year, select_month, 1);

                if (select_year < 1994) {
                    calendar.set(Calendar.YEAR, 1994);
                    year_edit.setText("" + 1994);

                    message1.setVisibility(View.VISIBLE);
                    message1.setText(st[0]);
                    value = false;
                }

                if (select_month + 1 < 1 || select_month + 1 > 12) {
                    if (select_month + 1 < 1) {
                        calendar.set(Calendar.MONTH, 0);
                        month_edit.setText("" + 1);
                    } else if (select_month + 1 > 12) {
                        calendar.set(Calendar.MONTH, 11);
                        month_edit.setText("" + 12);
                    }

                    if (message1.getVisibility() == View.INVISIBLE) {
                        message1.setVisibility(View.VISIBLE);
                        message1.setText(st[1]);
                    } else {
                        message2.setVisibility(View.VISIBLE);
                        message2.setText(st[1]);
                    }
                    value = false;

                }

                if (select_day < 1 || select_day > 31) {
                    if (message1.getVisibility() == View.INVISIBLE) {
                        message1.setVisibility(View.VISIBLE);
                        message1.setText(st[2]);
                    } else {
                        if (message2.getVisibility() == View.INVISIBLE) {
                            message2.setVisibility(View.VISIBLE);
                            message2.setText(st[2]);
                        } else {
                            message3.setVisibility(View.VISIBLE);
                            message3.setText(st[2]);
                        }
                    }
                    value = false;
                } else {

                    Log.d("tlqkf", "마지막 날 : " + select_day + ", " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.MONTH));
                    if (select_day > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        String temp = st[3] + calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + st[4];
                        if (message1.getVisibility() == View.INVISIBLE) {
                            message1.setVisibility(View.VISIBLE);
                            message1.setText(temp);
                        } else {
                            if (message2.getVisibility() == View.INVISIBLE) {
                                message2.setVisibility(View.VISIBLE);
                                message2.setText(temp);
                            } else {
                                message3.setVisibility(View.VISIBLE);
                                message3.setText(temp);
                            }
                        }
                        value = false;
                    }
                }

                if (!value) return;

                Log.d("tlqkf", "선택 완료 : " + select_year + ", " + select_month + ", " + select_day);
                ((OnClickSearchListener)container.getContext()).onClickSearchListener(true, select_year, select_month, select_day);
            }
        });

        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_year = Integer.parseInt(year_edit.getText().toString());
                select_month = Integer.parseInt(month_edit.getText().toString()) - 1;
                select_day = Integer.parseInt(day_edit.getText().toString());

                Log.d("tlqkf", "선택 완료 : " + select_year + ", " + select_month + ", " + select_day);
                ((OnClickSearchListener)container.getContext()).onClickSearchListener(false, select_year, select_month, select_day);
            }
        });
        return rootView;
    }

    public interface OnClickSearchListener {
        public void onClickSearchListener(boolean value, int year, int month, int day);
    }


}
