package com.example.note;

import android.content.Context;
import android.os.Bundle;

import com.example.note.database.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.VerifiedInputEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Lista
    private ListView listView;
    // private static ArrayAdapter<String> adapter;
    private static List<String> lista;
    private static AppDatabase db;
    private static List<Nota> notes;
    private static RecyclerView recyclerView;
    private static RecyclerViewAdapter adapter;
    private static Context baseContext;
    private static View viewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewLayout = findViewById(R.id.viewLayout);
        baseContext = MainActivity.this;

        // Creiamo il database
        if (savedInstanceState == null) {
            db = Room.databaseBuilder(this, AppDatabase.class, "note_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            getValue();
        }


        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(notes, MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // Questo è il bottone
        FloatingActionButton fab = findViewById(R.id.fab);
        // Gestisco l'onClick del bottone
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Voglio che, cliccando sul bottone, ci porti a un'activity diversa, però
                // questa activity deve servire sia per creare le note che per modificarle.
                // Quindi passo un parametro char, che può essere "a" o "e" a seconda che sia
                // add o edit.
                // Richiamo il metodo startActivity, che starta un'Activity, passandogli un
                // Intent come parametro. Gli passiamo l'intent ritornato dal metodo statico
                // getIntentEdit, presente all'interno della calsse EditActivity
                startActivity(EditActivity.getIntentEdit(MainActivity.this, 'a'));
            }
        });
    }

    // Metodo per prendere i valori dentro al db
    private static void getValue() {
        // Creiamo lista di Nota e prendiamo tutte le note da db
        notes = db.notaDAO().getAll();
        // Aggiorno adapter
        recyclerView = viewLayout.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(notes, baseContext);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // Metodo per aggiornare la mainactivity dinamicamente, senza doverla chiudere
    public static void adapterNotifyAll() {
        // Pulisco la lista
        getValue();
    }

    public static void sendObject(Context context, int position)
    {
        context.startActivity(DetailActivity.getDetailsIntent(context, notes.get(position)));
    }

    // Questi sotto sono i metodi per creare i tre puntini del menu
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
     */
}