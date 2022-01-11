package com.example.notes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.EditActivity;
import com.example.notes.R;
import com.example.notes.db.MyConstants;
import com.example.notes.db.MyDbManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Hier erstellen wir einen Adapter für die Recycle-View.
 * <p>
 * Innerhalb des MainAdapters wird eine andere Klasse - ein MaiViewHolder,
 * es wird zusammen arbeiten mit dem Adapter und die Elemente ausfüllen.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private Context context;
    private List<ListItem> mainArray;


    /**
     * Erstellen Sie einen Konstruktor mit Kontext- und Listenübergabe.
     */

    public MainAdapter(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();
    }

    /**
     * Adapter zeichnet ein Element mit dieser Funktion Elemente auf RecycleVue,
     * erfordert das Hinzufügen eines Arrays Größe als Parameter.
     */

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false);
        return new MyViewHolder(view, context, mainArray);
    }


    /**
     * Mit dieser Funktion kann man die RecycleView mit den Elementen füllen, die sich in jedem Objekt Item befinden.
     * Jedes Element wird entsprechend den im Parameter angegebenen Listenpositionen gezeichnet.
     * <p>
     * Es wird eine Hilfsfunktion SetData verwendet,
     * die Elemente in der OnBindHolder-Methode mit String-Werten entsprechend ihrem Positionsindex füllt.
     */

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(mainArray.get(position).getTitle());
    }

    /**
     * Angabe der Größe des Arrays.
     */


    @Override
    public int getItemCount() {
        return mainArray.size();
    }


    /**
     * Interne Hilfsadapterklasse. Hinzufügen einer Drucktasten-Listener-Schnittstelle
     */


    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private Context context;
        private List<ListItem> mainArray;


        /**
         * Konstruktor, der den Kontext für Laiaot verwendet und String-Elemente erhält über tvTitle und ItemListLayot.
         * Hinzufügen eines Tap-Listeners
         */

        public MyViewHolder(@NonNull View itemView, Context context, List<ListItem> mainArray) {
            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);

        }

        /**
         * Erstellen einer Methode zum Füllen der Liste mit einem Parameter vom Typ String.
         */


        public void setData(String title) {
            tvTitle.setText(title);
        }


        /**
         * Erstellen eines Tap-Listeners.
         * Eine Ansichtsfunktion implementieren, ein Verbot der Bearbeitung festlegen.
         */

        @Override
        public void onClick(View v) {

            Intent i = new Intent(context, EditActivity.class);
            i.putExtra(MyConstants.LIST_ITEM_INTENT, mainArray.get(getAdapterPosition()));
            i.putExtra(MyConstants.EDIT_STATE, false);
            context.startActivity(i);


        }
    }

    /**
     * Erstellng einer Funktion, die die Aufgabenliste auffüllt.
     * Prüfung über die integrierte Funktionalität von Recycleview Adapter und Methode notifyDataSetChanged().
     */

    public void updateAdapter(List<ListItem> newList) {

        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * Erstellen eine Methode zum Löschen eines Notizobjekts über einen Adapter.
     * Überprüfung der Entfernung von Elementen aus der Datenbank mit der Methode notifyItemRangeChanged().
     */

    public void removeItem(int pos, MyDbManager dbManager) {
        dbManager.delete(mainArray.get(pos).getId());
        mainArray.remove(pos);
        notifyItemRangeChanged(0, mainArray.size());
        notifyItemRemoved(pos);


    }

}