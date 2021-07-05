package com.sseung.akasoschedule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;


public class ClickItemFragment extends Fragment {


    TextView nameText, dateText, closeText;
    ImageView imageView, move_message;
    boolean value = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.click_schedule_activity, container, false);

        nameText = rootView.findViewById(R.id.click_name_text);
        dateText = rootView.findViewById(R.id.click_date_text);
        closeText = rootView.findViewById(R.id.click_close_text);
        imageView = rootView.findViewById(R.id.click_image);
        move_message = rootView.findViewById(R.id.move_message);

        Bundle bundle = getArguments();

        int year = bundle.getInt("year");
        int month = bundle.getInt("month");
        int day = bundle.getInt("day");
        String division = bundle.getString("division");
        String detail = bundle.getString("detail");
        String name = bundle.getString("name");
        String time = bundle.getString("time");
        String image = bundle.getString("image");
        String uri = bundle.getString("uri");
        String sale = bundle.getString("sale");
        String source = bundle.getString("source");

        Log.d("tiqkf", year + ", " + month  + ", " + day + ", " + division + ", " + detail + ", " + name + ", " + time + ", " + image + ", " + uri + ", " + sale + ", " + source);

        nameText.setText(name);

        String date = year + "년 " + month + "월 " + day + "일 " + time;

        dateText.setText(date);

//        if (image != 0) imageView.setImageResource(image);

        if (NetworkStatus.getConnectivityStatus(UseFunction.mainContext) == 3){
            imageView.setImageResource(R.drawable.no_internet_image);
            value = false;
        } else {
            value = true;
            imageView.setImageResource(R.drawable.image_guide_background);
            FireBaseFunction.readImage(getActivity().getApplicationContext(), imageView, image);

            AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
            anim.setDuration(1000);
            anim.setStartOffset(500);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(3);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    move_message.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            if (uri != null){
                move_message.setVisibility(View.VISIBLE);
                move_message.startAnimation(anim);
            }


        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uri.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });

        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((setClickCloseText)container.getContext()).onClickCloseText();
            }
        });

        return rootView;
    }

    public interface setClickCloseText{
        public void onClickCloseText();
    }
}
