package com.example.shooterssense.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    private String id;
    private String fullname;
    private String username;
    private String fotoPerfilUrl;
    private String bio;

    //TERMINADO-REVISADO

    public Usuario() {
    }

    public Usuario(String id, String fullname, String username, String fotoPerfilUrl, String bio) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.fotoPerfilUrl = fotoPerfilUrl;
        this.bio = bio;
    }

    protected Usuario(Parcel in) {
        id = in.readString();
        fullname = in.readString();
        username = in.readString();
        fotoPerfilUrl = in.readString();
        bio = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullname);
        dest.writeString(username);
        dest.writeString(fotoPerfilUrl);
        dest.writeString(bio);
    }
}