package com.example.quanlybanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.adapter.ChiTietDonHangAdapter;
import com.example.quanlybanhang.model.DonHang;
import com.example.quanlybanhang.model.Item2;
import com.example.quanlybanhang.model.Item2Model;
import com.example.quanlybanhang.model.User;
import com.example.quanlybanhang.model.UserModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChitietDonHangActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView ctdh_id_user,ctdh_iddonhang_user,ctdh_name_user,ctdh_email_user,ctdh_diachi_user,ctdh_tongsl_user,ctdh_tongtien_user,ctdh_sdt_user;
    DonHang donHang;
    List<Item2> list;
    User user = new User();
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_don_hang);
        anhXa();
        getItem();
        getUserbyId();
        actionToolBar();
        initData();

    }

    private void getItem() {
        compositeDisposable.add(apiBanHang.getItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        Item2Model ->{
                            list = Item2Model.getResult();
                            ChiTietDonHangAdapter adapter = new ChiTietDonHangAdapter(getApplicationContext(),list);
                            recyclerView.setAdapter(adapter);
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getUserbyId() {
        compositeDisposable.add(apiBanHang.getUser(donHang.getIduser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        UserModel -> {
                            user = UserModel.getResult();
                            Toast.makeText(getApplicationContext(),UserModel.getMessage(),Toast.LENGTH_LONG).show();
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void initData() {
        donHang = (DonHang) getIntent().getSerializableExtra("chitietdh");
        ctdh_id_user.setText(donHang.getId());
        ctdh_iddonhang_user.setText(donHang.getIduser());
        ctdh_diachi_user.setText(donHang.getDiachi());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        ctdh_tongtien_user.setText("Giá: "+ decimalFormat.format(Double.parseDouble(donHang.getTongtien())+"Đ"));
        ctdh_sdt_user.setText(donHang.getSodienthoai());
        int soluong=0;
        for(int i = 0;i<list.size();i++){
            soluong = soluong + list.get(i).getSoluong();
        }
        ctdh_tongsl_user.setText(String.valueOf(soluong));
        ctdh_name_user.setText(user.getName().toString());
        ctdh_email_user.setText(user.getEmail().toString());


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
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolBarCTDH);
        recyclerView = findViewById(R.id.recycleView_chitietdh);
        ctdh_id_user = findViewById(R.id.ctdh_id_user);
        ctdh_iddonhang_user = findViewById(R.id.ctdh_iddonhang_user);
        ctdh_name_user = findViewById(R.id.ctdh_name_user);
        ctdh_email_user = findViewById(R.id.ctdh_email_user);
        ctdh_diachi_user = findViewById(R.id.ctdh_diachi_user);
        ctdh_tongsl_user = findViewById(R.id.ctdh_tongsl_user);
        ctdh_tongtien_user = findViewById(R.id.ctdh_tongtien_user);
        ctdh_sdt_user = findViewById(R.id.ctdh_sdt_user);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }
}