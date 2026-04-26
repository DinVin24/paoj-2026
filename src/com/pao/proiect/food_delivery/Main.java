package com.pao.proiect.food_delivery;

import com.pao.proiect.food_delivery.exception.ComandaNuPoateAnulataException;
import com.pao.proiect.food_delivery.model.*;
import com.pao.proiect.food_delivery.service.*;

import java.util.Arrays;
import java.util.List;

/**
 * Adauga un restaurant nou pe platforma
 * Inregistreaza un client nou
 * Inregistreaza un sofer nou
 * Adauga un preparat in meniul unui restaurant
 * Plaseaza o comanda
 * Asigneaza un sofer la o comanda
 * Cauta restaurante dupa tipul de bucatarie
 * Listeaza comenzile active ale unui sofer
 * Vizualizeaza istoricul comenzilor unui client
 * Anuleaza o comanda
 */
public class Main {

    public static void main(String[] args) {

        ServiciuRestaurant serviciuRestaurant = ServiciuRestaurant.getInstance();
        ServiciuClient serviciuClient = ServiciuClient.getInstance();
        ServiciuComanda serviciuComanda = ServiciuComanda.getInstance();

        // 1. Adauga restaurante
        Restaurant r1 = new Restaurant(1, "Bella Italia", "Italiana",
                new Adresa("Calea Victoriei 10", "Bucuresti", "010011", "Romania"));
        Restaurant r2 = new Restaurant(2, "Sushi Palace", "Japoneza",
                new Adresa("Strada Lipscani 5", "Bucuresti", "030011", "Romania"));
        Restaurant r3 = new Restaurant(3, "Burger Bros", "Americana",
                new Adresa("Bulevardul Unirii 3", "Bucuresti", "040011", "Romania"));
        serviciuRestaurant.adauga(r1);
        serviciuRestaurant.adauga(r2);
        serviciuRestaurant.adauga(r3);
        System.out.println("Restaurante adaugate (sortate alfabetic):");
        serviciuRestaurant.listeazaTot().forEach(r -> System.out.println("  " + r));

        // 2. Inregistreaza clienti
        Client c1 = new Client(1, "Ana Ionescu", "ana@mail.com", "0721000001",
                new Adresa("Str. Florilor 1", "Bucuresti", "010001", "Romania"));
        Client c2 = new Client(2, "Mihai Popescu", "mihai@mail.com", "0721000002",
                new Adresa("Str. Lalelelor 2", "Bucuresti", "010002", "Romania"));
        serviciuClient.adauga(c1);
        serviciuClient.adauga(c2);
        System.out.println("Clienti inregistrati:");
        serviciuClient.listeazaTot().forEach(c -> System.out.println("  " + c));

        // 3. Inregistreaza un sofer
        Sofer s1 = new Sofer(1, "Ion Vasile", "ion@sofer.com", "0723000001",
                new Adresa("Str. Motilor 7", "Bucuresti", "020001", "Romania"),
                "Motocicleta", "B-00-ION");
        System.out.println("Sofer inregistrat: " + s1);

        // 4. Adauga preparate in meniuri
        Preparat pizza = new Preparat(1, "Pizza Margherita", "Rosie si mozzarella clasica", 32.00);
        Preparat paste = new Preparat(2, "Paste Carbonara", "Paste cremoase cu bacon", 28.50);
        Preparat sushi = new Preparat(3, "Rulou Somon", "8 bucati rulou somon", 45.00);
        Preparat edamame = new Preparat(4, "Edamame", "Boabe de soia cu sare", 15.00);
        Preparat burger = new Preparat(5, "Burger Clasic", "Cotlet vita 200g", 35.00);
        serviciuRestaurant.adaugaPreparat(r1.getId(), pizza);
        serviciuRestaurant.adaugaPreparat(r1.getId(), paste);
        serviciuRestaurant.adaugaPreparat(r2.getId(), sushi);
        serviciuRestaurant.adaugaPreparat(r2.getId(), edamame);
        serviciuRestaurant.adaugaPreparat(r3.getId(), burger);
        System.out.println("Meniu Bella Italia (sortat dupa pret):");
        serviciuRestaurant.getMeniu(r1.getId()).forEach(p -> System.out.println("  " + p));

        // 5. Plaseaza comenzi
        List<ArticolComanda> articole1 = Arrays.asList(
                new ArticolComanda(pizza, 1),
                new ArticolComanda(paste, 2));
        Comanda comanda1 = serviciuComanda.plaseazaComanda(c1, r1, articole1);

        List<ArticolComanda> articole2 = Arrays.asList(new ArticolComanda(burger, 1));
        Comanda comanda2 = serviciuComanda.plaseazaComanda(c2, r3, articole2);

        // 6. Asigneaza sofer la comanda
        serviciuComanda.asigneazaSofer(comanda1.getId(), s1);
        System.out.println("Sofer disponibil dupa asignare: " + s1.isDisponibil());

        // 7. Cauta restaurante dupa tipul de bucatarie
        String tipCautat = "Italiana";
        System.out.println("Restaurante cu bucatarie '" + tipCautat + "':");
        serviciuRestaurant.cautaDupaBucatarie(tipCautat)
                .forEach(r -> System.out.println("  " + r));

        // 8. Comenzile active ale soferului
        List<Comanda> comenziActive = serviciuComanda.listeazaDupaSofer(s1);
        if (comenziActive.isEmpty()) {
            System.out.println("  Nicio comanda activa.");
        } else {
            comenziActive.forEach(c -> System.out.println("  " + c));
        }

        // 9. Livreaza comanda si afiseaza istoricul clientului
        serviciuComanda.livreaza(comanda1.getId());
        System.out.println("Sofer disponibil dupa livrare: " + s1.isDisponibil());
        System.out.println("\nIstoricul comenzilor pentru clientul '" + c1.getNume() + "':");
        List<InregistrareComanda> istoric = serviciuComanda.getIstoricClient(c1.getId());
        if (istoric.isEmpty()) {
            System.out.println("  Nu exista comenzi finalizate.");
        } else {
            istoric.forEach(inr -> System.out.println("  " + inr));
        }

        // 10. Anuleaza comanda
        try {
            serviciuComanda.anuleaza(comanda2.getId());
            System.out.println("Status comanda #" + comanda2.getId() + ": " + comanda2.getStatus());

            System.out.println("\nIncercare de anulare a comenzii deja livrate #" + comanda1.getId() + "...");
            serviciuComanda.anuleaza(comanda1.getId());
        } catch (ComandaNuPoateAnulataException e) {
            System.out.println("  Exceptie asteptata prinsa: " + e.getMessage());
        }

        // STAREA FINALA
        System.out.println("Toate comenzile:");
        serviciuComanda.listeazaTot().forEach(c -> System.out.println("  " + c));
        System.out.println("\nDimensiune coada de asteptare: " + serviciuComanda.getCoadaAsteptare().size());
    }

}
