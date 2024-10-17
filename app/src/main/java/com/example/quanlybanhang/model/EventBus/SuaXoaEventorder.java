package com.example.quanlybanhang.model.EventBus;

import com.example.quanlybanhang.model.DonHang;

public class SuaXoaEventorder {
    DonHang donHang;

    public SuaXoaEventorder(DonHang donHang) {
        this.donHang = donHang;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }
}
