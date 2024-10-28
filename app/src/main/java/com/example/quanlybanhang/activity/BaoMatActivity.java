package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.model.MessageModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BaoMatActivity extends AppCompatActivity {
    Toolbar toolBarBM;
    TextInputEditText pass_cu,pass_moi,pass_moi_moi;
    AppCompatButton btn_doimk;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_mat);
        anhXa();
        actionToolBar();
        actionButton();
    }

    private void actionButton() {
        btn_doimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pass_cu.getText() != null && pass_moi.getText() != null && pass_moi_moi.getText() != null){
                    if(pass_moi.getText().toString().equals(pass_moi_moi.getText().toString())){
                        compositeDisposable.add(apiBanHang.updatepass(Utils.user_current.getId(),pass_cu.getText().toString().trim(),pass_moi.getText().toString().trim())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        MessageModel ->{
                                            Toast.makeText(getApplicationContext(),MessageModel.getMessage(),Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(),ChiTietUserActivity.class);
                                            startActivity(intent);
                                        },throwable -> {
                                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                ));
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Thông tin nhập không trùng khớp",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Hãy nhập đủ thông tin",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void actionToolBar() {
        setSupportActionBar(toolBarBM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarBM.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhXa() {
        toolBarBM = findViewById(R.id.toolBarBM);
        pass_cu = findViewById(R.id.pass_cu);
        pass_moi = findViewById(R.id.pass_moi);
        pass_moi_moi = findViewById(R.id.pass_moi_moi);
        btn_doimk = findViewById(R.id.btn_doimk);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }
}