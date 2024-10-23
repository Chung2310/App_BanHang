package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.adapter.DonHangAdapter;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HoaDonActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppCompatButton btn;
    RecyclerView recyclerView_HD;
    TextView hoadon_ten,hoadon_sdt,hoadon_diachi,hoadon_gia,hoadon_soluong;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);
        Utils.chucnang = "HoaDon";
        anhXa();
        actionButton();
        getData();
        toolbar.setTitle("Hóa Đơn ");

        Long str_tongtien = getIntent().getLongExtra("tongtien",0);
        String str_sdt = getIntent().getStringExtra("sdt");
        String str_diachi = getIntent().getStringExtra("diachi");
        int str_soluong = getIntent().getIntExtra("soluong",0);

        showData(str_sdt,str_tongtien,str_diachi,str_soluong);
    }

    private void getData() {
        compositeDisposable.add(apiBanHang.xemHoaDon(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult());
                            recyclerView_HD.setAdapter(adapter);

                        },
                        throwable -> {
                            Log.d("loisever", throwable.getMessage());
                            Toast.makeText(getApplicationContext(), "Lỗi khi lấy đơn hàng: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void actionButton() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showData(String str_sdt,Long str_tongtien,String str_diachi,int str_soluong) {
        hoadon_ten.setText(Utils.user_current.getName());
        hoadon_sdt.setText(str_sdt);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        hoadon_gia.setText(decimalFormat.format(str_tongtien)+"Đ");
        hoadon_diachi.setText(str_diachi);
        hoadon_soluong.setText(String.valueOf(str_soluong));
    }

    private void anhXa() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolBarHD);
        btn = findViewById(R.id.btn_HD);
        recyclerView_HD = findViewById(R.id.recycleView_HD);
        hoadon_ten = findViewById(R.id.hoadon_ten);
        hoadon_sdt = findViewById(R.id.hoadon_sdt);
        hoadon_diachi = findViewById(R.id.hoadon_diachi);
        hoadon_gia = findViewById(R.id.hoadon_gia);
        hoadon_soluong = findViewById(R.id.hoadon_soluong);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_HD.setLayoutManager(layoutManager);
        recyclerView_HD.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}