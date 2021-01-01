package com.ynov.findme.models;

public class Fields {
    private String gc_obo_nature_c;
    private String gc_obo_gare_origine_r_name;
    private String date;
    private String gc_obo_type_c;
    // private int _obo_gare_origine_r_code_uic_c;

    public Fields (String gc_obo_nature_c, String gc_obo_gare_origine_r_name, String date, String gc_obo_type_c) {
        this.gc_obo_nature_c = gc_obo_nature_c;
        this.gc_obo_gare_origine_r_name = gc_obo_gare_origine_r_name;
        this.date = date;
        this.gc_obo_type_c = gc_obo_type_c;

    }

    public String getGc_obo_nature_c() {
        return gc_obo_nature_c;
    }

    public void setGc_obo_nature_c(String gc_obo_nature_c) {
        this.gc_obo_nature_c = gc_obo_nature_c;
    }

    public String getGc_obo_gare_origine_r_name() {
        return gc_obo_gare_origine_r_name;
    }

    public void setGc_obo_gare_origine_r_name(String gc_obo_gare_origine_r_name) {
        this.gc_obo_gare_origine_r_name = gc_obo_gare_origine_r_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGc_obo_type_c() {
        return gc_obo_type_c;
    }

    public void setGc_obo_type_c(String gc_obo_type_c) {
        this.gc_obo_type_c = gc_obo_type_c;
    }

   /* public int get_obo_gare_origine_r_code_uic_c() {
        return _obo_gare_origine_r_code_uic_c;
    }

    public void set_obo_gare_origine_r_code_uic_c(int _obo_gare_origine_r_code_uic_c) {
        this._obo_gare_origine_r_code_uic_c = _obo_gare_origine_r_code_uic_c;
    }*/
}
