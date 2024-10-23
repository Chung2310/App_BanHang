package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.adapter.DonHangAdapter;
import com.example.quanlybanhang.model.DonHang;
import com.example.quanlybanhang.model.DonHangModel;
import com.example.quanlybanhang.model.EventBus.SuaXoaEventorder;
import com.example.quanlybanhang.model.MessageModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanLyOrderActivity extends AppCompatActivity {
    RecyclerView recycleView_QLDH;
    Toolbar toolBarQLDH;
    DonHangAdapter adapter;
    ApiBanHang apiBanHang;
    List<DonHang> listOrder;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    DonHangAdapter adapterDH;
    DonHang donHangSuaXoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.chucnang = "QuanLyOrder";
        setContentView(R.layout.activity_quan_ly_order);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        anhXa();
        getOrder();
        actionToolBar();
    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.getOrder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        DonHangModel ->{
                            if (DonHangModel.isSuccess()){
                                listOrder = DonHangModel.getResult();
                                adapter = new DonHangAdapter(getApplicationContext(),listOrder);
                                recycleView_QLDH.setAdapter(adapter);
                            }
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }

                ));
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Xóa")) {
            xoaDonHang();
        }
        getOrder();
        return super.onContextItemSelected(item);
    }


    private void xoaDonHang() {
        compositeDisposable.add(apiBanHang.xoaDH(donHangSuaXoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        MessageModel -> {
                            Toast.makeText(getApplicationContext(),MessageModel.getMessage(),Toast.LENGTH_LONG).show();
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }

                ));
    }

    private void actionToolBar() {
        setSupportActionBar(toolBarQLDH);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarQLDH.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QuanLyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {

        recycleView_QLDH = findViewById(R.id.recycleView_QLDH);
        toolBarQLDH = findViewById(R.id.toolBarQLDH);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView_QLDH.setHasFixedSize(true);
        recycleView_QLDH.setLayoutManager(layoutManager);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void evenSuaXoaOrder(SuaXoaEventorder event){
        if(event != null){
            donHangSuaXoa = event.getDonHang();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}