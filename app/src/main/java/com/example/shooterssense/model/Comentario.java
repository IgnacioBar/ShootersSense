package com.example.shooterssense.model;

public class Comentario {
    private String comment;
    private String publisher;

    //TERMINADO

    public Comentario() {
    }

    public Comentario(String comment, String publisher) {
        this.comment = comment;
        this.publisher = publisher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
