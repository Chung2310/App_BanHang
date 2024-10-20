package com.example.quanlybanhang.model;

public class Item {
    int idsp;
    long giasp;
    long tonggia;
    String tensp;
    int soluong;
    String hinhanh;


    public long getGiasp() {
        return giasp;
    }

    public void setGiasp(long giasp) {
        this.giasp = giasp;
    }

    public long getTonggia() {
        return tonggia;
    }

    public void setTonggia(long tonggia) {
        this.tonggia = tonggia;
    }

    public int getIdsp() {
        return idsp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }
}
