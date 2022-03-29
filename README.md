# Smart-Dam

Si vuole realizzare un sistema IoT che implementi una versione semplificata di un sistema intelligente per il monitoraggio del livello idrometrico (*) di un fiume e controllo di una diga (dam).  In particolare, il sistema ha il principale compito di monitorare il livello dell’acqua in un punto del fiume e, in caso di situazione critica, controllare l’apertura di una diga per far defluire l’acqua (in apposite vasche di contenimento o nei campi).

##Remote Hydrometer (ESP) - abbreviato a seguire in RH
sistema embedded che ha la funzione di monitorare il livello idrometrico del fiume (si presuppone in un punto specifico) e interagire con la parte Service via HTTP oppure MQTT
Dam Service (backend - pc) - abbreviato a seguire in DS
servizio REST che funge da centralina di controllo del sistema. Interagisce via seriale con il Controller (arduino) e via HTTP o MQTT con il Remote Hydrometer (ESP) e con la Dashboard (frontend/PC).

##Dam Controller (Arduino) - abbreviato a seguire in DC
sistema embedded che controlla la diga. Interagisce via seriale con il Service e via Bluetooth con la parte Mobile App, usata dagli operatori.

##Dam App (Android - smartphone) abbreviato a seguire in DM
Mobile app che ad un operatore di poter agire direttamente sulla diga, essendo nelle prossimità. Interagisce con la parte Controller (via Bluetooth) ed eventualmente con la parte Service (via HTTP).

##Dam Dashboard (Frontend - web app o Client su PC) abbreviato a seguire in DD
Front end per visualizzazione/monitoraggio dati.
