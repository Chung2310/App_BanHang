package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    TextInputEditText txtemailDK,txtpassDK,txtpassxacthuc,txtsdt,txtusername;
    AppCompatButton btnDangKi;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        anhXa();
        initControl();

    }

    private void initControl() {
        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });
    }

    private void dangKi() {
        String str_email = txtemailDK.getText().toString().trim();
        String str_pass = txtpassDK.getText().toString().trim();
        String str_repass = txtpassxacthuc.getText().toString().trim();
        String str_sdt = txtsdt.getText().toString().trim();
        String str_user = txtusername.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(),"Hãy nhập email của bạn",Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(),"Hãy nhập mật khẩu của bạn",Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(),"Hãy xác thực lại mật khẩu của bạn",Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(str_sdt)){
            Toast.makeText(getApplicationContext(),"Hãy nhập số điện thoại của bạn",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(str_sdt)){
            Toast.makeText(getApplicationContext(),"Hãy nhập họ và tên người dùng",Toast.LENGTH_LONG).show();
        } else {
            if(str_pass.equals(str_repass)){
                compositeDisposable.add(apiBanHang.dangKi(str_email,str_pass,str_user,str_sdt)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()){
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setPass(str_pass);

                                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }, throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                }
                        ));
            }else {
                Toast.makeText(getApplicationContext(),"Mật khẩu của bạn chưa trùng khớp",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void anhXa() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtemailDK = findViewById(R.id.txtemailDK);
        txtpassDK = findViewById(R.id.txtpassDK);
        txtpassxacthuc = findViewById(R.id.txtpassxacnhan);
        btnDangKi =findViewById(R.id.btnDangKy);
        txtsdt = findViewById(R.id.txtsdtxacthuc);
        txtusername = findViewById(R.id.txtusername);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}