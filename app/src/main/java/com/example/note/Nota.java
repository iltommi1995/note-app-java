package com.example.note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// Implementa Serializable per poter inviare oggetti tramite l'Intent
@Entity
public class Nota implements Serializable
{
    // Proproietà
    @PrimaryKey (autoGenerate = true)
    private int id;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="nota")
    private String nota;
    @ColumnInfo(name="nota_recap")
    private String notaRecap;
    @ColumnInfo(name="date")
    private String date;

    // Costruttori
    public Nota() {}

    public Nota(String title, String nota, String notaRecap) {
        this.title = title;
        this.nota = nota;
        this.notaRecap = notaRecap;
        // Questa classe ci permette di settare il formato in cui vogliamo vedere
        // la data
        date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
    }

    // Getters & Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotaRecap() {
        return notaRecap;
    }

    public void setNotaRecap(String notaRecap) {
        this.notaRecap = notaRecap;
    }
}
