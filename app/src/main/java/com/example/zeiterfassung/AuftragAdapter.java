package com.example.zeiterfassung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AuftragAdapter extends RecyclerView.Adapter<AuftragAdapter.AuftragViewHolder> {

    private final List<Auftrag> auftragList;

    public AuftragAdapter(List<Auftrag> auftragList) {
        this.auftragList = auftragList;
    }



    public static class AuftragViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView, customerNameTextView, einsatzgrundTextView, addressTextView, mitarbeiterTextView;
        View cardBackground;

        public AuftragViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumberTextView = itemView.findViewById(R.id.orderNumberText);
            einsatzgrundTextView = itemView.findViewById(R.id.reasonForOrderText);
            customerNameTextView = itemView.findViewById(R.id.customerText);
            addressTextView = itemView.findViewById(R.id.addressText);
            mitarbeiterTextView = itemView.findViewById(R.id.cardItem_mitarbeiter);
            cardBackground = itemView.findViewById(R.id.cardViewBackground);
        }
    }

    @Override
    public AuftragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new AuftragViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuftragViewHolder holder, int position) {
        Auftrag auftrag = auftragList.get(position);

        holder.orderNumberTextView.setText("Auftragsnummer: " + auftrag.getOrderNumber());
        holder.einsatzgrundTextView.setText("Einsatzgrund: " + auftrag.getEinsatzgrund());
        holder.customerNameTextView.setText("Kundenname: " + auftrag.getCustomerName());
        holder.addressTextView.setText("Einsatzort: " + auftrag.getCustomerAddress());
        holder.mitarbeiterTextView.setText("Mitarbeiter: " + auftrag.getMitarbeiter());

        // Lange die Card berühren, um die ausgewählte card zu löschen
        holder.itemView.setOnLongClickListener(v -> {
            Context context = v.getContext();

            new AlertDialog.Builder(context)
                    .setTitle("Auftrag löschen")
                    .setMessage("Möchten Sie diesen Auftrag wirklich löschen?")
                    .setPositiveButton("Löschen", (dialog, which) -> {
                        // Hier wird der Auftrag aus der Datenbank gelöscht
                        String auftragId = auftrag.getId();

                        FirebaseDatabase.getInstance()
                                .getReference("auftraege")
                                .child(auftragId)
                                .removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Auftrag gelöscht", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Fehler beim Löschen", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Abbrechen", null)
                    .show();

            return true; // LongClick wurde behandelt
        });






        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, SupervisorCurrentJobActivity.class);
            intent.putExtra("id", auftrag.getId());
            intent.putExtra("orderNumber", auftrag.getOrderNumber());
            intent.putExtra("customerName", auftrag.getCustomerName());
            intent.putExtra("customerAddress", auftrag.getCustomerAddress());
            intent.putExtra("einsatzgrund", auftrag.getEinsatzgrund());
            intent.putExtra("mitarbeiter", auftrag.getMitarbeiter());
            intent.putExtra("prioritaet", auftrag.getPrioritaet());
            intent.putExtra("weitereInformationen", auftrag.getMoreInformation());
            context.startActivity(intent);

            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        });

        // Kartenfarbe nach Priorität setzen
        String prioritaet = auftrag.getPrioritaet();
        if (prioritaet != null) {
            switch (prioritaet.trim().toLowerCase()) {
                case "hoch":
                    holder.cardBackground.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Hoch));
                    break;
                case "mittel":
                    holder.cardBackground.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Mittel));
                    break;
                case "niedrig":
                    holder.cardBackground.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Niedrig));
                    break;
                default:
                    holder.cardBackground.setBackgroundColor(Color.WHITE);
                    Log.d("PrioritaetDebug", "Unbekannte Priorität: " + prioritaet);
            }
        }
    }

    @Override
    public int getItemCount() {
        return auftragList.size();
    }



}
