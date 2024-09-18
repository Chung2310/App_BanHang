package com.example.quanlybanhang.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlybanhang.R;
import com.example.quanlybanhang.adapter.LoaiSanPhamAdapter;
import com.example.quanlybanhang.adapter.SanPhamMoiAdapter;
import com.example.quanlybanhang.model.GioHang;
import com.example.quanlybanhang.model.Loaisp;
import com.example.quanlybanhang.model.SanPhamMoi;
import com.example.quanlybanhang.model.SanPhamMoiModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;




public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewMHC;
    NavigationView navigationView;
    ListView listViewMHC;
    DrawerLayout drawerLayout;
    LoaiSanPhamAdapter loaiSanPhamAdapter;
    List<Loaisp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpmoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        anhXa();
        ActionBar();

        if(isConnected(this)){
            getSpmoi();
            ActionViewFlipper();
            getLoaiSanPham();
            getEvenClick();
        }
        else{
            Toast.makeText(getApplicationContext(),"Không có Internet, vui lòng kết nối", Toast.LENGTH_LONG).show();
        }
    }

    private void getEvenClick() {
        listViewMHC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent laptop = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 2:
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                }
            }
        });
    }

    private void getSpmoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                SanPhamMoiModel -> {
                    if(SanPhamMoiModel.isSuccess()){
                        mangSpmoi = SanPhamMoiModel.getResult();
                        spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpmoi);
                        recyclerViewMHC.setAdapter(spAdapter);
                    }
                },throwable -> {
                    Toast.makeText(getApplicationContext(),"Khong ket noi duoc voi sever"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                }
        ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loaiSpModel -> {
                    if (loaiSpModel.isSuccess()) {
                        mangloaisp = loaiSpModel.getResult();
                        loaiSanPhamAdapter = new LoaiSanPhamAdapter(getApplicationContext(), mangloaisp);
                        listViewMHC.setAdapter(loaiSanPhamAdapter);
                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi dữ liệu", Toast.LENGTH_SHORT).show();
                    }

                })
        );
    }



    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void anhXa() {
        toolbar = findViewById(R.id.toolbarMHC);
        viewFlipper = findViewById(R.id.viewflipperMHC);
        recyclerViewMHC = findViewById(R.id.recycleViewMHC);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewMHC.setLayoutManager(layoutManager);
        recyclerViewMHC.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationviewMHC);
        listViewMHC = findViewById(R.id.listviewMHC);
        drawerLayout = findViewById(R.id.drawerlayout);
        mangloaisp = new ArrayList<>();
        mangSpmoi = new ArrayList<>();
        badge = findViewById(R.id.menu_sl_main);
        frameLayout = findViewById(R.id.framegiohangmain);
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }
        else {
            badge.setText(String.valueOf(tinhTongSoLuongSanPham()));

        }

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        badge.setText(String.valueOf(tinhTongSoLuongSanPham()));
    }

    private int tinhTongSoLuongSanPham() {
        int total = 0;
        for (GioHang gioHang : Utils.manggiohang) {
            total += gioHang.getSoluong();
        }
        return total;
    }
    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);


            if (networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}