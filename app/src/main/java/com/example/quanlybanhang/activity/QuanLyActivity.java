package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlybanhang.R;

public class QuanLyActivity extends AppCompatActivity {
    AppCompatButton btnThem,btnSua,btnXoa;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        anhXa();
        actionToolBar();
        actionButton();
    }

    private void actionButton() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
                startActivity(intent);
            }
        });
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhXa() {
        btnThem = findViewById(R.id.managerThem);
        btnSua = findViewById(R.id.managerSua);
        btnXoa = findViewById(R.id.managerXoa);
        toolbar = findViewById(R.id.toolBarQL);
    }
}