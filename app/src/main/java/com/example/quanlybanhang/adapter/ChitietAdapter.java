package com.example.quanlybanhang.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlybanhang.R;
import com.example.quanlybanhang.model.Item;

import java.text.DecimalFormat;
import java.util.List;

public class ChitietAdapter extends RecyclerView.Adapter<ChitietAdapter.MyViewHolder> {

    Context context;
    List<Item> itemList;

    public ChitietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitietdonhang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtdongia.setText(decimalFormat.format(item.getGiasp())+"Đ");
        item.setTonggia(item.getGiasp()*item.getSoluong());
        holder.txttonggia.setText(decimalFormat.format(item.getTonggia())+"Đ");
        holder.txtten.setText(item.getTensp()+"");
        holder.txtsoluong.setText("Số lượng: "+item.getSoluong()+"");

        Glide.with(context).load(item.getHinhanh()).into(holder.imagechitiet);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class MyViewHolder extends  RecyclerView.ViewHolder{

        ImageView imagechitiet;
        TextView txtten,txtsoluong,txtdongia,txttonggia;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitiet = itemView.findViewById(R.id.item_imgchitiet);
            txtten = itemView.findViewById(R.id.item_tensp_chitiet);
            txtsoluong = itemView.findViewById(R.id.item_soluong_chitiet);
            txtdongia = itemView.findViewById(R.id.item_dongia_chitiet);
            txttonggia = itemView.findViewById(R.id.item_tonggia_chitiet);
        }
    }
}
