package com.example.kari.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kari.EditDetailingActivity;
import com.example.kari.models.DetailingOrder;
import com.example.kari.R;
import java.util.List;

public class DetailingAdapter extends RecyclerView.Adapter<DetailingAdapter.ViewHolder> {
    private List<DetailingOrder> detailingOrders;

    public DetailingAdapter(List<DetailingOrder> detailingOrders) {
        this.detailingOrders = detailingOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detailing_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailingOrder order = detailingOrders.get(position);
        holder.tvOwnerName.setText("Имя: " + order.getOwnerName());
        holder.tvPhone.setText("Телефон: " + order.getPhone());
        holder.tvEmail.setText("Email: " + order.getEmail());
        holder.tvDeviceModel.setText("Устройство: " + order.getDeviceModel());
        holder.tvDate.setText("Дата: " + order.getDate());
        holder.tvTime.setText("Время: " + order.getTime());
        holder.tvStatus.setText("Статус: " + order.getStatus());

        // ДОБАВЛЕН ОБРАБОТЧИК КЛИКА ДЛЯ РЕДАКТИРОВАНИЯ
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditDetailingActivity.class);
            intent.putExtra("detailing_order", order);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return detailingOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOwnerName, tvPhone, tvEmail, tvDeviceModel, tvDate, tvTime, tvStatus;

        public ViewHolder(View view) {
            super(view);
            tvOwnerName = view.findViewById(R.id.tv_owner_name);
            tvPhone = view.findViewById(R.id.tv_phone);
            tvEmail = view.findViewById(R.id.tv_email);
            tvDeviceModel = view.findViewById(R.id.tv_device_model);
            tvDate = view.findViewById(R.id.tv_date);
            tvTime = view.findViewById(R.id.tv_time);
            tvStatus = view.findViewById(R.id.tv_status);
        }
    }
}