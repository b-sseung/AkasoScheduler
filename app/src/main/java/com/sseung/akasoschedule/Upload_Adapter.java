package com.sseung.akasoschedule;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class Upload_Adapter extends RecyclerView.Adapter<Upload_Adapter.ViewHolder> implements OnUploadItemClickListener {

    ArrayList<Upload_Item> items = new ArrayList<>();
    OnUploadItemClickListener listener;

    @NonNull
    @Override
    public Upload_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.upload_recyclerview, parent, false);

        return new Upload_Adapter.ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Upload_Adapter.ViewHolder holder, int position) {
        Upload_Item item = items.get(position);

        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Upload_Item item){
        items.add(item);
    }

    public void setItems(ArrayList<Upload_Item> items){
        this.items = items;
    }

    public Upload_Item getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Upload_Item item){
        items.set(position, item);
    }

    public void setOnItemClickListener(OnUploadItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(Upload_Adapter.ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView upload_date, upload_name, upload_state;

        public ViewHolder(View itemView, final OnUploadItemClickListener listener){
            super(itemView);

            upload_date = itemView.findViewById(R.id.upload_date);
            upload_name = itemView.findViewById(R.id.upload_name);
            upload_state = itemView.findViewById(R.id.upload_state);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(Upload_Adapter.ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(Upload_Item item){

            upload_date.setText(item.getUploadYY() + "." + item.getUploadMM() + "." + item.getUploadDD());
            upload_name.setText(item.getName());
            upload_state.setText(item.getState());
        }
    }

}

