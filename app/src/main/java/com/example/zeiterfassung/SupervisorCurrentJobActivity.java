package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SupervisorCurrentJobActivity extends AppCompatActivity {






    /** @noinspection deprecation*/ // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen!", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_current_job);

        // XML-Elemente mit dem Code verbinden
        Button backToSupervisorPage_Button_S = findViewById(R.id.backToSupervisorPage_Button_S);
        Button chat_Supervisor_Button_S = findViewById(R.id.chat_Button);
        Button showDialogMoreInformationButton_S = findViewById(R.id.moreInformationElevatedButton);
        Button finishJobButton = findViewById(R.id.jobDoneButton);

        // Hier wird das Eingabefeld der Zahlen von 0-10 begrenzt, damit man keine mehr als 10h arbeiten kann
        //------------------------------------------------------------------------------------------------------------------
        EditText editText = findViewById(R.id.workTime_S);

        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                try {
                    String result = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());
                    int input = Integer.parseInt(result);
                    if (input >= 0 && input <= 10) {
                        return null; // erlaubt
                    }
                } catch (NumberFormatException e) {
                    // Eingabe ist kein gültiger Integer
                }
                return ""; // Eingabe blockieren
            }
        };

        editText.setFilters(new InputFilter[]{inputFilter});
        //------------------------------------------------------------------------------------------------------------------



        // Daten empfangen aus der Card die angetippt wurde
        Intent intent = getIntent();
        String customerName = intent.getStringExtra("customerName");
        String customerAddress = intent.getStringExtra("customerAddress");
        String einsatzgrund = intent.getStringExtra("einsatzgrund");
        String mitarbeiter = intent.getStringExtra("mitarbeiter");
        String prioritaet = intent.getStringExtra("prioritaet");
        final String[] weitereInformationen = {intent.getStringExtra("weitereInformationen")};
        String jobId = intent.getStringExtra("id");
        String orderNumber = intent.getStringExtra("orderNumber");


        // Daten anzeigen
        TextView customerNameTextView = findViewById(R.id.textView_customer_S);
        TextView customerAddressTextView = findViewById(R.id.textView_address_S);
        TextView reasonForWorkTextView = findViewById(R.id.textView_Einsatzgrund_S);
        TextView workerTextView = findViewById(R.id.textView_Worker_S);
        TextView priorityTextView = findViewById(R.id.textView_Priority_S);


        customerNameTextView.setText(customerName);
        customerAddressTextView.setText(customerAddress);
        reasonForWorkTextView.setText(einsatzgrund);
        workerTextView.setText(mitarbeiter);
        priorityTextView.setText(prioritaet);


        // Zurück Button zur Mitarbeiteransicht aktuelle Auftraege
        backToSupervisorPage_Button_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zur Activity "Neue Auftragsdaten"
                Intent intent = new Intent(SupervisorCurrentJobActivity.this, SupervisorActivity.class);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });

        // Über den Chat Button zur AddOn Page
        chat_Supervisor_Button_S.setOnClickListener(v -> {
            Intent addonIntent = new Intent(SupervisorCurrentJobActivity.this, SuperVisorAddOnButtonsActivity.class);
            startActivity(addonIntent);
        });

        // Button für weitere Informationen Dialog
        showDialogMoreInformationButton_S.setOnClickListener(v -> {

            // Prüfung auf null oder leerer Inhalt
            if (weitereInformationen[0] == null || weitereInformationen[0].trim().isEmpty()) {
                weitereInformationen[0] = "Keine weiteren Informationen zum Auftrag vorhanden.";
            }

            // Dialog anzeigen
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Weitere Informationen")
                    .setMessage(weitereInformationen[0])
                    .setPositiveButton("OK", null)
                    .show();
        });

        // Button für Fertigmeldung des Auftrags, wenn die Auftragsdauer eingetragen ist.
        finishJobButton.setOnClickListener(v -> {
            String auftragsdauer = editText.getText().toString().trim();

            if (auftragsdauer.isEmpty()) {
                Toast.makeText(this, "Bitte die geleistete Arbeitszeit eingeben!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Auftrag-Objekt mit Arbeitszeit erstellen (du brauchst hier den neuen Konstruktor)
            Auftrag finishedJob = new Auftrag(
                    jobId,
                    orderNumber,
                    customerName,
                    customerAddress,
                    einsatzgrund,
                    mitarbeiter,
                    prioritaet,
                    weitereInformationen,
                    auftragsdauer

            );

            // Firebase Referenzen
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            DatabaseReference currentRef = db.child("auftraege").child(jobId);
            DatabaseReference historyRef = db.child("fertigeAuftraege").child(jobId);

            // In history schreiben, dann aus currentActivity entfernen
            historyRef.setValue(finishedJob).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentRef.removeValue();
                    Toast.makeText(this, "Auftrag fertig gemeldet", Toast.LENGTH_SHORT).show();

                    // Zurück zur Übersicht
                    Intent i = new Intent(SupervisorCurrentJobActivity.this, SupervisorActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "Fehler beim fertigmelden", Toast.LENGTH_SHORT).show();
                }
            });
        });






    }



}
