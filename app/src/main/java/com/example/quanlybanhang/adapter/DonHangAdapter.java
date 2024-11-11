package com.example.quanlybanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.Interface.ItemClickListener;
import com.example.quanlybanhang.R;
import com.example.quanlybanhang.model.DonHang;
import com.example.quanlybanhang.model.EventBus.SuaXoaEventorder;
import com.example.quanlybanhang.model.MessageModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listdonhang;

    public DonHangAdapter(Context context, List<DonHang> listdonhang) {
        this.context = context;
        this.listdonhang = listdonhang;
    }
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        holder.txtdonhang.setText("Đơn hàng: "+donHang.getId());
        LinearLayoutManager linearLayout = new LinearLayoutManager(holder.recyclerViewChitiet.getContext(), LinearLayoutManager.VERTICAL,false);
        linearLayout.setInitialPrefetchItemCount(donHang.getItem().size());
        ChitietAdapter chitietAdapter = new ChitietAdapter(context, donHang.getItem());
        holder.recyclerViewChitiet.setLayoutManager(linearLayout);
        holder.recyclerViewChitiet.setAdapter(chitietAdapter);
        holder.recyclerViewChitiet.setRecycledViewPool(viewPool);
        if(Utils.chucnang.equals("LichSu") || Utils.chucnang.equals("QuanLyOrder")) {
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    compositeDisposable.add(apiBanHang.xoaDH(donHang.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    MessageModel -> {
                                        Toast.makeText(context, MessageModel.getMessage(), Toast.LENGTH_LONG).show();
                                    }, throwable -> {
                                        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG);
                                    }

                            ));
                }
            });
        }else {
            holder.btn_delete.setVisibility(View.INVISIBLE);
        }
        if(Utils.chucnang.equals("QuanLyOrder")){
            if(donHang.getIdtrangthai() == 1){
                holder.btn_xacthuc_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        compositeDisposable.add(apiBanHang.xacthucorder(donHang.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        MessageModel -> {
                                            holder.btn_xacthuc_order.setBackgroundColor(Color.GRAY);
                                            holder.btn_xacthuc_order.setTextColor(Color.WHITE);
                                            holder.btn_xacthuc_order.setText("Đã xác thực");
                                            holder.btn_xacthuc_order.setEnabled(false);
                                            Toast.makeText(context,"Xác nhận đơn hàng thành công",Toast.LENGTH_LONG).show();
                                        },throwable -> {

                                        }
                                ));
                    }
                });
            } else if(donHang.getIdtrangthai() == 2) {
                holder.btn_xacthuc_order.setBackgroundColor(Color.GRAY);
                holder.btn_xacthuc_order.setTextColor(Color.WHITE);
                holder.btn_xacthuc_order.setText("Đã xác thực");
                holder.btn_xacthuc_order.setEnabled(false);
            }
        }
        else {
            if(donHang.getIdtrangthai() == 1){
                holder.btn_xacthuc_order.setBackgroundColor(Color.GRAY);
                holder.btn_xacthuc_order.setTextColor(Color.WHITE);
                holder.btn_xacthuc_order.setText("Chờ xác thực");
                holder.btn_xacthuc_order.setEnabled(false);
            } else if(donHang.getIdtrangthai() == 2) {
                holder.btn_xacthuc_order.setBackgroundColor(Color.GRAY);
                holder.btn_xacthuc_order.setTextColor(Color.WHITE);
                holder.btn_xacthuc_order.setText("Đã xác thực");
                holder.btn_xacthuc_order.setEnabled(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtdonhang;
        RecyclerView recyclerViewChitiet;
        AppCompatButton btn_delete,btn_xacthuc_order;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            recyclerViewChitiet = itemView.findViewById(R.id.recycleView_chitietdonhang);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_xacthuc_order = itemView.findViewById(R.id.btn_xacthuc_order);
        }
    }
}
