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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Activity zeigt dem angemeldeten Mitarbeiter seine fertigen Aufträge an.
 * Daten werden aus Firebase Realtime Database geladen und in einem RecyclerView dargestellt.
 */
public class EmployeeWorkHistoryActivity extends AppCompatActivity {

    private MitarbeiterAuftragAdapter adapter;

    // Verhindert, dass der Benutzer mit dem Zurück-Button versehentlich zurücknavigiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den Zurück Button benutzen!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Lädt alle fertigen Aufträge aus Firebase, die vom aktuell eingeloggten Mitarbeiter stammen
     * @param mitarbeiterName Der lesbare Benutzername (z. B. "Max Mustermann")
     */
    private void ladeNurEigeneFertigeAuftraege(String mitarbeiterName) {
        DatabaseReference auftraegeRef = FirebaseDatabase.getInstance().getReference("fertigeAuftraege");

        auftraegeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Auftrag> eigeneAuftraege = new ArrayList<>();

                // Alle fertigen Aufträge durchgehen und nur die eigenen einsammeln
                for (DataSnapshot auftragSnap : snapshot.getChildren()) {
                    Auftrag auftrag = auftragSnap.getValue(Auftrag.class);
                    if (auftrag != null && auftrag.getMitarbeiter().equalsIgnoreCase(mitarbeiterName)) {
                        eigeneAuftraege.add(auftrag);
                    }
                }

                // Daten an den Adapter übergeben und Liste aktualisieren
                adapter.setAuftraege(eigeneAuftraege);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeWorkHistoryActivity.this, "Fehler beim Laden der fertigen Aufträge", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Wird beim Erstellen der Activity aufgerufen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout mit dieser Activity verknüpfen
        setContentView(R.layout.activity_employee_workhistory);

        // Zurück-Button initialisieren → führt zur EmployeeActivity zurück
        Button backButton_E = findViewById(R.id.backToEmployeePage_Button);
        backButton_E.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeWorkHistoryActivity.this, EmployeeActivity.class);
            startActivity(intent);
            finish();  // Beendet diese Activity, um keine Rücknavigation zu erlauben
        });

        // RecyclerView aufbauen
        RecyclerView recyclerView = findViewById(R.id.doneOrderrecyclerView_E);
        adapter = new MitarbeiterAuftragAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Aktuellen Benutzer abrufen
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        // Benutzername aus Firebase holen
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(uid)
                .child("userName");

        // Benutzernamen auslesen und lesbare Version erzeugen
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String loginUserName = snapshot.getValue(String.class);
                if (loginUserName != null) {
                    String readableName = loginUserName.replace(".", " ");  // z.B. "max.mustermann" → "max mustermann"
                    ladeNurEigeneFertigeAuftraege(readableName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeWorkHistoryActivity.this, "Fehler beim Laden des Benutzers", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
