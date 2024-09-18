package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.quanlybanhang.R;
import com.example.quanlybanhang.model.GioHang;
import com.example.quanlybanhang.model.SanPhamMoi;
import com.example.quanlybanhang.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp,giasp,mota;
    Button btnthem;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet);
        anhXa();
        actionToolBar();
        initData();
        initConntrol();
    }

    private void initConntrol() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if(Utils.manggiohang.size() >0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i < Utils.manggiohang.size(); i++ ){
                if(Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasp()) * Utils.manggiohang.get(i).getSoluong();
                    Utils.manggiohang.get(i).setGiasp(gia);
                    flag = true;
                }
            }
            if(flag == false){
                long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                Utils.manggiohang.add(gioHang);
            }
        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            Utils.manggiohang.add(gioHang);
        }
        badge.setText(String.valueOf(tinhTongSoLuongSanPham()));
    }

    private int tinhTongSoLuongSanPham() {
        int total = 0;
        for (GioHang gioHang : Utils.manggiohang) {
            total += gioHang.getSoluong();
        }
        return total;
    }


    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+ decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);

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
        tensp = findViewById(R.id.txttensp);
        giasp = findViewById(R.id.txtgiasp);
        mota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        imghinhanh = findViewById(R.id.imgchitiet);
        spinner = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolBarCT);
        badge = findViewById(R.id.menu_sl);
        FrameLayout frameLayout = findViewById(R.id.framegiohang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
            }
        });
        if(Utils.manggiohang !=null){
            badge.setText(String.valueOf(tinhTongSoLuongSanPham()));
        }

    }

}