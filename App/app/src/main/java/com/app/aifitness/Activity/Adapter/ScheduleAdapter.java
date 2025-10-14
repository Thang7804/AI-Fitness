package com.app.aifitness.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.aifitness.R;
import com.app.aifitness.Activity.DayDetailActivity;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.DayViewHolder> {

    private List<String> dayList;
    private Context context;

    public ScheduleAdapter(Context context, List<String> dayList) {
        this.context = context;
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.scheduleday, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        String day = dayList.get(position);
        holder.tvDayName.setText(day);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DayDetailActivity.class);
            intent.putExtra("dayName", day);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayName;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.tvDayName);
        }
    }
}
