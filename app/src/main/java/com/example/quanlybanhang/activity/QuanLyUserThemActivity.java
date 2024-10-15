package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.example.quanlybanhang.model.User;
import com.example.quanlybanhang.model.UserModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanLyUserThemActivity extends AppCompatActivity {
    Toolbar toolBarTND;
    TextInputEditText tnd_ten,tnd_email,tnd_pass,tnd_sdt;
    Spinner tnd_spinner;
    AppCompatButton tnd_btn;
    String chucvu;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean flag = false;
    User userSuaXoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_user_them);

        anhXa();
        actionToolBar();
        initData();

        Intent intent = getIntent();
        userSuaXoa = (User) intent.getSerializableExtra("userSX");
        if(userSuaXoa == null){
            flag = false;
        }
        else {
            flag = true;
            tnd_btn.setText("Sửa");
            tnd_email.setText(userSuaXoa.getEmail());
            tnd_pass.setText(userSuaXoa.getPass());
            tnd_sdt.setText(userSuaXoa.getSdt());
            tnd_ten.setText(userSuaXoa.getName());
            if(userSuaXoa.getChucvu().equals("user")){
                tnd_spinner.setSelection(1);
            }
            else {
                tnd_spinner.setSelection(2);
            }

        }
    }

    private void initData() {
        List<String>  stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại sản phẩm");
        stringList.add("user");
        stringList.add("admin");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,stringList);
        tnd_spinner.setAdapter(adapter);
        tnd_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1) {
                    chucvu = "user";
                }
                else {
                    chucvu = "admin";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tnd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag == false){
                    themuser();
                }
                else {
                    suauser();
                }
            }
        });
    }

    private void suauser() {
        int id = userSuaXoa.getId();
        String str_ten = tnd_ten.getText().toString().trim();
        String str_email = tnd_email.getText().toString().trim();
        String str_pass = tnd_pass.getText().toString().trim();
        String str_sdt = tnd_sdt.getText().toString().trim();
        if(TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_pass) || TextUtils.isEmpty(str_sdt) || TextUtils.isEmpty(chucvu)) {
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_LONG).show();
        }
        else {
            compositeDisposable.add(apiBanHang.suauser(id,str_email,str_pass,str_ten,str_sdt,chucvu)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            MessageModel -> {
                                if(MessageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),MessageModel.getMessage(),Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),QuanLyUserActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),MessageModel.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            },throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    )
            );
        }
    }


    private void themuser() {
        String str_ten = tnd_ten.getText().toString().trim();
        String str_email = tnd_email.getText().toString().trim();
        String str_pass = tnd_pass.getText().toString().trim();
        String str_sdt = tnd_sdt.getText().toString().trim();
        if(TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_pass) || TextUtils.isEmpty(str_sdt) || TextUtils.isEmpty(chucvu)) {
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_LONG).show();
        }
        else {
            xacThucTT(str_email, isValid -> {
                if (isValid) {
                    // Tiến hành đăng ký
                    compositeDisposable.add(apiBanHang.themuser(str_email, str_pass, str_ten, str_sdt, chucvu)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    UserModel -> {
                                        if (UserModel.isSuccess()) {
                                            Intent intent = new Intent(getApplicationContext(), QuanLyUserActivity.class);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(getApplicationContext(), UserModel.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                            ));
                } else {
                    Toast.makeText(getApplicationContext(), "Email không tồn tại", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    
    private void actionToolBar() {
        setSupportActionBar(toolBarTND);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarTND.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void xacThucTT(String email, DangKiActivity.EmailValidationCallback callback) {
        compositeDisposable.add(apiBanHang.xacthuc(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()) {
                                callback.onValidationResult(true);
                            } else {
                                callback.onValidationResult(false);
                                Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            callback.onValidationResult(false);
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    interface EmailValidationCallback {
        void onValidationResult(boolean isValid);
    }

    private void anhXa() {
        toolBarTND = findViewById(R.id.toolBarTND);
        tnd_ten = findViewById(R.id.tnd_ten);
        tnd_email = findViewById(R.id.tnd_email);
        tnd_pass = findViewById(R.id.tnd_pass);
        tnd_sdt = findViewById(R.id.tnd_sdt);
        tnd_spinner = findViewById(R.id.tnd_spinner);
        tnd_btn = findViewById(R.id.tnd_btn);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}