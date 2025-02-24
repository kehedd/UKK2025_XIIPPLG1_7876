package com.example.ukk_hed;

public class ktgr {
    private String nama_kategori;
    private int id;

    public ktgr(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }
    public ktgr(int id) {
        this.id = id;
    }
    public String getNama_kategori() {
        return nama_kategori;
    }
    public int getId() {
        return id;
    }
}
