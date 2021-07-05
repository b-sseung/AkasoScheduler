package com.sseung.akasoschedule;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Schedule_Adapter extends RecyclerView.Adapter<Schedule_Adapter.ViewHolder> implements OnScheduleItemClickListener {

    ArrayList<Schedule_Item> items = new ArrayList<>();
    OnScheduleItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.calendar_recyclerview, parent, false);

        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule_Item item = items.get(position);

        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Schedule_Item item){
        items.add(item);
    }

    public void setItems(ArrayList<Schedule_Item> items){
        this.items = items;
    }

    public Schedule_Item getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Schedule_Item item){
        items.set(position, item);
    }

    public void setOnItemClickListener(OnScheduleItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView detail_division;
        TextView name;
        TextView time;
        FrameLayout calendar_setLayout;
        ImageView calendar_background_image;

        public ViewHolder(View itemView, final OnScheduleItemClickListener listener){
            super(itemView);

            detail_division = itemView.findViewById(R.id.detail_text);
            name = itemView.findViewById(R.id.name_text);
            time = itemView.findViewById(R.id.start_text);
            calendar_setLayout = itemView.findViewById(R.id.calendar_setLayout);
            calendar_background_image = itemView.findViewById(R.id.calendar_background_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(Schedule_Item item){

            detail_division.setText(item.getDetail());
            name.setText(item.getName());

            Log.d("tlqkf", "item test : " + item.getTime() + ", " + item.getDetail() + ", " + item.getSource() + ", " + item.getSale());
            if (item.getTime().length() > 0) {
                Log.d("tlqkf", "true");
                time.setText(item.getTime());
            } else {
                Log.d("tlqkf", "false");
                if (item.getDetail().equals("사진") || item.getDetail().equals("비디오")) {
                    Log.d("tlqkf", "source");
                    time.setText(item.getSource());
                } else if (item.getDetail().equals("잡지")) {
                    Log.d("tlqkf", "sale");
                    time.setText(item.getSale());
                }
            }

        }
    }

}
