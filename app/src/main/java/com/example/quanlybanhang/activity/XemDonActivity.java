package com.example.quanlybanhang.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.adapter.DonHangAdapter;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiBanHang apiBanHang;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);

        anhXa();
        initToolBar();
        getOrder();
    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            if (donHangModel.getResult() != null && !donHangModel.getResult().isEmpty()) {
                                Log.d("json", donHangModel.toString());
                                Log.d("tinnhan",donHangModel.getMessage());
                                DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult());
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(XemDonActivity.this, "Không có đơn hàng nào", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.d("loisever", throwable.getMessage());
                            Toast.makeText(XemDonActivity.this, "Lỗi khi lấy đơn hàng: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhXa() {
        recyclerView = findViewById(R.id.recycleViewXD);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        toolbar = findViewById(R.id.toolBarXD);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
