package com.zs.hourstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {
    private final ArrayList<Entry> dataList;
    private OnItemClickListener onItemClickListener;

    public EntriesAdapter(ArrayList<Entry> dataList) {
        this.dataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView hours;

        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            hours = itemView.findViewById(R.id.hours);

            itemView.setOnClickListener(v -> {
                onItemClickListener.onItemClick(getAdapterPosition());
            });

            itemView.setOnLongClickListener(v -> {
                onItemClickListener.onItemClick(getAdapterPosition());

                return true;
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.date.setText(dataList.get(position).getDate());
        holder.hours.setText(dataList.get(position).getHours() + "");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
