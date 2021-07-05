package com.sseung.akasoschedule;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;

import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;

import static androidx.core.content.ContextCompat.getSystemService;

public class UploadFragment extends Fragment {

    EditText edit_date, edit_name;
    ImageView click_image;
    LinearLayout click_calendar;
    LinearLayout click_save, click_cancel;
    LinearLayout dialog_layout;
    DatePicker datePicker;
    TextView select_datePicker;
    TextView text_date, text_name, text_image;

    Calendar calendar = Calendar.getInstance();
    Calendar minCal = Calendar.getInstance();

    int year, month, day;
    boolean isSelectImage = false;
    Uri selectUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.upload_save_activity, container, false);

        edit_date = rootView.findViewById(R.id.edit_date);
        edit_name = rootView.findViewById(R.id.edit_name);
        click_calendar = rootView.findViewById(R.id.click_calendar);
        click_image = rootView.findViewById(R.id.click_image);
        click_save = rootView.findViewById(R.id.click_save);
        click_cancel = rootView.findViewById(R.id.click_cancel);
        dialog_layout = rootView.findViewById(R.id.dialog_layout);
        datePicker = rootView.findViewById(R.id.datePicker);
        select_datePicker = rootView.findViewById(R.id.select_datePicker);

        text_date = rootView.findViewById(R.id.text_date);
        text_name = rootView.findViewById(R.id.text_name);
        text_image = rootView.findViewById(R.id.text_image);

        edit_date.setInputType(0);
        InputMethodManager inputManager = (InputMethodManager)container.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        inputManager.hideSoftInputFromWindow(edit_date.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        click_image.setDrawingCacheEnabled(true);
        click_image.buildDrawingCache();

        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        minCal.set(1993, 02, 01);
        datePicker.setMinDate(minCal.getTime().getTime());

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_layout.setVisibility(View.VISIBLE);
            }
        });

        click_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_layout.setVisibility(View.VISIBLE);
            }
        });

        select_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_layout.setVisibility(View.INVISIBLE);
                year = datePicker.getYear();
                month = datePicker.getMonth() + 1;
                day = datePicker.getDayOfMonth();

                edit_date.setText(year + "." + month + "." + day);
            }
        });

        click_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });

        click_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_date.setTextColor(Color.parseColor("#000000"));
                text_name.setTextColor(Color.parseColor("#000000"));
                text_image.setTextColor(Color.parseColor("#000000"));

                if (edit_date.getText().toString().length() == 0 || edit_name.getText().toString().length() == 0 || !isSelectImage) {
                    if (edit_date.getText().toString().length() == 0) text_date.setTextColor(Color.parseColor("#FF0000"));
                    if (edit_name.getText().toString().length() == 0) text_name.setTextColor(Color.parseColor("#FF0000"));
                    if (!isSelectImage) text_image.setTextColor(Color.parseColor("#FF0000"));

                    return;
                }

                //파이어스토어 업로드
                Upload_Item temp = new Upload_Item(UseFunction.upload_list.size() + 1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1
                        , calendar.get(Calendar.DAY_OF_MONTH), year, month, day, edit_name.getText().toString(), edit_name.getText().toString(), "검토중");
                FireBaseFunction.uploadData(temp);
                UseFunction.upload_list.put(UseFunction.upload_list.size() + 1, temp);

                String uriString = getPath(selectUri);
                String fileName = edit_name.getText().toString() + uriString.substring(uriString.length() - 4, uriString.length());

                Log.d("tlqkf", fileName);
                FireBaseFunction.uploadImage(selectUri, fileName);

                ((setUploadClickListener)container.getContext()).clickOnSaveButton(temp);
            }
        });

        click_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((setUploadClickListener)container.getContext()).clickOnCancelButton();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && data != null && data.getData() != null) {
            Uri selectImage = data.getData();
            click_image.setImageURI(selectImage);
            isSelectImage = true;

            selectUri = data.getData();
        }
    }

    public String getPath(Uri uri){
        String[] proj  = {MediaStore.Images.Media.DATA};
        CursorLoader cl = new CursorLoader(getActivity().getApplicationContext(), uri, proj, null, null, null);

        Cursor cursor = cl.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }

    public interface setUploadClickListener{
        public void clickOnSaveButton(Upload_Item item);
        public void clickOnCancelButton();
    }
}