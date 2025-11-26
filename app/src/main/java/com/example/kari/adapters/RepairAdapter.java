package com.example.kari.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kari.EditRepairActivity;
import com.example.kari.models.RepairOrder;
import com.example.kari.R;
import java.util.List;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.ViewHolder> {
    private List<RepairOrder> repairOrders;

    public RepairAdapter(List<RepairOrder> repairOrders) {
        this.repairOrders = repairOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RepairOrder order = repairOrders.get(position);
        holder.tvOwnerName.setText("Имя: " + order.getOwnerName());
        holder.tvPhone.setText("Телефон: " + order.getPhone());
        holder.tvCarModel.setText("Авто: " + order.getCarModel());
        holder.tvDate.setText("Дата: " + order.getDate());
        holder.tvTime.setText("Время: " + order.getTime());
        holder.tvProblem.setText("Проблема: " + order.getProblem());
        holder.tvStatus.setText("Статус: " + order.getStatus());

        // ДОБАВЛЕН ОБРАБОТЧИК КЛИКА ДЛЯ РЕДАКТИРОВАНИЯ
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditRepairActivity.class);
            intent.putExtra("repair_order", order);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return repairOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOwnerName, tvPhone, tvCarModel, tvDate, tvTime, tvProblem, tvStatus;

        public ViewHolder(View view) {
            super(view);
            tvOwnerName = view.findViewById(R.id.tv_owner_name);
            tvPhone = view.findViewById(R.id.tv_phone);
            tvCarModel = view.findViewById(R.id.tv_car_model);
            tvDate = view.findViewById(R.id.tv_date);
            tvTime = view.findViewById(R.id.tv_time);
            tvProblem = view.findViewById(R.id.tv_problem);
            tvStatus = view.findViewById(R.id.tv_status);
        }
    }
}