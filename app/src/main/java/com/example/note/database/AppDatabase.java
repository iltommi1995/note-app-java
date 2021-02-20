package com.example.note.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.note.Nota;

@Database(entities = {Nota.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract NotaDAO notaDAO();
}
