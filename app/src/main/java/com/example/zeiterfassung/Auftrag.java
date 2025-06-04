package com.example.zeiterfassung;


import java.util.Arrays;

public class Auftrag {
    private String id;
    private String orderNumber;
    private String moreInformation;

    private String customerName;
    private String customerAddress;
    private String einsatzgrund;
    private String mitarbeiter;
    private String prioritaet;
    private String auftragsDauer;


    // Leerer Konstruktor für Firebase
    public Auftrag() {

    }

    // Konstruktor für den neu anzulegenden Auftrag
    public Auftrag(String id, String ordernumber, String customerName, String customerAddress,String einsatzgrund, String mitarbeiter, String prioritaet, String moreInformation) {
        this.id = id;
        this.orderNumber = ordernumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.einsatzgrund = einsatzgrund;
        this.mitarbeiter = mitarbeiter;
        this.prioritaet = prioritaet;
        this.moreInformation = moreInformation;

    }

    // Konstruktor für den zu verschiebenden Auftrag
    public Auftrag(String id, String ordernumber, String customerName, String customerAddress,String einsatzgrund, String mitarbeiter, String prioritaet, String[] moreInformation, String auftragsdauer) {
        this.id = id;
        this.orderNumber = ordernumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.einsatzgrund = einsatzgrund;
        this.mitarbeiter = mitarbeiter;
        this.prioritaet = prioritaet;
        this.moreInformation = Arrays.toString(moreInformation);
        this.auftragsDauer = auftragsdauer;

    }




    // Getter & Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getEinsatzgrund() {
        return einsatzgrund;
    }

    public void setEinsatzgrund(String einsatzgrund) {
        this.einsatzgrund = einsatzgrund;
    }

    public String getMitarbeiter() {
        return mitarbeiter;
    }

    public void setMitarbeiter(String mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }

    public String getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(String prioritaet) {
        this.prioritaet = prioritaet;
    }

    public String getMoreInformation() {
        return moreInformation;
    }

    public void setMoreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
    }

    public String getAuftragsDauer() {
        return auftragsDauer;
    }

    public void setAuftragsDauer(String auftragsDauer) {
        this.auftragsDauer = auftragsDauer;
    }

}
