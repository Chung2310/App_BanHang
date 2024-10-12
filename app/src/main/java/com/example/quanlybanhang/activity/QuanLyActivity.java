package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.adapter.SanPhamMoiAdapter;
import com.example.quanlybanhang.model.EventBus.SuaXoaEvent;
import com.example.quanlybanhang.model.SanPhamMoi;
import com.example.quanlybanhang.model.ThemSPModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanLyActivity extends AppCompatActivity {
    ImageView btnThem;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    List<SanPhamMoi> list;
    SanPhamMoiAdapter adapter;
    SanPhamMoi sanPhamSuaXoa;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        anhXa();
        actionToolBar();
        actionButton();
        getSpmoi();
    }

    private void getSpmoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        SanPhamMoiModel -> {
                            if(SanPhamMoiModel.isSuccess()){
                                list = SanPhamMoiModel.getResult();
                                adapter = new SanPhamMoiAdapter(getApplicationContext(),list);
                                recyclerView.setAdapter(adapter);
                            }
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),"Khong ket noi duoc voi sever"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
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
        toolbar = findViewById(R.id.toolBarQL);
        recyclerView =findViewById(R.id.recycleView_QL);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            suaSanPham();
        }
        else if(item.getTitle().equals("Xóa")) {
            xoaSanPham();
        }
        getSpmoi();
        return super.onContextItemSelected(item);
    }

    private void xoaSanPham() {
        compositeDisposable.add(apiBanHang.xoaSP(sanPhamSuaXoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ThemSPModel -> {
                            if(ThemSPModel.isSuccess()){
                                Toast.makeText(getApplicationContext(),ThemSPModel.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),ThemSPModel.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        },throwable -> {
                            Log.d("loi",throwable.getMessage());
                        }
                ));
    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
        intent.putExtra("sua",sanPhamSuaXoa);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void evenSuaXoa(SuaXoaEvent event){
        if(event != null){
            sanPhamSuaXoa = event.getSanPhamMoi();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}