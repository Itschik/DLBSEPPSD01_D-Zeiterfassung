package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SupervisorNewJobActivity extends AppCompatActivity {

    // Überschreibt den Standard-Zurück-Button, um versehentliches Verlassen zu verhindern
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen.", Toast.LENGTH_SHORT).show();
    }

    // ____________________________ Variablen-Definitionen _______________________________
    // Dropdown-Felder
    AutoCompleteTextView dropdown_einsatzgrund;
    AutoCompleteTextView dropdown_mitarbeiter;
    AutoCompleteTextView dropdown_prioritaet;

    // Firebase-Datenbank-Referenz
    private DatabaseReference auftragRef;
    // ___________________________________________________________________________________


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_new_job);

        // ____________________________ Firebase Referenzierung ____________________________
        // Firebase-Referenz auf den Knoten "auftraege"
        auftragRef = FirebaseDatabase.getInstance().getReference("auftraege");
        // ________________________________________________________________________________


        // ____________________________ UI-Elemente verknüpfen _____________________________
        // Dropdowns
        dropdown_einsatzgrund = findViewById(R.id.AutoCompleteTextView_einsatzgrund);
        dropdown_mitarbeiter = findViewById(R.id.AutoCompleteTextView_mitarbeiter);
        dropdown_prioritaet = findViewById(R.id.AutoCompleteTextView_prioritaet);

        // Buttons
        Button backToSupervisorPageButton_S = findViewById(R.id.backToSupervisorPage_Button);
        Button jobDoneButton_S = findViewById(R.id.jobDoneButton);

        // Eingabefelder
        EditText orderNumberInput = findViewById(R.id.ordernumber_Input);
        EditText customerNameInput = findViewById(R.id.customerName_Input);
        EditText customerAddressInput = findViewById(R.id.customerAddress_Input);
        EditText moreInformationInput = findViewById(R.id.moreInformation_Input);
        // ________________________________________________________________________________


        // ____________________ Werte für die Dropdown-Menüs definieren ____________________
        String[] einsatzgrund = {"Defekt", "Wartung", "Störung", "Reparatur", "Installation", "Prüfung"};
        String[] mitarbeiter = {"Philipp Neumann", "Alban Demirci", "Carlson Diego"};
        String[] prioritaet = {"Hoch", "Mittel", "Niedrig"};
        // ________________________________________________________________________________


        // _______________________ Adapter für Dropdowns erstellen _________________________
        ArrayAdapter<String> adapter_einsatzgrund = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, einsatzgrund);

        ArrayAdapter<String> adapter_mitarbeiter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, mitarbeiter);

        ArrayAdapter<String> adapter_prioritaet = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, prioritaet);

        // Adapter mit Dropdown-Feldern verbinden
        dropdown_einsatzgrund.setAdapter(adapter_einsatzgrund);
        dropdown_mitarbeiter.setAdapter(adapter_mitarbeiter);
        dropdown_prioritaet.setAdapter(adapter_prioritaet);

        // Freie Texteingabe deaktivieren – nur Auswahl erlaubt
        dropdown_einsatzgrund.setInputType(InputType.TYPE_NULL);
        dropdown_einsatzgrund.setKeyListener(null);

        dropdown_mitarbeiter.setInputType(InputType.TYPE_NULL);
        dropdown_mitarbeiter.setKeyListener(null);

        dropdown_prioritaet.setInputType(InputType.TYPE_NULL);
        dropdown_prioritaet.setKeyListener(null);
        // ________________________________________________________________________________


        // ____________________ Zurück-Button: Wechsel zur SupervisorActivity _____________
        backToSupervisorPageButton_S.setOnClickListener(v -> {
            Intent intent = new Intent(SupervisorNewJobActivity.this, SupervisorActivity.class);
            startActivity(intent);
            finish(); // Beendet aktuelle Activity
        });
        // ________________________________________________________________________________


        // ____________ OK-Button: Eingabe prüfen und Auftrag in Firebase speichern ________
        jobDoneButton_S.setOnClickListener(v -> {
            // Eingabedaten auslesen
            String orderNumber = orderNumberInput.getText().toString().trim();
            String customerName = customerNameInput.getText().toString().trim();
            String customerAddress = customerAddressInput.getText().toString().trim();
            String einsatzGrund = dropdown_einsatzgrund.getText().toString().trim();
            String mitarbeiterValue = dropdown_mitarbeiter.getText().toString().trim();
            String prioritaetValue = dropdown_prioritaet.getText().toString().trim();
            String moreInformation = moreInformationInput.getText().toString().trim();  // optional

            // Validierung: Pflichtfelder dürfen nicht leer sein
            if (orderNumber.isEmpty() || customerName.isEmpty() || customerAddress.isEmpty()
                    || einsatzGrund.isEmpty() || mitarbeiterValue.isEmpty() || prioritaetValue.isEmpty()) {
                Toast.makeText(SupervisorNewJobActivity.this, "Bitte füllen Sie alle Felder aus", Toast.LENGTH_SHORT).show();
            } else {
                // Eindeutige ID für den Auftrag generieren
                String id = auftragRef.push().getKey();

                // Neues Auftrag-Objekt erstellen
                Auftrag neuerAuftrag = new Auftrag(id, orderNumber, customerName, customerAddress,
                        einsatzGrund, mitarbeiterValue, prioritaetValue, moreInformation);

                // Auftrag in Firebase speichern
                auftragRef.child(id).setValue(neuerAuftrag)
                        .addOnSuccessListener(aVoid -> {
                            // Bei Erfolg zurück zur Hauptseite
                            Intent intent = new Intent(SupervisorNewJobActivity.this, SupervisorActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(SupervisorNewJobActivity.this, "Fehler beim Speichern", Toast.LENGTH_SHORT).show());
            }
        });
        // ________________________________________________________________________________
    }
}
