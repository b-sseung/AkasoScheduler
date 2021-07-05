package com.sseung.akasoschedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class List_Adapter extends RecyclerView.Adapter<List_Adapter.ViewHolder> implements OnListItemClickListener {

    ArrayList<Schedule_Item> items = new ArrayList<>();
    OnListItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_recyclerview, parent, false);

        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule_Item item = items.get(position);
        holder.setItem(item);

        if (position != 0) {
            Schedule_Item temp = items.get(position - 1);
            if (temp.getYear() == item.getYear() && temp.getMonth() == item.getMonth() && temp.getDay() == item.getDay()) {
                holder.list_recycler_day.setVisibility(View.INVISIBLE);
                holder.list_recycler_year.setVisibility(View.INVISIBLE);
                holder.list_background.setImageResource(R.drawable.list_background2);
            }
        }
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

    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView list_recycler_day, list_recycler_year, list_recycler_name, list_recycler_time;
        ImageView list_background;

        public ViewHolder(View itemView, final OnListItemClickListener listener){
            super(itemView);

            list_background = itemView.findViewById(R.id.list_background);

            list_recycler_day = itemView.findViewById(R.id.list_recycler_day);
            list_recycler_year = itemView.findViewById(R.id.list_recycler_year);
            list_recycler_name = itemView.findViewById(R.id.list_recycler_name);
            list_recycler_time = itemView.findViewById(R.id.list_recycler_time);

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

            list_recycler_day.setText(item.getDay() + "일");
            list_recycler_year.setText(item.getYear() + ". " + item.getMonth());
            list_recycler_name.setText(item.getName());

            if (item.getTime().length() > 0) {
                list_recycler_time.setText(item.getTime());
            } else {
                if (item.getDetail().equals("사진") || item.getDetail().equals("비디오")) {
                    list_recycler_time.setText(item.getSource());
                } else if (item.getDetail().equals("잡지")) {
                    list_recycler_time.setText(item.getSale());
                }
            }

        }
    }
}
