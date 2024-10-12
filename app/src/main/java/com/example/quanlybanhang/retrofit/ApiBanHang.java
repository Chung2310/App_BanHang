package com.example.quanlybanhang.retrofit;

import com.example.quanlybanhang.model.DonHangModel;
import com.example.quanlybanhang.model.LoaiSpModel;
import com.example.quanlybanhang.model.SanPhamMoiModel;
import com.example.quanlybanhang.model.ThemSPModel;
import com.example.quanlybanhang.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("dangki.php")
    Observable<UserModel> dangKi(
            @Query("email") String email,
            @Query("pass") String pass,
            @Query("name") String name,
            @Query("sdt") String sdt
    );
    @GET("dangnhap.php")
    Observable<UserModel> dangNhap(
            @Query("email") String email,
            @Query("pass") String pass
    );

    @GET("reset.php")
    Observable<UserModel> reset(
            @Query("email") String email
    );

    @GET("donhang.php")
    Observable<UserModel> createOrder(
            @Query("sdt") String sdt,
            @Query("email") String email,
            @Query("tongtien") String tongtien,
            @Query("iduser") int iduser,
            @Query("diachi") String diachi,
            @Query("soluong") int soluong,
            @Query("chitiet") String chitiet
    );

    @GET("xemdonhang.php")
    Observable<DonHangModel> xemDonHang(
            @Query("iduser") int iduser
    );

    @GET("timkiem.php")
    Observable<SanPhamMoiModel> search(
            @Query("search") String search
    );

    @GET("themsanpham.php")
    Observable<ThemSPModel> themsanpham(
            @Query("tensp") String tensp,
            @Query("gia") String gia,
            @Query("hinhanh") String hinhanh,
            @Query("mota") String mota,
            @Query("loai") int loai
    );

    @Multipart
    @POST("upload.php")
    Call<ThemSPModel> uploadFile(@Part MultipartBody.Part file);
}
