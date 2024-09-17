package com.example.quanlybanhang.retrofit;

import com.example.quanlybanhang.model.LoaiSpModel;
import com.example.quanlybanhang.model.SanPhamMoiModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();

    @GET("chitiet.php")
    Observable<SanPhamMoiModel> getSanPham(
            @Query("page") int page,
            @Query("loai") int loai
    );
}
