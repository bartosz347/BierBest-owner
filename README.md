## BierBest-owner

### Opis
Aplikacja wspiera proces zamawiania piwa przez sieć.
 Użytkownik korzystając z aplikacji "klient" (tworzona przez kolegę),
 rejestruje się i może złożyć zamówienie na wybrane przez siebie piwo.  
 Następnie właściciel sklepu potwierdza, czy może takie piwo zamówić w hurtowni oraz określa cenę.  
 Klient akceptuje lub odrzuca ofertę.
    
 
 Właściciel sklepu korzysta z aplikacji "serwer" (w tym repozytorium). Zarządza ona ona bazą klientów i zamówień. Każdy użytkownik ma swoje konto (nazwa użytkownika i hasło).  
 Aplikacja umożliwia wyświetlenie list zamówień, zaproponowanie klientowi ceny i zmianę statusów zamówienia.  
 Dane przesyłane między klientami a serwerem są szyfrowane (SSL/TLS), komunikacja odbywa się za pomocą socketów.  
 Aplikacja serwerowa korzysta z Hibernate'a i za jego pomocą przechowuje dane w bazie MySQL.


### Testy
Testy jednostkowe sprawdzają i demonstrują działanie komunikacji między serwerem a klientami.  

    ./gradlew test  


### Uruchomienie programu
 - projekt Gradle/IntelliJ
 - Argumenty programu `<db_address> <db_username> <db_password> <keystore_path> <keystore_password> [simulated]`
 - Opcjonalnie parametr VM `-Dbierbest.communication.port=<port_number>`

 
 `keystore_path` - scieżka do certyfikatu z kluczem w formacie PKCS#12  
 `keystore_password` - hasło do certyfikatu  
 
 Dodanie `simulated` jako ostatni parametr spowoduje usunięcie i stworzenie na nowo tabel w bazie oraz uruchomienie symulatora klienta przy starcie programu  
 Bez parametru `simulated`, aplikacja stworzy tabele w bazie tylko, jeśli nie istnieją
 
   
 
 
      
 