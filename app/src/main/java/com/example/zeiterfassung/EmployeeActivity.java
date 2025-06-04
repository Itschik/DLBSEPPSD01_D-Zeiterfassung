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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity zur Anzeige der aktuellen Aufträge eines Mitarbeiters.
 * Zeigt nur Aufträge an, die dem aktuell angemeldeten Mitarbeiter zugeordnet sind.
 */
public class EmployeeActivity extends AppCompatActivity {

    /**
     * Verhindert, dass der Zurück-Button der Hardware die Activity verlässt.
     * Stattdessen wird eine Toast-Meldung angezeigt.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den Abmelden Button benutzen!", Toast.LENGTH_SHORT).show();
    }

    // Adapter für die RecyclerView, um die Aufträge darzustellen
    private MitarbeiterAuftragAdapter adapter;

    /**
     * Lädt aus der Firebase-Datenbank nur die Aufträge, die zum angegebenen Mitarbeiter gehören.
     * @param mitarbeiterName Name des Mitarbeiters (z.B. "Alban Demirci")
     */
    private void ladeNurEigeneAuftraege(String mitarbeiterName) {
        DatabaseReference auftraegeRef = FirebaseDatabase.getInstance().getReference("auftraege");

        auftraegeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Auftrag> eigeneAuftraege = new ArrayList<>();

                // Alle Aufträge durchgehen und nur die mit passendem Mitarbeiter auswählen
                for (DataSnapshot auftragSnap : snapshot.getChildren()) {
                    Auftrag auftrag = auftragSnap.getValue(Auftrag.class);
                    if (auftrag != null && auftrag.getMitarbeiter().equalsIgnoreCase(mitarbeiterName)) {
                        eigeneAuftraege.add(auftrag);
                    }
                }

                // RecyclerView-Adapter mit den gefilterten Aufträgen aktualisieren
                adapter.setAuftraege(eigeneAuftraege);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeActivity.this, "Fehler beim Laden der Aufträge", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Verknüpft die Activity mit dem Layout aus res/layout/activity_employee.xml
        setContentView(R.layout.activity_employee);

        // RecyclerView initialisieren und LayoutManager setzen (Linear, vertikal)
        RecyclerView recyclerView = findViewById(R.id.EmployeeCurrentJobrecyclerView);
        adapter = new MitarbeiterAuftragAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Aktuell angemeldeten Benutzer über Firebase Auth ermitteln
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        // Firebase Referenz zum Benutzernamen des angemeldeten Users
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("userName");

        // Einmaliger Listener zum Laden des Benutzernamens
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Benutzername auslesen (z.B. "Alban.Demirci")
                String loginUserName = snapshot.getValue(String.class);
                if (loginUserName != null) {

                    // Nur die Aufträge laden, die dem angemeldeten Benutzer gehören
                    ladeNurEigeneAuftraege(loginUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeActivity.this, "Fehler beim Laden des Benutzers", Toast.LENGTH_SHORT).show();
            }
        });

        // Buttons aus Layout finden
        Button logoutButton_E = findViewById(R.id.backToLoginPage_employee_Button);
        Button backButton_E = findViewById(R.id.workHistory_Employee_Button);

        // Abmelden-Button: Navigiert zurück zur LoginActivity und beendet diese Activity
        logoutButton_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Button für "Arbeitsverlauf" (History) - öffnet die Work History Activity
        backButton_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeActivity.this, EmployeeWorkHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
