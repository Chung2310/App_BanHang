package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlybanhang.R;
import com.example.quanlybanhang.model.ThemSPModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    TextInputEditText tsp_ten,tsp_gia,tsp_mota,tsp_hinhanh;
    AppCompatButton btn_them;
    int loai = 0;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ImageView imageView;
    String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_spactivity);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        anhXa();
        initData();
    }

    private void initData() {
        List<String>  stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại sản phẩm");
        stringList.add("Điện Thoại");
        stringList.add("Lattop");
        stringList.add("Thiết bị khác");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themsp();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(ThemSPActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        upLoadFile();

    }

    private void themsp() {
        String str_ten = tsp_ten.getText().toString().trim();
        String str_gia = tsp_gia.getText().toString().trim();
        String str_hinhanh = tsp_hinhanh.getText().toString().trim();
        String str_mota = tsp_mota.getText().toString().trim();
        if(TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_hinhanh) || TextUtils.isEmpty(str_mota) || loai < 1) {
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_LONG).show();
        }
        else {
            compositeDisposable.add(apiBanHang.themsanpham(str_ten,str_gia,str_hinhanh,str_mota,(loai))
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
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    )
            );
        }
    }

    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if(cursor == null) {
            result = uri.getPath();
        }
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void upLoadFile() {
        Uri uri = Uri.parse(mediaPath);  // Lấy đường dẫn từ mediaPath
        File file = new File(getPath(uri));  // Chuyển đổi Uri thành File
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);  // Định dạng file tải lên là image
        MultipartBody.Part fileupload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);  // Chuẩn bị tệp để tải lên

        // Gọi API để tải lên
        Call<ThemSPModel> call = apiBanHang.uploadFile(fileupload);
        call.enqueue(new Callback<ThemSPModel>() {
            @Override
            public void onResponse(Call<ThemSPModel> call, Response<ThemSPModel> response) {
                ThemSPModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        tsp_hinhanh.setText(serverResponse.getName());
                        Toast.makeText(getApplicationContext(), "Tải lên thành công", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ThemSPModel> call, Throwable t) {
                Log.d("Upload error", t.getMessage());  // Hiển thị lỗi nếu không thành công
            }
        });
    }


    private void anhXa() {
        spinner = findViewById(R.id.tsp_spinner);
        tsp_ten = findViewById(R.id.tsp_ten);
        tsp_gia = findViewById(R.id.tsp_gia);
        tsp_hinhanh = findViewById(R.id.tsp_hinhanh);
        tsp_mota = findViewById(R.id.tsp_mota);
        btn_them = findViewById(R.id.tsp_btn);
        imageView = findViewById(R.id.tsp_img);
    }
}