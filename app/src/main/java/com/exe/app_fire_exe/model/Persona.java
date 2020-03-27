package com.exe.app_fire_exe.model;

public class Persona {

    private String uid;
    private String Nombre;
    private String Domicilio;
    private String DNI;
    private String Causa;

    public Persona() {
    }

    public String getDomicilio() {
        return Domicilio;
    }

    public void setDomicilio(String domicilio) {
        Domicilio = domicilio;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getCausa() {
        return Causa;
    }

    public void setCausa(String causa) {
        Causa = causa;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    @Override
    public String toString() {
        //return Nombre;
        return Nombre;
    }
}
