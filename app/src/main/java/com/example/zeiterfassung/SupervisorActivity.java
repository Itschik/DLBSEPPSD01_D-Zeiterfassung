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



public class SupervisorActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Variablen
    private RecyclerView recyclerView;
    private AuftragAdapter adapter;
    private List<Auftrag> auftragList;

    private DatabaseReference auftragRef;
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------



    /** @noinspection deprecation*/ // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den Abmelden Button benutzen!", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Hier wird das Layout mit der Activity verknüpft
        setContentView(R.layout.activity_supervisor);  // <- Der Name der XML-Datei aus res/layout

        // Verknüpfung der XML-Elemente mit dem Code
        Button logoutButton_S = findViewById(R.id.backToLoginPage_supervisor_Button);
        Button addNewJobButton_S = findViewById(R.id.addJob_Button);
        Button goToWorkHistorySupvervisorButton_S = findViewById(R.id.goToWorkHistory_Supervisor_Button);


        //----------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Aufträge aus der Firebase laden und als Card in die Recyclerview einfügen
        recyclerView = findViewById(R.id.SupervisorCurrentJobrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        auftragList = new ArrayList<>();
        adapter = new AuftragAdapter(auftragList);
        recyclerView.setAdapter(adapter);

        auftragRef = FirebaseDatabase.getInstance().getReference("auftraege");

        // Listener, der die Daten aus Firebase liest
        auftragRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                auftragList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Auftrag auftrag = dataSnapshot.getValue(Auftrag.class);
                    if (auftrag != null) {
                        auftragList.add(auftrag);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SupervisorActivity.this, "Fehler beim Laden der Aufträge", Toast.LENGTH_SHORT).show();
            }
        });
        //----------------------------------------------------------------------------------------------------------------------------------------------------------------



        // Zurück Button zur Login Activity
        logoutButton_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zurück zur LoginActivity
                Intent intent = new Intent(SupervisorActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });

        // Button zur Activity für neue Auftragsdaten - Supervisor
        addNewJobButton_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zur Activity "Neue Auftragsdaten"
                Intent intent = new Intent(SupervisorActivity.this, SupervisorNewJobActivity.class);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });

        // Button zur Activity für fertige Aufträge - Supervisor
        goToWorkHistorySupvervisorButton_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zur Activity "fertige Aufträge"
                Intent intent = new Intent(SupervisorActivity.this, SupervisorWorkHistoryActivity.class);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });


    }
}
