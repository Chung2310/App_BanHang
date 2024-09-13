package com.example.quanlybanhang.retrofit;

import com.example.quanlybanhang.model.LoaiSpModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();
}
