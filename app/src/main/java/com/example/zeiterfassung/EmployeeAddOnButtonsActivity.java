package com.example.zeiterfassung;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class EmployeeAddOnButtonsActivity extends AppCompatActivity {

    // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen", Toast.LENGTH_SHORT).show();
    }

    String[] names = {"Robin"};
    String[] numbers = {};
    final String[] phoneNumber = {""}; //
    final String[] phoneNumberName = {""}; //




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add_on_buttons);

        Button backButton = findViewById(R.id.backToEmployeeCurrentJob_Button_E);
        Button addOnButtonOne = findViewById(R.id.ADD_ON_Button_1_E);
        Button addOnButtonTwo = findViewById(R.id.ADD_ON_Button_2_E);
        Button addOnButtonThree = findViewById(R.id.ADD_ON_Button_3_E);
        Button addOnButtonFour = findViewById(R.id.ADD_ON_Button_4_E);

        backButton.setOnClickListener(v -> {
            finish();
        });

        //_________________________________________________________________________________________________________________________________
        // Dropdown Menü für die Nummerauswahl
        AutoCompleteTextView recipientDropdown = findViewById(R.id.recipientDropdown_E);

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
            String message = "Weiteres Personal ist erforderlich!";

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
            String message = "Material ist erforderlich!";

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
            String message = "Werkzeug ist erforderlich!";

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
