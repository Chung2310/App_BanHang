package com.example.quanlybanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.Interface.ItemClickListener;
import com.example.quanlybanhang.R;
import com.example.quanlybanhang.model.DonHang;
import com.example.quanlybanhang.model.EventBus.SuaXoaEventorder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listdonhang;

    public DonHangAdapter(Context context, List<DonHang> listdonhang) {
        this.context = context;
        this.listdonhang = listdonhang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        holder.txtdonhang.setText("Đơn hàng: "+donHang.getId());
        LinearLayoutManager linearLayout = new LinearLayoutManager(holder.recyclerViewChitiet.getContext(), LinearLayoutManager.VERTICAL,false);
        linearLayout.setInitialPrefetchItemCount(donHang.getItem().size());
        ChitietAdapter chitietAdapter = new ChitietAdapter(context, donHang.getItem());
        holder.recyclerViewChitiet.setLayoutManager(linearLayout);
        holder.recyclerViewChitiet.setAdapter(chitietAdapter);
        holder.recyclerViewChitiet.setRecycledViewPool(viewPool);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(context, ChitietDonHangActivity.class);
                    intent.putExtra("chitietdh",donHang);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    EventBus.getDefault().postSticky(new SuaXoaEventorder(donHang));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener{
        TextView txtdonhang;
        RecyclerView recyclerViewChitiet;
        private ItemClickListener itemClickListener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            recyclerViewChitiet = itemView.findViewById(R.id.recycleView_chitietdonhang);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 0, getAdapterPosition(), "Xóa");
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return false;
        }

    }

}
