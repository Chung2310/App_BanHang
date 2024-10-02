package com.example.quanlybanhang.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTongTien,txtSDT,txtEmail;
    EditText edtDiaChi;
    AppCompatButton btnThanhToan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        anhXa();
        initControl();
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
        long tongtien = getIntent().getLongExtra("tongtien",0);
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
                    Log.d("test", new Gson().toJson(Utils.manggiohang));
                }
            }
        });
    }

    private void anhXa() {
        txtTongTien = findViewById(R.id.tongTienTT);
        txtEmail = findViewById(R.id.emailTT);
        txtSDT = findViewById(R.id.sodienthoaiTT);
        edtDiaChi = findViewById(R.id.diaChiTT);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        toolbar = findViewById(R.id.toolBarTT);
    }
}