package com.halfbyte.danv1.Contenido;

public class Card {
    private String mFecha;
    private String mNombre;
    private String mTitulo;
    private String mResumen;
    private String mHistoria;
    private int mImagen;

    public String getmName() {
        return mNombre;
    }

    public void setmName(String mName) {
        this.mNombre = mName;
    }

    public String getmHistoria() {
        return mHistoria;
    }

    public void setmHistoria(String mHistoria) {
        this.mHistoria = mHistoria;
    }

    public int getmImagen() {
        return mImagen;
    }

    public void setmImagen(int mImagen) {
        this.mImagen = mImagen;
    }

    public String getmResumen() {
        return mResumen;
    }

    public void setmResumen(String mResumen) {
        this.mResumen = mResumen;
    }

    public String getmFecha() {
        return mFecha;
    }

    public void setmFecha(String mFecha) {
        this.mFecha = mFecha;
    }

    public String getmTitulo() {
        return mTitulo;
    }

    public void setmTitulo(String mTitulo) {
        this.mTitulo = mTitulo;
    }
}
