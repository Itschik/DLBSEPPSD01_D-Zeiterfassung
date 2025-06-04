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

/**
 * Activity zur Anzeige und Bearbeitung des aktuellen Auftrags eines Mitarbeiters.
 * Hier kann der Mitarbeiter die Auftragsdauer eingeben, den Auftrag als erledigt melden,
 * weitere Informationen anzeigen oder zur vorherigen Ansicht zurückkehren.
 */
public class EmployeeCurrentJobActivity extends AppCompatActivity {

    /**
     * Verhindert, dass der Benutzer mit dem Zurück-Button der Hardware die Activity verlässt.
     * Stattdessen erscheint ein Toast mit einem Hinweis.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_current_job);

        // Verknüpfe die Buttons aus dem XML-Layout mit den Java-Objekten
        Button backToEmployeePage_Button_E = findViewById(R.id.backToLoginPage_Button_Mitarbeiter);
        Button chat_Employee_Button_E = findViewById(R.id.chat_Button_Mitarbeiter);
        Button showDialogMoreInformationButton_E = findViewById(R.id.moreInformationElevatedButton);
        Button finishJobButton = findViewById(R.id.jobDoneButton_Mitarbeiter);

        // Eingabefeld für die Auftragsdauer (Arbeitszeit)
        EditText editText = findViewById(R.id.auftragsDauerMitarbeiter_Input);

        // Eingabefilter um die erlaubte Zahl auf Werte von 0 bis 10 Stunden zu begrenzen
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                try {
                    // Ergebnis simulieren, wenn der neue Input in das bestehende Feld eingefügt wird
                    String result = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());
                    int input = Integer.parseInt(result);
                    // Nur Eingaben zwischen 0 und 10 sind erlaubt
                    if (input >= 0 && input <= 10) {
                        return null; // Eingabe zulassen
                    }
                } catch (NumberFormatException e) {
                    // Falls Eingabe keine gültige Zahl ist -> Eingabe blockieren
                }
                return ""; // Eingabe blockieren
            }
        };
        editText.setFilters(new InputFilter[]{inputFilter});

        // Hole die Daten aus dem Intent (Übergeben von der vorherigen Activity)
        Intent intent = getIntent();
        String customerName = intent.getStringExtra("customerName");
        String customerAddress = intent.getStringExtra("customerAddress");
        String einsatzgrund = intent.getStringExtra("einsatzgrund");
        String mitarbeiter = intent.getStringExtra("mitarbeiter");
        String prioritaet = intent.getStringExtra("prioritaet");
        final String[] weitereInformationen = {intent.getStringExtra("weitereInformationen")};
        String jobId = intent.getStringExtra("id");
        String orderNumber = intent.getStringExtra("orderNumber");

        // Verknüpfe TextViews aus dem Layout mit Java-Objekten
        TextView customerNameTextView = findViewById(R.id.textView_customer_Mitarbeiter);
        TextView customerAddressTextView = findViewById(R.id.textView_address_Mitarbeiter);
        TextView reasonForWorkTextView = findViewById(R.id.textView_Einsatzgrund_Mitarbeiter);
        TextView workerTextView = findViewById(R.id.textView_Worker_Mitarbeiter);
        TextView priorityTextView = findViewById(R.id.textView_Priority_Mitarbeiter);

        // Zeige die übergebenen Daten in den jeweiligen TextViews an
        customerNameTextView.setText(customerName);
        customerAddressTextView.setText(customerAddress);
        reasonForWorkTextView.setText(einsatzgrund);
        workerTextView.setText(mitarbeiter);
        priorityTextView.setText(prioritaet);

        // Listener für den Zurück-Button: Zur Mitarbeiterübersicht navigieren und aktuelle Activity beenden
        backToEmployeePage_Button_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeCurrentJobActivity.this, EmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Listener für den Chat-Button: Öffnet eine AddOn Activity für weitere Funktionen
        chat_Employee_Button_E.setOnClickListener(v -> {
            Intent addonIntent = new Intent(EmployeeCurrentJobActivity.this, EmployeeAddOnButtonsActivity.class);
            startActivity(addonIntent);
        });

        // Listener für den Button "Weitere Informationen"
        showDialogMoreInformationButton_E.setOnClickListener(v -> {
            // Falls keine weiteren Infos vorhanden sind, Standardtext setzen
            if (weitereInformationen[0] == null || weitereInformationen[0].trim().isEmpty()) {
                weitereInformationen[0] = "Keine weiteren Informationen zum Auftrag vorhanden.";
            }

            // MaterialDialog mit den weiteren Informationen anzeigen
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Weitere Informationen")
                    .setMessage(weitereInformationen[0])
                    .setPositiveButton("OK", null)
                    .show();
        });

        // Listener für den Fertigmelde-Button des Auftrags
        finishJobButton.setOnClickListener(v -> {
            String auftragsdauer = editText.getText().toString().trim();

            // Prüfen, ob eine Auftragsdauer eingetragen wurde
            if (auftragsdauer.isEmpty()) {
                Toast.makeText(this, "Bitte die geleistete Arbeitszeit eingeben!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Erstelle ein Auftrag-Objekt mit den aktuellen Daten, inklusive Arbeitszeit
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

            // Firebase Datenbankreferenzen: aktueller Auftrag und Fertig-Aufträge
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            DatabaseReference currentRef = db.child("auftraege").child(jobId);
            DatabaseReference historyRef = db.child("fertigeAuftraege").child(jobId);

            // Fertigen Auftrag in "fertigeAuftraege" speichern und aus "auftraege" löschen
            historyRef.setValue(finishedJob).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentRef.removeValue();
                    Toast.makeText(this, "Auftrag fertig gemeldet", Toast.LENGTH_SHORT).show();

                    // Nach dem Fertigmelden zurück zur Mitarbeiterübersicht
                    Intent i = new Intent(EmployeeCurrentJobActivity.this, EmployeeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "Fehler beim fertigmelden", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
