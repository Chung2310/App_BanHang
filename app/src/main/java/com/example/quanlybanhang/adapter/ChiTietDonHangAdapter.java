package com.example.quanlybanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlybanhang.R;
import com.example.quanlybanhang.model.Item2;

import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.MyViewHolder> {
    Context context;
    List<Item2> list;

    public ChiTietDonHangAdapter(Context context, List<Item2> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitietdonhang2,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item2 item = list.get(position);
        holder.item_tensp_chitiet2.setText(item.getTensp());
        holder.item_soluong_chitiet2.setText(item.getSoluong());
        holder.item_gia_chitiet2.setText(String.valueOf(item.getGia()));
        holder.item_mota_chitiet2.setText(item.getMota());
        Glide.with(context).load(item.getHinhanh()).into(holder.item_imgchitiet2);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView item_tensp_chitiet2,item_soluong_chitiet2,item_gia_chitiet2,item_mota_chitiet2;
        ImageView item_imgchitiet2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_tensp_chitiet2 = itemView.findViewById(R.id.item_tensp_chitiet2);
            item_soluong_chitiet2 = itemView.findViewById(R.id.item_soluong_chitiet2);
            item_gia_chitiet2 = itemView.findViewById(R.id.item_gia_chitiet2);
            item_mota_chitiet2 = itemView.findViewById(R.id.item_mota_chitiet2);
            item_imgchitiet2 = itemView.findViewById(R.id.item_imgchitiet2);
        }
    }
}
