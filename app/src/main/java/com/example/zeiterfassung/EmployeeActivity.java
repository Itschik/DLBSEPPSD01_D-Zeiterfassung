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


public class EmployeeActivity extends AppCompatActivity {

    // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Bitte den Abmelden Button benutzen!", Toast.LENGTH_SHORT).show();
    }

    private MitarbeiterAuftragAdapter adapter;

// Methode für das laden der Aufträge die dem angemeldeten Benutzer zugehörig sind
    private void ladeNurEigeneAuftraege(String mitarbeiterName) {
        DatabaseReference auftraegeRef = FirebaseDatabase.getInstance().getReference("auftraege");

        auftraegeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Auftrag> eigeneAuftraege = new ArrayList<>();

                for (DataSnapshot auftragSnap : snapshot.getChildren()) {
                    Auftrag auftrag = auftragSnap.getValue(Auftrag.class);
                    if (auftrag != null && auftrag.getMitarbeiter().equalsIgnoreCase(mitarbeiterName)) {
                        eigeneAuftraege.add(auftrag);
                    }
                }

                // RecyclerView aktualisieren
                adapter.setAuftraege(eigeneAuftraege); // Methode im Adapter
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
        // Hier wird das Layout mit der Activity verknüpft
        setContentView(R.layout.activity_employee);  // <- Der Name der XML-Datei aus res/layout

        //_________________________________________________________________________________________________________________________
        // Nur die Aufträge aus der Firebase laden, welche dem angemeldeten Benutzer zugehörig sind
        //Variablen
        RecyclerView recyclerView = findViewById(R.id.EmployeeCurrentJobrecyclerView);
        adapter = new MitarbeiterAuftragAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("userName");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String loginUserName = snapshot.getValue(String.class); // z. B. "Alban.Demirci"
                if (loginUserName != null) {
                    // Leerzeichen-Version erzeugen → "Alban Demirci"
                    String readableName = loginUserName.replace(".", " ");
                    ladeNurEigeneAuftraege(readableName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeActivity.this, "Fehler beim Laden des Benutzers", Toast.LENGTH_SHORT).show();
            }
        });

        //_________________________________________________________________________________________________________________________


        // Zuweisung der Variablen
        Button logoutButton_E = findViewById(R.id.backToLoginPage_employee_Button);
        Button backButton_E = findViewById(R.id.workHistory_Employee_Button);


        // Abmelden Button - Abmeldefunktion
        logoutButton_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zurück zur LoginActivity
                Intent intent = new Intent(EmployeeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });


        // Button für zurück zur Mitarbeiteransicht
        backButton_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zurück zur Mitarbeiteransicht
                Intent intent = new Intent(EmployeeActivity.this, EmployeeWorkHistoryActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });

    }
}
