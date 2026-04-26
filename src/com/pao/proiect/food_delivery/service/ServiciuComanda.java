package com.pao.proiect.food_delivery.service;

import com.pao.proiect.food_delivery.exception.ComandaNuPoateAnulataException;
import com.pao.proiect.food_delivery.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class ServiciuComanda {

    private static ServiciuComanda instanta;

    private final Map<Integer, Comanda> comenzi = new HashMap<>();
    private final Queue<Comanda> coadaAsteptare = new LinkedList<>();
    private final Map<Integer, List<InregistrareComanda>> istoricClienti = new HashMap<>();
    private int urmatoareId = 1;

    private ServiciuComanda() {
    }

    public static ServiciuComanda getInstance() {
        if (instanta == null)
            instanta = new ServiciuComanda();
        return instanta;
    }

    public Comanda plaseazaComanda(Client client, Restaurant restaurant, List<ArticolComanda> articole) {
        if (client == null)
            throw new IllegalArgumentException("Clientul nu poate fi null");
        if (restaurant == null)
            throw new IllegalArgumentException("Restaurantul nu poate fi null");
        if (articole == null || articole.isEmpty())
            throw new IllegalArgumentException("Comanda trebuie sa aiba cel putin un articol");

        Comanda comanda = new Comanda(urmatoareId++, client, restaurant, articole);
        comenzi.put(comanda.getId(), comanda);
        coadaAsteptare.offer(comanda);
        System.out.println("  [ServiciuComanda] Comanda plasata: " + comanda);
        return comanda;
    }

    public void asigneazaSofer(int idComanda, Sofer sofer) {
        if (sofer == null)
            throw new IllegalArgumentException("Soferul nu poate fi null");
        Comanda comanda = getComanda(idComanda);
        if (comanda.getStatus() != StatusComanda.IN_ASTEPTARE)
            throw new IllegalStateException("Doar comenzile IN_ASTEPTARE pot primi un sofer");

        comanda.setSofer(sofer);
        comanda.setStatus(StatusComanda.ASIGNAT);
        sofer.setDisponibil(false);
        coadaAsteptare.remove(comanda);
        System.out.println("  [ServiciuComanda] Soferul '" + sofer.getNume() + "' asignat la comanda #" + idComanda);
    }

    public List<Comanda> listeazaDupaSofer(Sofer sofer) {
        if (sofer == null)
            return Collections.emptyList();
        return comenzi.values().stream()
                .filter(c -> sofer.equals(c.getSofer())
                        && c.getStatus() != StatusComanda.LIVRAT
                        && c.getStatus() != StatusComanda.ANULAT)
                .collect(Collectors.toList());
    }

    public List<InregistrareComanda> getIstoricClient(int idClient) {
        return istoricClienti.getOrDefault(idClient, Collections.emptyList());
    }

    public void anuleaza(int idComanda) {
        Comanda comanda = getComanda(idComanda);
        if (comanda.getStatus() == StatusComanda.IN_LIVRARE
                || comanda.getStatus() == StatusComanda.LIVRAT) {
            throw new ComandaNuPoateAnulataException(idComanda, comanda.getStatus().name());
        }
        if (comanda.getStatus() == StatusComanda.ANULAT) {
            System.out.println("  [ServiciuComanda] Comanda #" + idComanda + " este deja anulata.");
            return;
        }
        if (comanda.getSofer() != null)
            comanda.getSofer().setDisponibil(true);
        coadaAsteptare.remove(comanda);
        comanda.setStatus(StatusComanda.ANULAT);
        System.out.println("  [ServiciuComanda] Comanda #" + idComanda + " anulata.");
    }

    public InregistrareComanda livreaza(int idComanda) {
        Comanda comanda = getComanda(idComanda);
        comanda.setStatus(StatusComanda.LIVRAT);
        if (comanda.getSofer() != null)
            comanda.getSofer().setDisponibil(true);

        InregistrareComanda inregistrare = comanda.laInregistrare();
        istoricClienti.computeIfAbsent(comanda.getClient().getId(), k -> new ArrayList<>())
                .add(inregistrare);
        System.out.println("  [ServiciuComanda] Comanda #" + idComanda + " livrata. Inregistrare creata.");
        return inregistrare;
    }

    public Comanda gasesteDupaId(int idComanda) {
        return getComanda(idComanda);
    }

    public List<Comanda> listeazaTot() {
        return new ArrayList<>(comenzi.values());
    }

    public Queue<Comanda> getCoadaAsteptare() {
        return coadaAsteptare;
    }

    private Comanda getComanda(int idComanda) {
        Comanda c = comenzi.get(idComanda);
        if (c == null)
            throw new NoSuchElementException("Comanda cu id=" + idComanda + " nu a fost gasita");
        return c;
    }
}
