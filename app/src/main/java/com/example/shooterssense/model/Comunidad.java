package com.example.shooterssense.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comunidad implements Parcelable {
    private String id;
    private String titulo;
    private String descripcion;
    private String lugar;
    private String imageUrl;
    private String publisher;
    private String fecha;

    //TERMINADO

    public Comunidad() {
    }

    public Comunidad(String id, String titulo, String descripcion, String lugar, String imageUrl, String publisher, String fecha) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.imageUrl = imageUrl;
        this.publisher = publisher;
        this.fecha = fecha;
    }

    protected Comunidad(Parcel in) {
        id = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
        lugar = in.readString();
        imageUrl = in.readString();
        publisher = in.readString();
        fecha = in.readString();
    }

    public static final Creator<Comunidad> CREATOR = new Creator<Comunidad>() {
        @Override
        public Comunidad createFromParcel(Parcel in) {
            return new Comunidad(in);
        }

        @Override
        public Comunidad[] newArray(int size) {
            return new Comunidad[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(lugar);
        dest.writeString(imageUrl);
        dest.writeString(publisher);
        dest.writeString(fecha);
    }
}
