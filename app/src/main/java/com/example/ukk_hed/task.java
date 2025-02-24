package com.example.ukk_hed;

public class task {
    private String namaTugas;
    private String kategori;
    private String tanggal;
    private String waktu;
    private String deskripsi;
    private String status;
    private String id;

    // Constructor
    public task(String id, String namaTugas, String kategori, String tanggal, String waktu, String deskripsi, String status) {
        this.id = id;
        this.namaTugas = namaTugas;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
        this.status = status;
    }

    // Getter Methods
    public String getId() {
        return id;
    }

    public String getNamaTugas() {
        return namaTugas;
    }

    public String getKategori() {
        return kategori;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getStatus() {
        return status;
    }

    // Setter Methods
    public void setNamaTugas(String namaTugas) {
        this.namaTugas = namaTugas;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Mark Task as Complete
    public void markAsComplete() {
        this.status = "Selesai";
    }

    // Debugging (Convert Object to String)
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", namaTugas='" + namaTugas + '\'' +
                ", kategori='" + kategori + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", waktu='" + waktu + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
