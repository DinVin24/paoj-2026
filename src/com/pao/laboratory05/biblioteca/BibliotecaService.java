package com.pao.laboratory05.biblioteca;
import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];

    private BibliotecaService(){}

    private static class Holder{
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance(){
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte){
        Carte[] newArray = new Carte[carti.length + 1];
        System.arraycopy(this.carti, 0, newArray, 0, this.carti.length);
        newArray[this.carti.length] = carte;
        this.carti = newArray;
        System.out.println("Cartea adaugata: " + carte.getTitlu());
    }
    public void listSortedByRating(){
        Carte[] copy = this.carti.clone();
        Arrays.sort(copy);
        for (Carte c : copy)
            System.out.println(c);
    }

    public void listSortedBy (Comparator<Carte> comparator){
        Carte[] copy = this.carti.clone();
        Arrays.sort(copy,comparator);
        for (Carte c : copy){
            System.out.println(c);
        }
    }

}
