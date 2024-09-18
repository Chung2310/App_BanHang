package com.example.quanlybanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.adapter.GioHangAdapter;
import com.example.quanlybanhang.utils.Utils;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong;
    Toolbar toolbar;
    RecyclerView  recyclerView;
    TextView tongtien;
    Button btnmuahang;
    GioHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gio_hang);
        anhXa();
        initControl();
        tinhtongtien();
    }

    private void tinhtongtien() {
        long tongtiensp = 0;
        for(int i=0;i<Utils.manggiohang.size();i++){
            tongtiensp = tongtiensp + (Utils.manggiohang.get(i).getGiasp()*Utils.manggiohang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp)+"Đ");
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

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.manggiohang.size() == 0) {
            giohangtrong.setVisibility(View.VISIBLE);
            tongtien.setText("");
        }
        else {
            adapter = new GioHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(adapter);

        }
    }

    private void anhXa() {
        giohangtrong = findViewById(R.id.txtgiohangtrong);
        toolbar = findViewById(R.id.toolBarGH);
        recyclerView = findViewById(R.id.recycleViewGH);
        tongtien = findViewById(R.id.txttongtien);
        btnmuahang = findViewById(R.id.btnmuahang);
    }
}