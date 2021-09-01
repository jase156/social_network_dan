package com.halfbyte.danv1.Comentarios;

import android.os.Parcel;
import android.os.Parcelable;

public class Comentario implements Parcelable{
    private String persona;
    private String comentario;

    public Comentario(){}

    public  Comentario(Parcel in){
        readFromParcel(in);
    }

    public static final Creator<Comentario> CREATOR = new Creator<Comentario>() {
        @Override
        public Comentario createFromParcel(Parcel in) {
            return new Comentario(in);
        }

        @Override
        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(persona);
        parcel.writeString(comentario);
    }

    private void readFromParcel(Parcel in) {
        persona = in.readString();
        comentario = in.readString();
    }
}