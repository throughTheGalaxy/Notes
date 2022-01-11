package com.example.notes.adapter;

import java.io.Serializable;

/**
 * Hier erstellen wir eine separate Klasse, um die einzelnen DataBase-Elemente zu behandeln.
 * Diese Klasse dient als Datentyp zur Erstellung von Sammlungen und darauf basierenden Bildoperationen.
 */

public class ListItem implements Serializable {

    private String title;
    private String desc;
    private String uri = "empty";
    private int id = 0;

    /**
     * Gibt den Title des Elements zurück.
     */

    public String getTitle() {
        return title;
    }

    /**
     * Legt den Title für das Element fest.
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gibt die Textbeschreibung des Artikels zurück.
     */

    public String getDesc() {
        return desc;
    }

    /**
     * Ermöglicht die Platzierung von Textinhalten in einem Element.
     */

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gibt Uri vom Element zurück.
     */

    public String getUri() {
        return uri;
    }

    /**
     * Legt Uri für das Element fest.
     */

    public void setUri(String uri) {
        this.uri = uri;
    }


    /**
     * Gibt ID vom Element zurück.
     */

    public int getId() {
        return id;
    }

    /**
     * Legt ID für das Element fest.
     */

    public void setId(int id) {
        this.id = id;
    }
}