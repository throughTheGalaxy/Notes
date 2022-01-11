package com.example.notes.db;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Diese Klasse wird für einen sekundären Thread erstellt.
 * <p>
 * Die Klasse wird nur einmal für Neben-Thread angelegt.
 * <p>
 * Es wird ein Singleton-Muster verwendet.
 */

public class AppExecuter {
    private static AppExecuter instance;
    private final Executor mainIO;
    private final Executor subIO;

    public AppExecuter(Executor mainIO, Executor subIO) {
        this.mainIO = mainIO;
        this.subIO = subIO;
    }

    /**
     * In diesem Punkt implementieren wir das Singletone-Muster. Die Methode implementiert einen Haupt- und einen Neben-Thread.
     */

    public static AppExecuter getInstance() {

        if (instance == null)
            instance = new AppExecuter(new MainThreadHandler(), Executors.newSingleThreadExecutor());
        return instance;
    }

    /**
     * Hier schreiben wir eine separate Thread-Implementierungsklasse, die das, was wir brauchen, vom sekundären Thread zum Hauptthread überträgt.
     * <p>
     * Der Handler ist eine Brücke zwischen dem sekundären Thread und dem Hauptthread
     * <p>
     * N/B Wenn wir versuchen, etwas sofort auszuführen mit dem sekundären Thread, z. B. die Benutzeroberfläche zu aktualisieren, kann ein Fehler angezeigt.
     */

    public static class MainThreadHandler implements Executor {
        private Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainHandler.post(command);
        }
    }



    /**
     * Schon fertige Threads, die wir aus Methoden heraus ausführen, um Speicher zu sparen, anstatt neue Objekte zu erstellen.
     */

    public Executor getMainIO() {
        return mainIO;
    }

    public Executor getSubIO() {
        return subIO;
    }
}