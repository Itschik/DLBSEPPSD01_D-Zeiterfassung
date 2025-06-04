package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class SupervisorNewJobActivity extends AppCompatActivity {


    /** @noinspection deprecation*/ // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen.", Toast.LENGTH_SHORT).show();
    }

    //__________________________________________________________________________________
    //Variablen
    AutoCompleteTextView dropdown_einsatzgrund;
    AutoCompleteTextView dropdown_mitarbeiter;
    AutoCompleteTextView dropdown_prioritaet;
    private DatabaseReference auftragRef;

    //__________________________________________________________________________________



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_new_job);

        //__________________________________________________________________________________
        //Google Firebase referenzieren mit dem Pfad für die Aufträge
        auftragRef = FirebaseDatabase.getInstance().getReference("auftraege");
        //__________________________________________________________________________________


        //__________________________________________________________________________________
        // Verbindung des XML-Element mit dem Code
        // Dropdowns
        dropdown_einsatzgrund = findViewById(R.id.AutoCompleteTextView_einsatzgrund);
        dropdown_mitarbeiter = findViewById(R.id.AutoCompleteTextView_mitarbeiter);
        dropdown_prioritaet = findViewById(R.id.AutoCompleteTextView_prioritaet);
        // Buttons
        Button backToSupervisorPageButton_S = findViewById(R.id.backToSupervisorPage_Button);
        Button jobDoneButton_S = findViewById(R.id.jobDoneButton);
        // Textfelder
        EditText orderNumberInput = findViewById(R.id.ordernumber_Input);
        EditText customerNameInput = findViewById(R.id.customerName_Input);
        EditText customerAddressInput = findViewById(R.id.customerAddress_Input);
        EditText moreInformationInput = findViewById(R.id.moreInformation_Input);
        //__________________________________________________________________________________








        //__________________________________________________________________________________
        // Inhalte für die Dropdown-Menüs festlegen
        // Einsatzgründe zur Auswahl
        String[] einsatzgrund = {"Defekt", "Wartung", "Störung", "Reparatur", "Installation", "Prüfung"};
        // Mitarbeiter zur Auswahl
        String[] mitarbeiter = {"Philipp Neumann", "Alban Demirci", "Carlson Diego"};
        // Mitarbeiter zur Auswahl
        String[] prioritaet = {"Hoch", "Mittel", "Niedrig"};
        //__________________________________________________________________________________



        //__________________________________________________________________________________
        // Adapter erstellen für den Array des Dropdown Menüs
        // Einsatzgrund
        ArrayAdapter<String> adapter_einsatzgrund = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                einsatzgrund
        );

        // Mitarbeiter
        ArrayAdapter<String> adapter_mitarbeiter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                mitarbeiter
        );

        // Priorität
        ArrayAdapter<String> adapter_prioritaet = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                prioritaet
        );

        // Adapter mit der Dropdown-Komponente verbinden
        dropdown_einsatzgrund.setAdapter(adapter_einsatzgrund);
        dropdown_mitarbeiter.setAdapter(adapter_mitarbeiter);
        dropdown_prioritaet.setAdapter(adapter_prioritaet);

        // Optional: Keine freie Texteingabe erlauben für Einsatzgrund
        dropdown_einsatzgrund.setInputType(InputType.TYPE_NULL);
        dropdown_einsatzgrund.setKeyListener(null);

        // Optional: Keine freie Texteingabe erlauben für Mitarbeiter
        dropdown_mitarbeiter.setInputType(InputType.TYPE_NULL);
        dropdown_mitarbeiter.setKeyListener(null);

        // Optional: Keine freie Texteingabe erlauben für Priorität
        dropdown_prioritaet.setInputType(InputType.TYPE_NULL);
        dropdown_prioritaet.setKeyListener(null);
        //__________________________________________________________________________________


        //__________________________________________________________________________________
        // Zurück Button zur Supervisor Activity
        backToSupervisorPageButton_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zurück zur LoginActivity
                Intent intent = new Intent(SupervisorNewJobActivity.this, SupervisorActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });


        // OK Button für Speicherung in Firebase und zurück zur Supervisor Activity
        jobDoneButton_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Eingabefelder, die validiert werden müssen, es darf kein Feld leer sein
                String orderNumber = orderNumberInput.getText().toString().trim();
                String customerName = customerNameInput.getText().toString().trim();
                String customerAddress = customerAddressInput.getText().toString().trim();
                String einsatzGrund = dropdown_einsatzgrund.getText().toString().trim();
                String mitarbeiter = dropdown_mitarbeiter.getText().toString().trim();
                String prioritaet = dropdown_prioritaet.getText().toString().trim();
                //Außer das hier:
                String moreInformation = moreInformationInput.getText().toString().trim();

                // Prüfen, ob alle Pflichtfelder ausgefüllt sind, wenn ja, dann speichern, sonst nicht.
                if (orderNumber.isEmpty() || customerName.isEmpty() || customerAddress.isEmpty() || einsatzGrund.isEmpty() || mitarbeiter.isEmpty() || prioritaet.isEmpty()) {
                    Toast.makeText(SupervisorNewJobActivity.this, "Bitte füllen Sie alle Felder aus", Toast.LENGTH_SHORT).show();
                } else {

                    // ID für Firebase erzeugen
                    String id = auftragRef.push().getKey();

                    Auftrag neuerAuftrag = new Auftrag(id, orderNumber, customerName, customerAddress, einsatzGrund, mitarbeiter, prioritaet, moreInformation);

                    // In Firebase speichern
                    auftragRef.child(id).setValue(neuerAuftrag)
                            .addOnSuccessListener(aVoid -> {
                                // Zur Liste wechseln
                                Intent intent = new Intent(SupervisorNewJobActivity.this, SupervisorActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(SupervisorNewJobActivity.this, "Fehler beim Speichern", Toast.LENGTH_SHORT).show();
                            });
            }
        }




    });
        //__________________________________________________________________________________


    }
}
