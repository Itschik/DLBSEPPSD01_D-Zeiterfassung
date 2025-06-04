package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.telephony.SmsManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SuperVisorAddOnButtonsActivity extends AppCompatActivity {

    // Deaktiviert den Hardware-Zurück-Button für diese Activity
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen", Toast.LENGTH_SHORT).show();
    }

    // Beispielhafte Namen für Empfängerauswahl
    String[] names = {"Robin"};

    // WICHTIG: Hier sollte die Telefonnummern-Liste denselben Index wie die Namen haben
    // Hinweis: Im geposteten Code ist sie leer → SMS-Versand wird fehlschlagen, wenn dies nicht gefüllt ist
    String[] numbers = {""};

    // Temporäre Variablen zum Speichern der ausgewählten Nummer und des Namens
    final String[] phoneNumber = {""};
    final String[] phoneNumberName = {""};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_add_on_buttons);

        // UI-Komponenten referenzieren
        Button backButton = findViewById(R.id.backToSupervisorCurrentJob_Button);
        Button addOnButtonOne = findViewById(R.id.ADD_ON_Button_1);
        Button addOnButtonTwo = findViewById(R.id.ADD_ON_Button_2);
        Button addOnButtonThree = findViewById(R.id.ADD_ON_Button_3);
        Button addOnButtonFour = findViewById(R.id.ADD_ON_Button_4);

        // Zurück zur vorherigen Activity
        backButton.setOnClickListener(v -> finish());

        // ---------- DROPDOWN EMPFÄNGERAUSWAHL ----------
        AutoCompleteTextView recipientDropdown = findViewById(R.id.recipientDropdown);

        // Adapter mit Namen für das Dropdown-Menü verbinden
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names);
        recipientDropdown.setAdapter(adapter);

        // Listener: Bei Auswahl eines Namens wird die zugehörige Nummer gespeichert
        recipientDropdown.setOnItemClickListener((parent, view, position, id) -> {
            if (position < numbers.length) {
                phoneNumber[0] = numbers[position];
                phoneNumberName[0] = names[position];
            } else {
                Toast.makeText(this, "Keine Nummer hinterlegt!", Toast.LENGTH_SHORT).show();
            }
        });

        // ---------- SMS BUTTONS MIT VORDEFINIERTEN NACHRICHTEN ----------

        // Button 1: Nachricht über zusätzliches Personal
        addOnButtonOne.setOnClickListener(v -> sendeSMS("Weiteres Personal ist unterwegs!"));

        // Button 2: Nachricht über Material
        addOnButtonTwo.setOnClickListener(v -> sendeSMS("Material ist unterwegs!"));

        // Button 3: Nachricht über Werkzeug
        addOnButtonThree.setOnClickListener(v -> sendeSMS("Werkzeug ist unterwegs!"));

        // Button 4: Bitte um Rückruf
        addOnButtonFour.setOnClickListener(v -> sendeSMS("Bitte kurz anrufen, ich habe Fragen zum Auftrag!"));
    }

    /**
     * Prüft Berechtigungen und sendet eine SMS mit dem übergebenen Text an die gewählte Nummer.
     *
     * @param message Der Nachrichtentext
     */
    private void sendeSMS(String message) {
        // Prüfe SMS-Berechtigung
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Falls nicht vorhanden, Berechtigung anfordern
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            // SMS kann gesendet werden
            try {
                if (!phoneNumber[0].isEmpty()) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber[0], null, message, null, null);
                    Toast.makeText(this, "SMS gesendet an " + phoneNumberName[0], Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Bitte Empfänger auswählen!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Fehler beim Senden der SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
