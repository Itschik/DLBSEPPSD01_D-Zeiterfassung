package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Activity zeigt dem Supervisor eine Übersicht aller abgeschlossenen Aufträge
 */
public class SupervisorWorkHistoryActivity extends AppCompatActivity {

    // -------------------------- Variablen zur Anzeige der abgeschlossenen Aufträge --------------------------
    private FertigerAuftragAdapter adapter;              // Adapter für RecyclerView
    private List<Auftrag> auftragsListe;                 // Liste der abgeschlossenen Aufträge
    // --------------------------------------------------------------------------------------------------------


    /**
     * Überschreibt den Standard-Zurück-Button (unten am Handy) und zeigt stattdessen eine Toast-Meldung.
     * So wird verhindert, dass der Benutzer versehentlich die Activity verlässt.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den Zurück Button benutzen!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Wird beim Start der Activity aufgerufen.
     * Setzt das Layout, initialisiert UI-Komponenten und lädt die Daten aus Firebase.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_workhistory);  // Layout mit der Activity verknüpfen

        // ---------------------- Zurück-Button (UI) zur Supervisor-Hauptseite ----------------------
        Button backButton_S = findViewById(R.id.backToSupervisorPageFromWorkhistorySupervisor_Button);
        backButton_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wechsel zur SupervisorActivity
                Intent intent = new Intent(SupervisorWorkHistoryActivity.this, SupervisorActivity.class);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity, damit sie nicht im Stack bleibt
            }
        });
        // -------------------------------------------------------------------------------------------

        // ---------------------- RecyclerView Setup für abgeschlossene Aufträge ----------------------
        // Liste zur Anzeige der Aufträge
        RecyclerView recyclerView = findViewById(R.id.doneOrderrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Vertikale Liste

        auftragsListe = new ArrayList<>();                            // Initialisierung der Auftragsliste
        adapter = new FertigerAuftragAdapter(auftragsListe);          // Adapter mit leerer Liste
        recyclerView.setAdapter(adapter);                             // Adapter an RecyclerView binden
        // --------------------------------------------------------------------------------------------

        // ---------------------- Daten aus Firebase "fertigeAuftraege" laden -------------------------
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("fertigeAuftraege");

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                auftragsListe.clear();  // Liste vorher leeren, um doppelte Daten zu vermeiden

                // Jeden Auftrag aus der Datenbank einlesen und zur Liste hinzufügen
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Auftrag auftrag = jobSnapshot.getValue(Auftrag.class);
                    if (auftrag != null) {
                        auftragsListe.add(auftrag);
                    }
                }

                // Anzeige aktualisieren, sobald alle Daten geladen wurden
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Fehlerbehandlung beim Lesen der Daten
                Toast.makeText(SupervisorWorkHistoryActivity.this, "Fehler beim Laden", Toast.LENGTH_SHORT).show();
            }
        });
        // --------------------------------------------------------------------------------------------
    }
}
