package com.example.quanlybanhang.model;

import java.io.Serializable;
import java.util.List;

public class DonHang implements Serializable {
    int id;
    int iduser,idtrangthai;
    String diachi,sodienthoai,tongtien;
    List<Item> item;

    public int getIdtrangthai() {
        return idtrangthai;
    }

    public void setIdtrangthai(int idtrangthai) {
        this.idtrangthai = idtrangthai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getTongtien() {
        return tongtien;
    }

    public void setTongtien(String tongtien) {
        this.tongtien = tongtien;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }
}
