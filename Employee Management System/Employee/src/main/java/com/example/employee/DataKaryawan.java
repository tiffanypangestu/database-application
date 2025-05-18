package com.example.employee;


import java.sql.Date;

public class DataKaryawan {
    private Integer IDKaryawan;
    private String namaLengkap;
    private String jenisKelamin;
    private String nomorTelepon;
    private String posisiKerja;
    private String gambar;
    private Date tanggal;
    private Double gaji;

    public DataKaryawan(Integer IDKaryawan, String namaLengkap, String jenisKelamin, String nomorTelepon, String posisiKerja, String gambar, Date tanggal) {
        this.IDKaryawan = IDKaryawan;
        this.namaLengkap = namaLengkap;
        this.jenisKelamin = jenisKelamin;
        this.nomorTelepon = nomorTelepon;
        this.posisiKerja = posisiKerja;
        this.gambar = gambar;
        this.tanggal = tanggal;
    }

    public DataKaryawan(Integer IDKaryawan, String namaLengkap, String posisiKerja, Double gaji) {
        this.IDKaryawan = IDKaryawan;
        this.namaLengkap = namaLengkap;
        this.posisiKerja = posisiKerja;
        this.gaji = gaji;
    }
    public Integer getIDKaryawan() {
        return IDKaryawan;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public String getPosisiKerja() {
        return posisiKerja;
    }

    public String getGambar() {
        return gambar;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public Double getGaji() {
        return gaji;
    }
}

