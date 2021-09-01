package com.halfbyte.danv1.Chats;

public class Mensaje {
    private String texto;
    private MemberData datos;
    private boolean enviadoPorUsuario;

    public Mensaje(String texto, MemberData datos, boolean enviadoPorUsuario) {
        this.texto = texto;
        this.datos = datos;
        this.enviadoPorUsuario = enviadoPorUsuario;
    }

    public String getTexto() {
        return texto;
    }

    public MemberData getDatos() {
        return datos;
    }

    public boolean enviadoPorUsuario() {
        return enviadoPorUsuario;
    }
}
