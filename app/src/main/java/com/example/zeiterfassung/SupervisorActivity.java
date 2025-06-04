package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class SupervisorActivity extends AppCompatActivity {

    private AuftragAdapter adapter;                  // Adapter für die RecyclerView
    private List<Auftrag> auftragList;               // Liste von Aufträgen (Modellklasse)

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * Sperrt den Hardware-Zurückbutton und zeigt einen Hinweis.
     * @noinspection deprecation
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den Abmelden Button benutzen!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout dieser Activity laden (res/layout/activity_supervisor.xml)
        setContentView(R.layout.activity_supervisor);

        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
        // UI-Elemente mit den zugehörigen IDs aus dem Layout verknüpfen
        Button logoutButton_S = findViewById(R.id.backToLoginPage_supervisor_Button);
        Button addNewJobButton_S = findViewById(R.id.addJob_Button);
        Button goToWorkHistorySupvervisorButton_S = findViewById(R.id.goToWorkHistory_Supervisor_Button);
        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------


        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
        // RecyclerView vorbereiten, um aktuelle Aufträge als Cards anzuzeigen

        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
        // UI-Komponenten und Datenquellen
        // Anzeigeelement für aktuelle Aufträge (Liste)
        RecyclerView recyclerView = findViewById(R.id.SupervisorCurrentJobrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Vertikale Liste

        auftragList = new ArrayList<>();               // Initialisiert leere Liste
        adapter = new AuftragAdapter(auftragList);     // Adapter mit dieser Liste verbinden
        recyclerView.setAdapter(adapter);              // Adapter an RecyclerView setzen

        // Firebase-Datenbankreferenz auf den Knoten "auftraege"
        DatabaseReference auftragRef = FirebaseDatabase.getInstance().getReference("auftraege"); // Firebase-Knoten "auftraege"

        // Firebase-Datenänderungen überwachen
        auftragRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                auftragList.clear(); // Vorherige Daten entfernen
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Auftrag auftrag = dataSnapshot.getValue(Auftrag.class);
                    if (auftrag != null) {
                        auftragList.add(auftrag); // Auftrag zur Liste hinzufügen
                    }
                }
                adapter.notifyDataSetChanged(); // UI aktualisieren
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Fehler beim Abruf der Daten
                Toast.makeText(SupervisorActivity.this, "Fehler beim Laden der Aufträge", Toast.LENGTH_SHORT).show();
            }
        });
        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------


        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Button: Abmelden → zurück zur LoginActivity
        logoutButton_S.setOnClickListener(v -> {
            Intent intent = new Intent(SupervisorActivity.this, LoginActivity.class);
            // Stack löschen, damit "Zurück" nicht wieder hierher führt
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Diese Activity beenden
        });

        // Button: Neuen Auftrag hinzufügen
        addNewJobButton_S.setOnClickListener(v -> {
            Intent intent = new Intent(SupervisorActivity.this, SupervisorNewJobActivity.class);
            startActivity(intent);
            finish(); // Aktuelle Activity beenden
        });

        // Button: Zur Auftrags-Historie
        goToWorkHistorySupvervisorButton_S.setOnClickListener(v -> {
            Intent intent = new Intent(SupervisorActivity.this, SupervisorWorkHistoryActivity.class);
            startActivity(intent);
            finish(); // Aktuelle Activity beenden
        });
        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    }
}
