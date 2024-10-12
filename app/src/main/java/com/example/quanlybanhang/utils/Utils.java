package com.example.quanlybanhang.utils;

import com.example.quanlybanhang.model.GioHang;
import com.example.quanlybanhang.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.1.147:8080/banhang/";

    public static List<GioHang> manggiohang = new ArrayList<>();
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static User user_current = new User();
}
