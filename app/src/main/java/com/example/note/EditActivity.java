package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.note.database.AppDatabase;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = EditActivity.class.getName();
    private static final String ACTION_KEY = TAG + ".action.key";
    private static final String NOTA_KEY = TAG + "nota.key";
    private char action;
    // Richiam il ToolbarLayout
    private CollapsingToolbarLayout collapsingToolbarLayout;
    // Richiamo gli altri compoenti
    private EditText editTitle;
    private EditText editNote;

    // Dichiaro il db
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        // Inizializzo il toolbar layout
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        // Voglio settare il testo dell CTL a none, in modo che non compaia il titolo in alto
        // non posso mettere solo "" ma devo fare " " con uno spazio
        collapsingToolbarLayout.setTitle(" ");

        // Inizializzo gli altri componenti
        editTitle = findViewById(R.id.editTitle);
        editNote = findViewById(R.id.editNote);

        // Inizializzo il db
        db = Room.databaseBuilder(this, AppDatabase.class, "note_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        // Inizializzo l'action
        action = getIntent().getCharExtra(ACTION_KEY, 'n');

        // Recupero la nota
        final Nota nota = (Nota) getIntent().getSerializableExtra(NOTA_KEY);

        // Se l'action è 'e' devo andare ad updatare il titolo e il testo
        if (action == 'e')
        {
            editNote.setText(nota.getNota());
            editTitle.setText(nota.getTitle());
        }

        // Inizializzo il button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // Gestisco l'onClick del button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Salvo in questa variabile il contenuto dell'editTitle
                String titleEdit = editTitle.getText().toString().trim();
                // Salvo in questa variabile il contenuto dell'editNote
                String noteEdit = editNote.getText().toString().trim();
                // Creo riassunto
                String riassunto = noteEdit.length() > 200 ? noteEdit.substring(0, 197) + "..." : noteEdit;
                // Se non sono vuoti
                if (!titleEdit.isEmpty() || !noteEdit.isEmpty())
                {
                    // Devo vedere quale azione è stata scelta dall'user per capire se vuole
                    // editare il database o creare qualcsa di nuovo
                    switch (action)
                    {
                        // Caso in cui sta aggiungendo qualcosa di nuovo
                        case 'a':
                            // Aggiungo nel db una nuova Nota, passando titolo e testo
                            db.notaDAO().insertAll(new Nota(titleEdit, noteEdit, riassunto));
                            break;
                        // Caso in cui sta editando la nota
                        case 'e':
                            nota.setNota(editNote.getText().toString());
                            nota.setTitle(editTitle.getText().toString());
                            nota.setNotaRecap(nota.getNota().length() > 200 ? nota.getNota().substring(0, 197) + "..." : nota.getNota());
                            Toast.makeText(getApplicationContext(), "Not modificata", Toast.LENGTH_SHORT).show();
                            // Elimino la nota

                            // Inserisco la nota modificata
                            db.notaDAO().updateNote(nota);
                            break;
                    }
                    // Così refresho la lista della home
                    MainActivity.adapterNotifyAll();
                    // Elimina l'attività nel momento in cui clicckiamo
                    finish();
                }
                // Caso in cui uno dei due è vuoto
                else
                {
                    // Faccio comparire una Snackbar con questo avviso
                    Snackbar.make(view, "Inserisci almeno un dato.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Definisco un metodo statico per creare l'intent per passare a questa Activity
    public static Intent getIntentEdit(Context c, char action)
    {
        Intent intent = new Intent(c, EditActivity.class);
        intent.putExtra(ACTION_KEY, action);
        return intent;
    }

    // Devo definire un altro metodo per creare l'intent, questa volta da richiamare nella DetailActivity
    public static Intent getIntentEdit(Context context, char action, Nota nota) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(ACTION_KEY, action);
        intent.putExtra(NOTA_KEY, nota);
        return intent;
    }
}