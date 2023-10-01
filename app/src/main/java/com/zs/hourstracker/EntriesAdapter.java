package com.zs.hourstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {
    private ArrayList<Entry> dataList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Constructor to initialize the data source
    public EntriesAdapter(Context context, ArrayList<Entry> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize your views here
            // Example:
            // titleTextView = itemView.findViewById(R.id.titleTextView);

            // Set up click listener (if needed)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate your item layout XML here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.date)).setText(dataList.get(position).getDate());
        ((TextView) holder.itemView.findViewById(R.id.hours)).setText(dataList.get(position).getHours() + "");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Define an interface for item click events (if needed)
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Set an item click listener (if needed)
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
