package com.example.note;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.note.database.AppDatabase;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;


public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getName();
    private static final String KEY = TAG + ".key";
    private AppDatabase db;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView textNote;
    private Nota nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Inizializzo i componenti della view
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        textNote = findViewById(R.id.textNote);


        // Una volta fatto ciò, possiamo creare il db
        db = Room.databaseBuilder(DetailActivity.this, AppDatabase.class, "note_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        // Gestiamo la ricesione dell'oggetto Nota
        nota = (Nota) getIntent().getSerializableExtra(KEY);
        // Ora dobbiamo gestire la ricezione dei messaggi
        // Assert vuol dire che se è diverso da null lo esegue, nel caso in cui fosse uguale a
        // null darà un'exception perché non potrà andare a eseguire le due righe sottostanti
        assert nota != null;
        collapsingToolbarLayout.setTitle(nota.getTitle());
        textNote.setText(nota.getNota());

        // Ora gestisco i bottoni
        // Edit button
        FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.editFab);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Qui gestiamo il tocco sull'edit, dobbiamo far partire l'editActivity
                startActivity(EditActivity.getIntentEdit(DetailActivity.this, 'e', nota));
                finish();
            }
        });
        // Delete button
        FloatingActionButton deleteFab = (FloatingActionButton) findViewById(R.id.deletefab);
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostro l'alert per confermare cancellazione
                showAlert("Conferma la cancellazione", "Vuoi davvero cancellare la nota?");
            }
        });
    }

    // Creiamo un metodo che restituisca un Intent per portare a questa Activity
    public static Intent getDetailsIntent(Context context, Nota nota)
    {
        Intent intent = new Intent(context, DetailActivity.class);
        // In questo intent non passiamo più delle variabili primitive e nemmeno oggetti di tipo
        // String. Passiamo una Nota.
        // Inviare oggetti in android è difficile, solitamente si fa con la classe Prsable, noi però
        // usiamo l'interfaccia seriezable, che ha però delle performance peggiori.
        // Quando si fanno app più complesse si usa Parsable
        intent.putExtra(KEY, nota);
        return intent;
    }

    // Creo un alert per quando si cancella la nota
    private void showAlert(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancello la nota
                        db.notaDAO().delete(nota);
                        // Aggiorno la lista nel main
                        MainActivity.adapterNotifyAll();
                        // Chiudo l'activity
                        finish();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Chiudo l'alert
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}