# Androidprojekt: REvent

Bei Revent handelt es sich um eine Androidapp zum Eventmanagement.

Zum Starten des Servers müssen zuvor die Befehle "npm init", "npm install" und "npm start" im entsprechenden Ordner ...\REvent\REvent_server nausgeführt werden.

## Setup

Benötigte Software:
* npm

Nachdem das Projekt geklont wurde, kann die Datenbank über folgende Befehle installiert werden (auszuführen im Verzeichnis ...\REvent\REvent_server):

```
npm init //erstellt Projekt
npm install
```

Die Datenbank kann dann über den folgenden Befehl gestartet werden:

```
npm start
```
Diese ist dann über den Port 3000 erreichbar.

## Notiz JSON-Server

Um auf den JSON-Server auch über ein angeschlossenes Smartphone zugreifen zu können, muss diese auf der lokalen IP des Rechners im Netz gehostet werden. 

```
json-server --host 192.168.1.XXX db.json
```

Entsprechend muss auch die URL-Variable in der MainActivity angepasst werden.

Kann problematisch bei Mac sein.
