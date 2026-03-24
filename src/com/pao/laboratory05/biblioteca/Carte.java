package com.pao.laboratory05.biblioteca;

public class Carte implements Comparable<Carte>{
    private String titlu;
    private String autor;
    private int an;
    private double rating;

    Carte (String t, String a, int an, double r){
        this.titlu = t;
        this.autor = a;
        this.an  = an;
        this.rating = r;
    }
    public String getTitlu(){return this.titlu;}
    public String getAutor(){return this.autor;}
    public int getAn(){return this.an;}
    public double getRating(){return this.rating;}

    @Override
    public String toString(){
        return "Carte{titlu='" + titlu + "', autor='" + autor + "', an=" + an + ", rating=" + rating + "}";
    }

    @Override
    public int compareTo(Carte alta){
        return Double.compare(alta.rating, this.rating);
    }

}
