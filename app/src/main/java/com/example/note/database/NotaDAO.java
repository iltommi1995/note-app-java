package com.example.note.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.note.Nota;

import java.util.List;

@Dao
public interface NotaDAO
{
    // METODI CRUD

    // Read
    @Query("SELECT * FROM nota")
    List<Nota> getAll();

    // Create
    @Insert
    void insertAll(Nota... note);

    // Delete
    @Delete
    void delete(Nota nota);

    // Update
    @Update
    void updateNote(Nota... note);
}
