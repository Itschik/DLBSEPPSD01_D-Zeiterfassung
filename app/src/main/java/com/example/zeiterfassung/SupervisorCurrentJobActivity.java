package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Diese Activity zeigt die Details eines aktuellen Auftrags für den Supervisor.
 * Sie erlaubt u.a. die Eingabe der Arbeitszeit, das Anzeigen von Zusatzinformationen
 * sowie das Abschließen (Fertigmelden) des Auftrags.
 */
public class SupervisorCurrentJobActivity extends AppCompatActivity {

    /**
     * Überschreibt das Verhalten des physischen Zurück-Buttons.
     * So wird sichergestellt, dass die Navigation ausschließlich über die UI erfolgt.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_current_job);

        // --------------------- UI-Elemente verknüpfen ---------------------
        Button backToSupervisorPage_Button_S = findViewById(R.id.backToSupervisorPage_Button_S);
        Button chat_Supervisor_Button_S = findViewById(R.id.chat_Button);
        Button showDialogMoreInformationButton_S = findViewById(R.id.moreInformationElevatedButton);
        Button finishJobButton = findViewById(R.id.jobDoneButton);
        EditText editText = findViewById(R.id.workTime_S);
        // -----------------------------------------------------------------

        // ----------------- Eingabe auf max. 10 Stunden beschränken -----------------
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                try {
                    String result = dest.subSequence(0, dstart)
                            + source.toString()
                            + dest.subSequence(dend, dest.length());
                    int input = Integer.parseInt(result);
                    if (input >= 0 && input <= 10) {
                        return null; // gültig
                    }
                } catch (NumberFormatException ignored) {
                }
                return ""; // ungültig -> Eingabe blockieren
            }
        };
        editText.setFilters(new InputFilter[]{inputFilter});
        // ---------------------------------------------------------------------------

        // ----------------- Auftrag-Daten aus Intent empfangen -----------------
        Intent intent = getIntent();
        String customerName = intent.getStringExtra("customerName");
        String customerAddress = intent.getStringExtra("customerAddress");
        String einsatzgrund = intent.getStringExtra("einsatzgrund");
        String mitarbeiter = intent.getStringExtra("mitarbeiter");
        String prioritaet = intent.getStringExtra("prioritaet");
        final String[] weitereInformationen = {intent.getStringExtra("weitereInformationen")};
        String jobId = intent.getStringExtra("id");
        String orderNumber = intent.getStringExtra("orderNumber");
        // ----------------------------------------------------------------------

        // ------------------- Daten in TextViews anzeigen -----------------------
        ((TextView) findViewById(R.id.textView_customer_S)).setText(customerName);
        ((TextView) findViewById(R.id.textView_address_S)).setText(customerAddress);
        ((TextView) findViewById(R.id.textView_Einsatzgrund_S)).setText(einsatzgrund);
        ((TextView) findViewById(R.id.textView_Worker_S)).setText(mitarbeiter);
        ((TextView) findViewById(R.id.textView_Priority_S)).setText(prioritaet);
        // ----------------------------------------------------------------------

        // -------------- Navigation: Zurück zur Hauptseite ----------------------
        backToSupervisorPage_Button_S.setOnClickListener(v -> {
            Intent backIntent = new Intent(SupervisorCurrentJobActivity.this, SupervisorActivity.class);
            startActivity(backIntent);
            finish();
        });
        // ----------------------------------------------------------------------

        // ---------------- Chat-Button: zur AddOn-Page wechseln -----------------
        chat_Supervisor_Button_S.setOnClickListener(v -> {
            Intent addonIntent = new Intent(SupervisorCurrentJobActivity.this, SuperVisorAddOnButtonsActivity.class);
            startActivity(addonIntent);
        });
        // ----------------------------------------------------------------------

        // ----------- Button: Weitere Informationen im Dialog anzeigen ----------
        showDialogMoreInformationButton_S.setOnClickListener(v -> {
            if (weitereInformationen[0] == null || weitereInformationen[0].trim().isEmpty()) {
                weitereInformationen[0] = "Keine weiteren Informationen zum Auftrag vorhanden.";
            }

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Weitere Informationen")
                    .setMessage(weitereInformationen[0])
                    .setPositiveButton("OK", null)
                    .show();
        });
        // ----------------------------------------------------------------------

        // ------------ Auftrag abschließen & in Datenbank verschieben -----------
        finishJobButton.setOnClickListener(v -> {
            String auftragsdauer = editText.getText().toString().trim();

            if (auftragsdauer.isEmpty()) {
                Toast.makeText(this, "Bitte die geleistete Arbeitszeit eingeben!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Neues Auftrag-Objekt mit Dauer erzeugen
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

            // Referenzen in Firebase
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            DatabaseReference currentRef = db.child("auftraege").child(jobId);
            DatabaseReference historyRef = db.child("fertigeAuftraege").child(jobId);

            // Auftrag in "fertigeAuftraege" verschieben und aus "auftraege" löschen
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
        // ----------------------------------------------------------------------
    }
}
