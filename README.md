Projekt: Software Development DLBSEPPSD01_D Aufgabenstellung 1: Entwicklung einer mobilen App - Auftragsgebundene Zeit Erfassung für einen Handwerksbetrieb

Die SMS Funktion für diese Anwendung funktioniert nur, wenn die Handynummer im Code als Hardcode eingetragen ist. Aus Datenschutzgründen ist hier die Nummer entfernt worden.
Wenn man nun versucht, die Buttons für die SMS Funktion mit vordefiniertem Text zu verwenden, erscheint ein Toast, welcher mitteilt, dass der Empfänger ausgewählt werden soll.

Für den Test der App muss man im folgenden Klassen die Namen und Nummern hinzufügen:

1.) SuperVisorAddOnButtonsActivity.java
2.) EmployeeAddOnButtonsActivity.java

// Beispielhafte Namen für Empfängerauswahl
    String[] names = {"Robin" , "Benutzer"};
    String[] numbers = {"0123456", "123456789"};

    // WICHTIG: Hier sollte die Telefonnummern-Liste denselben Index wie die Namen haben
    // Hinweis: Im geposteten Code ist sie leer → SMS-Versand wird fehlschlagen, wenn dies nicht gefüllt ist
