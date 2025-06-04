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


public class EmployeeWorkHistoryActivity extends AppCompatActivity {

    private MitarbeiterAuftragAdapter adapter;

    // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Bitte den Zurück Button benutzen!", Toast.LENGTH_SHORT).show();
    }



    // Methode für das laden der Aufträge die dem angemeldeten Benutzer zugehörig sind
    private void ladeNurEigeneFertigeAuftraege(String mitarbeiterName) {
        DatabaseReference auftraegeRef = FirebaseDatabase.getInstance().getReference("fertigeAuftraege");

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
                Toast.makeText(EmployeeWorkHistoryActivity.this, "Fehler beim Laden der fertigen Aufträge", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Hier wird das Layout mit der Activity verknüpft
        setContentView(R.layout.activity_employee_workhistory);  // <- Der Name der XML-Datei aus res/layout

        // Button Zurück zur EmployeeActivity
        Button backButton_E = findViewById(R.id.backToEmployeePage_Button);
        backButton_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeWorkHistoryActivity.this, EmployeeActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });


        //_________________________________________________________________________________________________________________________
        // Nur die Aufträge aus der Firebase laden, welche dem angemeldeten Benutzer zugehörig sind
        //Variablen
        RecyclerView recyclerView = findViewById(R.id.doneOrderrecyclerView_E);  // Falls du ihn so benannt hast
        adapter = new MitarbeiterAuftragAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("userName");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String loginUserName = snapshot.getValue(String.class);
                if (loginUserName != null) {
                    // Leerzeichen-Version erzeugen → "Alban Demirci"
                    String readableName = loginUserName.replace(".", " ");
                    ladeNurEigeneFertigeAuftraege(readableName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeWorkHistoryActivity.this, "Fehler beim Laden des Benutzers", Toast.LENGTH_SHORT).show();
            }
        });

        //_________________________________________________________________________________________________________________________

    }
}
