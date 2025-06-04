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

    // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen", Toast.LENGTH_SHORT).show();
    }

    String[] names = {"Robin", "Lena"};
    String[] numbers = {"01789126009", "015227247201"};
    final String[] phoneNumber = {""}; //
    final String[] phoneNumberName = {""}; //




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_add_on_buttons);

        Button backButton = findViewById(R.id.backToSupervisorCurrentJob_Button);
        Button addOnButtonOne = findViewById(R.id.ADD_ON_Button_1);
        Button addOnButtonTwo = findViewById(R.id.ADD_ON_Button_2);
        Button addOnButtonThree = findViewById(R.id.ADD_ON_Button_3);
        Button addOnButtonFour = findViewById(R.id.ADD_ON_Button_4);

        backButton.setOnClickListener(v -> {
            finish();
        });

        //_________________________________________________________________________________________________________________________________
        // Dropdown Menü für die Nummerauswahl
        AutoCompleteTextView recipientDropdown = findViewById(R.id.recipientDropdown);

// Dropdown füllen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names);
        recipientDropdown.setAdapter(adapter);

// Auswahl-Listener
        recipientDropdown.setOnItemClickListener((parent, view, position, id) -> {
            phoneNumber[0] = numbers[position]; // Nummer setzen, wenn Name ausgewählt
            phoneNumberName[0] = names[position]; // Name setzen, wenn Name ausgewählt

        });
        //_________________________________________________________________________________________________________________________________



        // SMS Versand über die Buttons
        //_________________________________________________________________________________________________________________________________
        addOnButtonOne.setOnClickListener(v -> {
            String message = "Weiteres Personal ist unterwegs!";

            // Berechtigung prüfen
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                // SMS senden
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    if (!phoneNumber[0].isEmpty()) {
                        smsManager.sendTextMessage(phoneNumber[0], null, message, null, null);
                        Toast.makeText(this, "SMS gesendet an " + phoneNumberName[0], Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Bitte Empfänger auswählen!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Fehler beim Senden der SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        addOnButtonTwo.setOnClickListener(v -> {
            String message = "Material ist untergwes!";

            // Berechtigung prüfen
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                // SMS senden
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    if (!phoneNumber[0].isEmpty()) {
                        smsManager.sendTextMessage(phoneNumber[0], null, message, null, null);
                        Toast.makeText(this, "SMS gesendet an " + phoneNumberName[0], Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Bitte Empfänger auswählen!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Fehler beim Senden der SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        addOnButtonThree.setOnClickListener(v -> {
            String message = "Werkzeug ist untergwes!";

            // Berechtigung prüfen
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                // SMS senden
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    if (!phoneNumber[0].isEmpty()) {
                        smsManager.sendTextMessage(phoneNumber[0], null, message, null, null);
                        Toast.makeText(this, "SMS gesendet an " + phoneNumberName[0], Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Bitte Empfänger auswählen!", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(this, "SMS gesendet!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Fehler beim Senden der SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        addOnButtonFour.setOnClickListener(v -> {
            String message = "Bitte kurz anrufen, ich habe Fragen zum Auftrag!";

            // Berechtigung prüfen
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                // SMS senden
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    if (!phoneNumber[0].isEmpty()) {
                        smsManager.sendTextMessage(phoneNumber[0], null, message, null, null);
                        Toast.makeText(this, "SMS gesendet an " + phoneNumberName[0], Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Bitte Empfänger auswählen!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "Fehler beim Senden der SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //_________________________________________________________________________________________________________________________________



    }


}
