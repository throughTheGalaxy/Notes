package com.example.notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.notes.adapter.ListItem;
import com.example.notes.db.AppExecuter;
import com.example.notes.db.MyConstants;
import com.example.notes.db.MyDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * Die Klasse zur Bearbeitung von Notizen,
 * als Felder initialisieren wir ein Objekt vom Typ ImageView, Button, Editor
 */

public class EditActivity extends AppCompatActivity {

    /**
     * Wir verwenden eine PICK-Konstante und eine String-Variable,
     * um das Bild auf dem Gerät zu finden. Variable TempUri wird leer bleiben,
     * bis wann das erste Image nicht hinzugefügt sein wird.
     */

    private final int PICK_IMAGE_CODE = 123;
    private String tempUri = "empty";
    private ImageView imNewImage;
    private ConstraintLayout imageContainer;
    private FloatingActionButton fbAddImage;
    private ImageButton imEditImage, imDeleteImage;
    private EditText edTitle, edDesc;
    private MyDbManager myDbManager;
    private boolean isEditState = true;
    private ListItem item;

    /**
     * Dies wurde in MainActivity beschrieben.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getMyIntents();
    }

    /**
     * Dies wurde in MainActivity beschrieben.
     */

    @Override
    protected void onResume() {
        super.onResume();

        myDbManager.openDb();
    }

    /**
     * Wir nehmen ResultCode als Parameter, was OK sein sollte.
     * Wenn es OK ist und der ReguestCode gleich Pick ist,
     * erhalten wir einen Link zur Auswahl eines Bildes.
     * <p>
     * Die Links zu den Bildern, die wir erhalten, werden dauerhaft sein.
     * So bleiben sie länger als nur ein paar Tage in der Datenbank gespeichert.
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE && data != null) {

            tempUri = data.getData().toString();
            imNewImage.setImageURI(data.getData());
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);

        }
    }


    /**
     * Initialisierungsmethode zur Erstellung von EditActivity,
     * zuerst Initialisierung vom View, dann der Bilder und der Datenbank
     */

    private void init() {

        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        imNewImage = findViewById(R.id.imNewImage);
        fbAddImage = findViewById(R.id.fbAddImage);
        imageContainer = findViewById(R.id.imageContainer);
        imEditImage = findViewById(R.id.imEditImage);
        imDeleteImage = findViewById(R.id.imDeleteImage);
        myDbManager = new MyDbManager(this);
    }


    /**
     * Hier nehmen wir unsere Objekte vom Typ Intent.
     * Wenn es nicht gleich NULL ist, können wir die Informationen, die wir von hier benötigen
     * <p>
     * Da wir einen Byte-Stream empfangen, muss er gecastet werden.
     * Zu diesem Zweck verwenden wir die Hilfsklasse Item.
     */

    private void getMyIntents() {

        Intent i = getIntent();
        if (i != null) {

            item = (ListItem) i.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);

            isEditState = i.getBooleanExtra(MyConstants.EDIT_STATE, true);

            if (!isEditState) {

                edTitle.setText(item.getTitle());
                edDesc.setText(item.getDesc());
                if (!item.getUri().equals("empty")) {
                    tempUri = item.getUri();
                    imageContainer.setVisibility(View.VISIBLE);
                    imNewImage.setImageURI(Uri.parse(item.getUri()));
                    imDeleteImage.setVisibility(View.GONE);
                    imEditImage.setVisibility(View.GONE);

                }

            }


        }

    }


    /**
     * Wir fügen eine Funktion hinzu, die es uns ermöglicht,
     * Elemente in der Datenbank zu speichern, wenn ein Zuhörer vorhanden ist.
     * <p>
     * Dann erstellen wir eine Reihe von Prüfungen, um ein leeres Feld in die Datenbank einzugeben,
     * wenn ein leeres Element in das Kopffeld oder Text in das Programm eingegeben wird,
     * sollte ein Fehler durch das Toast-Objekt in Form eines entsprechenden Pop-up-Fensters folgen,
     * wenn das Element zur Sammlung hinzugefügt wird, wird der Benutzer über das Pop-up-Fenster informiert,
     * dass das Element gespeichert wurde.
     * <p>
     * Am Ende des Methodenstapels schließen wir die Datenbank und rufen Methode finish() auf.
     */


    public void onClickSave(View view) {

        final String title = edTitle.getText().toString();
        final String desc = edDesc.getText().toString();

        if (title.equals("") || desc.equals("")) {

            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();

        } else {

            if (isEditState) {

                AppExecuter.getInstance().getSubIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        myDbManager.insertToDb(title, desc, tempUri);
                    }
                });
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();

            } else {

                myDbManager.updateItem(title, desc, tempUri, item.getId());
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();

            }
            myDbManager.closeDb();
            finish();

        }
    }


    /**
     * Funktion zum Löschen von Bildern aus der Application
     */


    public void onClickDeleteImage(View view) {

        imNewImage.setImageResource(R.drawable.ic_image_def);
        tempUri = "empty";
        imageContainer.setVisibility(View.GONE);
        fbAddImage.setVisibility(View.VISIBLE);
    }

    /**
     * Funktion zum Hinzufügen von Bildern zur Application
     */

    public void onClickAddImage(View view) {
        imageContainer.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    /**
     * Funktion zur Bildauswahl.
     * <p>
     * Um diese Funktion zu implementieren, verwenden wir ein Objekt vom Typ Intent und Konstante für die Abfrage
     */

    public void onClickChooseImage(View view) {

        Intent chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooser.setType("image/*");
        startActivityForResult(chooser, PICK_IMAGE_CODE);

    }
}