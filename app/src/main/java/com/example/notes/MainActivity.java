package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.notes.adapter.ListItem;
import com.example.notes.adapter.MainAdapter;
import com.example.notes.db.AppExecuter;
import com.example.notes.db.MyDbManager;
import com.example.notes.db.OnDataReceived;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataReceived {
    private MyDbManager myDbManager;
    private EditText edTitle, edDisc;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;


    /**
     * Erstellung der MainActivity mit der Funktion init(),
     * die die Hauptelemente der Application implementiert
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    /**
     * Erstellung von SearchView im Menü, um Notizen durch Interface zu suchen.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        SearchView sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             * Methoden für den Umgang mit Suchdaten
             */

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("My log", "Query . " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                //Log.d("My log", "Query . " + newText);

                readFromDb(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Implementierung einer Funktion zur Erstellung grundlegender Anwendungswerkzeuge:
     * <p>
     * DataBase SQLite
     * Adaptereinstellung
     * LinearLayout
     * RecycleView
     */

    private void init() {

        myDbManager = new MyDbManager(this);
        edTitle = findViewById(R.id.edTitle);
        edDisc = findViewById(R.id.edDesc);
        rcView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelper().attachToRecyclerView(rcView);
        rcView.setAdapter(mainAdapter);
    }


    /**
     * Extrahieren eines Datenstroms aus einer Datenbank.
     * An diesem Punkt interagiert der Benutzer mit dem von Ihnen erstellten Fenster.
     * Die Anwendung erhält Monopolressourcen.
     */


    @Override
    protected void onResume() {
        super.onResume();

        myDbManager.openDb();
        readFromDb("");

    }

    /**
     * Hinzufügen von Methoden per Klick
     */

    public void onClickAdd(View view) {

        Intent i = new Intent(MainActivity.this, EditActivity.class);
        startActivity(i);
    }

    /**
     * Die Methode wird am Ende der Aktivität aufgerufen,
     * wenn die finish()-Methode aufgerufen wird oder wenn das System diese Aktivitätsinstanz vernichtet,
     * um Ressourcen freizugeben.
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }


    /**
     * Swipe-Hörer RECHTS und LINKS.
     * <p>
     * Nachdem Methoden zum Löschen von Elementen aus der Datenbank festgelegt sind,
     * wird eine Methode zum Löschen mit Swipe festgelegt.
     */

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapter.removeItem(viewHolder.getAdapterPosition(), myDbManager);
            }
        });
    }

    /**
     * Lesen Daten von SQLite.
     */

    private void readFromDb(final String text) {

        AppExecuter.getInstance().getSubIO().execute(new Runnable() {
            @Override
            public void run() {
                myDbManager.getFromDb(text, MainActivity.this);
            }
        });

    }


    /**
     * Methode zum Aktualisieren des Adapters.
     * Wir erstellen keinen neuen Thread dank der Klasse AppExecuter
     */

    @Override
    public void onReceived(final List<ListItem> list) {

        AppExecuter.getInstance().getMainIO().execute(new Runnable() {
            @Override
            public void run() {
                mainAdapter.updateAdapter(list);
            }
        });
    }
}