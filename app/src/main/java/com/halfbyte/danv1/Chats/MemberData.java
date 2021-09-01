package com.halfbyte.danv1.Chats;

public class MemberData {
    private String nombre;
    private String color;

    public MemberData(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    // Add an empty constructor so we can later parse JSON into MemberData using Jackson
    public MemberData() {
    }

    public String getName() {
        return nombre;
    }

    public String getColor() {
        return color;
    }
}
