package com.example.quanlybanhang.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.quanlybanhang.R;
import com.example.quanlybanhang.model.MessageModel;
import com.example.quanlybanhang.model.ThemSPModel;
import com.example.quanlybanhang.retrofit.ApiBanHang;
import com.example.quanlybanhang.retrofit.RetrofitClient;
import com.example.quanlybanhang.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietUserActivity extends AppCompatActivity {
    Toolbar toolBarCTUser;
    ImageView CTuser_baomat, CTuser_name_btn, CTuser_email_btn, CTuser_sdt_btn;
    CircleImageView profile_image_CTuser;
    EditText CTuser_name, CTuser_email, CTuser_sdt;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mediaPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_user);
        anhXa();
        actionToolBar();
        actionButton();
        showInfo();
    }

    private void showInfo(){
        CTuser_name.setText(Utils.user_current.getName());
        CTuser_email.setText(Utils.user_current.getEmail());
        CTuser_sdt.setText(Utils.user_current.getSdt());

        if(Utils.user_current.getAvatar().contains("https")){
            Glide.with(getApplicationContext()).load(Utils.user_current.getAvatar()).into(profile_image_CTuser);
        }
        else {
            String hinh = Utils.BASE_URL+"imagesavt/"+Utils.user_current.getAvatar();
            Glide.with(getApplicationContext()).load(hinh).into(profile_image_CTuser);
        }
    }

    private void actionButton() {
        CTuser_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suaTT();
            }
        });
        CTuser_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suaEmail();
            }
        });
        CTuser_sdt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suaTT();
            }
        });
        CTuser_baomat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BaoMatActivity.class);
                startActivity(intent);
            }
        });
        profile_image_CTuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ChiTietUserActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .start();
            }
        });
    }

    private void suaEmail() {
        String str_email = CTuser_email.getText().toString();
        xacThucTT(str_email, isValid -> {
            if (isValid) {
                // Tiến hành đăng ký
                compositeDisposable.add(apiBanHang.suaemail(Utils.user_current.getId(), str_email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                MessageModel -> {
                                    if (MessageModel.isSuccess()) {
                                        Utils.user_current.setEmail(str_email);
                                        Paper.book().write("user", Utils.user_current);
                                    } else {
                                        Toast.makeText(getApplicationContext(), MessageModel.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            mediaPath = data.getDataString();
            upLoadFile();
        } else {
            Toast.makeText(this, "Không có hình ảnh nào được chọn", Toast.LENGTH_SHORT).show();
        }
    }

    private void upLoadFile() {
        String id = String.valueOf(Utils.user_current.getId());
        Uri uri = Uri.parse(mediaPath);  // Lấy đường dẫn từ mediaPath
        File file = new File(getPath(uri));  // Chuyển đổi Uri thành File
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);  // Định dạng file tải lên là image
        MultipartBody.Part fileupload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);  // Chuẩn bị tệp để tải lên

        // Gọi API để tải lên
        Call<ThemSPModel> call = apiBanHang.uploadFileAvt(fileupload, id);  // Gửi ID người dùng
        call.enqueue(new Callback<ThemSPModel>() {
            @Override
            public void onResponse(Call<ThemSPModel> call, Response<ThemSPModel> response) {
                ThemSPModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        Utils.user_current.setAvatar(serverResponse.getName());
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Không có phản hồi từ máy chủ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ThemSPModel> call, Throwable t) {
                Log.d("Upload error", t.getMessage());  // Hiển thị lỗi nếu không thành công
                Toast.makeText(getApplicationContext(), "Lỗi tải lên: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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

    private void suaTT() {
        String str_name = CTuser_name.getText().toString();
        String str_sdt = CTuser_sdt.getText().toString();
        compositeDisposable.add(apiBanHang.suauseruser(Utils.user_current.getId(), str_name , str_sdt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        MessageModel -> {
                            if (MessageModel.isSuccess()) {
                                Utils.user_current.setName(str_name);
                                Utils.user_current.setSdt(str_sdt);
                                Paper.book().write("user", Utils.user_current);
                                Toast.makeText(getApplicationContext(),MessageModel.getMessage(),Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(),MessageModel.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    interface EmailValidationCallback {
        void onValidationResult(boolean isValid);
    }

    private void actionToolBar() {
        setSupportActionBar(toolBarCTUser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarCTUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolBarCTUser = findViewById(R.id.toolBarCTUser);
        CTuser_baomat = findViewById(R.id.CTuser_baomat);
        CTuser_name_btn = findViewById(R.id.CTuser_name_btn);
        CTuser_email_btn = findViewById(R.id.CTuser_email_btn);
        CTuser_sdt_btn = findViewById(R.id.CTuser_sdt_btn);
        profile_image_CTuser = findViewById(R.id.profile_image_CTuser);
        CTuser_name = findViewById(R.id.CTuser_name);
        CTuser_email = findViewById(R.id.CTuser_email);
        CTuser_sdt = findViewById(R.id.CTuser_sdt);
    }
}