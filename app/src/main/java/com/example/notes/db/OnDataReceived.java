package com.example.notes.db;

import com.example.notes.adapter.ListItem;

import java.util.List;


/**
 *Interface, was die Lücke zwischen der Klasse MainInterface und MainActivity schließt.
 */

public interface OnDataReceived {
    void onReceived(List<ListItem> list);
}