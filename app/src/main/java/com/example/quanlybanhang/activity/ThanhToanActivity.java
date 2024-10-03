package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTongTien,txtSDT,txtEmail;
    EditText edtDiaChi;
    AppCompatButton btnThanhToan;
    CompositeDisposable compositeDisposable;
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        anhXa();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0;
        for(int i=0;i < Utils.manggiohang.size();i++){
            totalItem += Utils.manggiohang.get(i).getSoluong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txtTongTien.setText(decimalFormat.format(tongtien)+"Đ");
        txtEmail.setText(Utils.user_current.getEmail());
        txtSDT.setText(Utils.user_current.getSdt());


        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtDiaChi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"Hãy nhập địa chỉ nhận hàng của bạn",Toast.LENGTH_LONG).show();
                }
                else {
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getSdt();
                    int id = Utils.user_current.getId();



                    compositeDisposable.add(apiBanHang.createOrder(str_sdt,str_email,String.valueOf(tongtien),id,str_diachi,totalItem,new Gson().toJson(Utils.manggiohang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if (userModel.isSuccess()) {
                                            Toast.makeText(getApplicationContext(), "Thanh toán thành công", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Thanh toán thất bại: " + userModel.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    throwable -> {

                                        Toast.makeText(getApplicationContext(), "Lỗi kết nối: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                            ));

                }
            }
        });
    }

    private void anhXa() {
        compositeDisposable = new CompositeDisposable();
        txtTongTien = findViewById(R.id.tongTienTT);
        txtEmail = findViewById(R.id.emailTT);
        txtSDT = findViewById(R.id.sodienthoaiTT);
        edtDiaChi = findViewById(R.id.diaChiTT);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        toolbar = findViewById(R.id.toolBarTT);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
