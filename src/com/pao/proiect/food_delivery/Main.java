package com.pao.proiect.food_delivery;

import com.pao.proiect.food_delivery.exception.ComandaNuPoateAnulataException;
import com.pao.proiect.food_delivery.model.*;
import com.pao.proiect.food_delivery.service.*;
import com.pao.proiect.food_delivery.util.DatabaseConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== PLATFORMA FOOD DELIVERY - Etapa II ===\n");

        try {
            initializeazaBazaDeDate();

            AuditService audit = AuditService.getInstance();
            ServiciuRestaurant serviciuRestaurant = ServiciuRestaurant.getInstance();
            ServiciuClient serviciuClient = ServiciuClient.getInstance();
            ServiciuComanda serviciuComanda = ServiciuComanda.getInstance();

            //1 Adauga restaurante
            Restaurant r1 = new Restaurant(1, "Bella Italia", "Italiana",
                    new Adresa("Calea Victoriei 10", "Bucuresti", "010011", "Romania"));
            Restaurant r2 = new Restaurant(2, "Sushi Palace", "Japoneza",
                    new Adresa("Strada Lipscani 5", "Bucuresti", "030011", "Romania"));
            Restaurant r3 = new Restaurant(3, "Burger Bros", "Americana",
                    new Adresa("Bulevardul Unirii 3", "Bucuresti", "040011", "Romania"));
            serviciuRestaurant.adauga(r1);
            serviciuRestaurant.adauga(r2);
            serviciuRestaurant.adauga(r3);
            audit.log("adauga_restaurant");
            System.out.println("1. Restaurante adaugate in SQLite.");

            //2 Inregistreaza clienti
            Client c1 = new Client(1, "Ana Ionescu", "ana@mail.com", "0721000001",
                    new Adresa("Str. Florilor 1", "Bucuresti", "010001", "Romania"));
            Client c2 = new Client(2, "Mihai Popescu", "mihai@mail.com", "0721000002",
                    new Adresa("Str. Lalelelor 2", "Bucuresti", "010002", "Romania"));
            serviciuClient.adauga(c1);
            serviciuClient.adauga(c2);
            audit.log("inregistreaza_client");
            System.out.println("2. Clienti inregistrati in SQLite.");

            //3 Inregistreaza sofer
            Sofer s1 = new Sofer(1, "Ion Vasile", "ion@sofer.com", "0723000001",
                    new Adresa("Str. Motilor 7", "Bucuresti", "020001", "Romania"),
                    "Motocicleta", "B-00-ION");
            new com.pao.proiect.food_delivery.repository.SoferRepository().save(s1);
            audit.log("inregistreaza_sofer");
            System.out.println("3. Sofer inregistrat in SQLite: " + s1);

            //4 Adauga preparate in meniuri
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
            audit.log("adauga_preparat");
            System.out.println("4. Meniu Bella Italia actualizat:");
            serviciuRestaurant.getMeniu(r1.getId()).forEach(p -> System.out.println("   " + p));

            //5 Plaseaza comenzi (TRANZACTIE)
            List<ArticolComanda> articole1 = Arrays.asList(
                    new ArticolComanda(pizza, 1),
                    new ArticolComanda(paste, 2));
            Comanda comanda1 = serviciuComanda.plaseazaComanda(c1, r1, articole1);

            List<ArticolComanda> articole2 = Arrays.asList(new ArticolComanda(burger, 1));
            Comanda comanda2 = serviciuComanda.plaseazaComanda(c2, r3, articole2);
            audit.log("plaseaza_comanda");

            //6 Asigneaza un sofer la comanda
            serviciuComanda.asigneazaSofer(comanda1.getId(), s1);
            audit.log("asigneaza_sofer");
            System.out.println("6. Soferul '" + s1.getNume() + "' asignat la comanda #" + comanda1.getId());

            //7 Cauta restaurante dupa tipul de bucatarie
            String tipCautat = "Italiana";
            List<Restaurant> restauranteBucatarie = serviciuRestaurant.cautaDupaBucatarie(tipCautat);
            audit.log("cauta_restaurante_bucatarie");
            System.out
                    .println("7. Restaurante cu bucatarie '" + tipCautat + "' (" + restauranteBucatarie.size() + "):");
            restauranteBucatarie.forEach(r -> System.out.println("   " + r));

            //8 Listeaza comenzile active ale unui sofer
            List<Comanda> comenziActiveSofer = serviciuComanda.listeazaDupaSofer(s1);
            audit.log("listeaza_comenzi_sofer");
            System.out.println("8. Comenzi active pentru '" + s1.getNume() + "':");
            comenziActiveSofer.forEach(c -> System.out.println("   " + c));

            //9 Vizualizeaza istoricul comenzilor unui client (LIVRARE)
            serviciuComanda.livreaza(comanda1.getId());
            List<InregistrareComanda> istoric = serviciuComanda.getIstoricClient(c1.getId());
            audit.log("get_istoric_client");
            System.out.println("9. Istoric livrari client '" + c1.getNume() + "':");
            istoric.forEach(inr -> System.out.println("   " + inr));

            //10 Anuleaza o comanda
            try {
                serviciuComanda.anuleaza(comanda2.getId());
                System.out.println(
                        "10. Comanda #" + comanda2.getId() + " anulata cu succes. Status: " + comanda2.getStatus());

                System.out.println("\nIncercam sa anulam comanda livrata #" + comanda1.getId() + "...");
                serviciuComanda.anuleaza(comanda1.getId());
            } catch (ComandaNuPoateAnulataException e) {
                System.out.println("   [Rollback & Exceptie confirmata] " + e.getMessage());
            }
            audit.log("anuleaza_comanda");

            System.out.println("\nJOIN 1: Comenzile active (neasignate sau asignate) cu detalii client si livrator:");
            serviciuComanda.getComenziActiveCuDetalii().forEach(linie -> System.out.println("  " + linie));

            System.out.println("\nJOIN 2: Top preparate cele mai des comandate cu numele restaurantelor:");
            serviciuComanda.getTopPreparateComandate().forEach(linie -> System.out.println("  " + linie));

            System.out.println("\nJOIN 3: Raport istoric complet detaliat pentru Client ID = 1:");
            serviciuComanda.getIstoricCompletClientRaport(1).forEach(linie -> System.out.println("  " + linie));

            DatabaseConnection.getInstance().close();

        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeazaBazaDeDate() throws IOException, SQLException {
        System.out.println("[DB INIT] Se ruleaza schema.sql pentru a crea tabelele...");
        Connection conn = DatabaseConnection.getInstance().getConnection();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/schema.sql"))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (!linie.trim().startsWith("--") && !linie.trim().isEmpty()) {
                    sb.append(linie).append(" ");
                }
            }
        }

        String[] comenziSql = sb.toString().split(";");
        try (Statement stmt = conn.createStatement()) {
            for (String sql : comenziSql) {
                if (!sql.trim().isEmpty()) {
                    stmt.execute(sql.trim());
                }
            }
            System.out.println("[DB INIT] Baza de date initializata cu succes.\n");
        }
    }
}
