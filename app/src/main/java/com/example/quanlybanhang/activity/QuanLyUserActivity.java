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
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.adapter.UserAdapter;
import com.example.quanlybanhang.model.EventBus.SuaXoaEvent;
import com.example.quanlybanhang.model.EventBus.SuaXoaEventUser;
import com.example.quanlybanhang.model.MessageModel;
import com.example.quanlybanhang.model.User;
import com.example.quanlybanhang.model.UserModel;
import com.example.quanlybanhang.model.UsersModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanLyUserActivity extends AppCompatActivity {
    ImageView managerThem;
    Toolbar toolBarQLUser;
    RecyclerView recycleView_QLUser;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<User> list = new ArrayList<>();
    UserAdapter adapter;
    User userSuaXoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_user);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        anhXa();
        actionToolBar();

        getUser();
        actionButton();
    }

    private void actionButton() {
        managerThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QuanLyUserThemActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUser() {
        compositeDisposable.add(apiBanHang.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        UsersModel ->{
                            if (UsersModel.isSuccess()){
                                list = UsersModel.getResult();
                                adapter = new UserAdapter(getApplicationContext(),list);
                                recycleView_QLUser.setAdapter(adapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            suaUser();
        }
        else if(item.getTitle().equals("Xóa")) {
            xoaUser();

        }
        getUser();
        return super.onContextItemSelected(item);
    }

    private void xoaUser() {
        compositeDisposable.add(apiBanHang.xoaUser(userSuaXoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        MessageModel -> {
                            if (MessageModel.isSuccess()){
                                Toast.makeText(getApplicationContext(),MessageModel.getMessage(),Toast.LENGTH_LONG).show();
                                getUser();
                            }
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void actionToolBar() {
        setSupportActionBar(toolBarQLUser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarQLUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QuanLyActivity.class);
                startActivity(intent);
            }
        });

    }

    private void suaUser() {
        Intent intent = new Intent(getApplicationContext(),QuanLyUserThemActivity.class);
        intent.putExtra("userSX",userSuaXoa);
        startActivity(intent);
    }

    private void anhXa() {
        managerThem = findViewById(R.id.managerThemUser);
        toolBarQLUser = findViewById(R.id.toolBarQLUser);
        recycleView_QLUser = findViewById(R.id.recycleView_QLUser);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView_QLUser.setHasFixedSize(true);
        recycleView_QLUser.setLayoutManager(layoutManager);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void evenSuaXoaUser(SuaXoaEventUser event){
        if(event != null){
            userSuaXoa = event.getUser();
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
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