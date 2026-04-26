# Platformă Food Delivery

## Acțiuni posibile în sistem (10)

1. Adaugă un restaurant nou pe platformă
2. Înregistrează un client nou
3. Înregistrează un șofer nou
4. Adaugă un preparat în meniul unui restaurant
5. Plasează o comandă (client selectează preparate dintr-un restaurant)
6. Asignează un șofer la o comandă
7. Caută restaurante după tipul de bucătărie
8. Listează comenzile active ale unui șofer
9. Vizualizează istoricul comenzilor unui client
10. Anulează o comandă

## Tipuri de obiecte

1. `Restaurant` — restaurant cu meniu și adresă
2. `Client` — client; extinde `Persoana`
3. `Sofer` — șofer; extinde `Persoana`
4. `Preparat` — preparat din meniu cu preț (sortabil)
5. `Comanda` — comandă plasată de un client
6. `ArticolComanda` — linie de comandă (preparat + cantitate)
7. `InregistrareComanda` — înregistrare **imutabilă** a unei comenzi finalizate
8. `Adresa` — adresa unui client sau restaurant
9. `Recenzie` — recenzie lăsată de un client pentru un restaurant
