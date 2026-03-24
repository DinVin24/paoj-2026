package com.pao.laboratory05.playlist;

import java.util.Arrays;
import java.util.List;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];

    Playlist(String name){
        this.name = name;
    }

    String getName(){
        return this.name;
    }

    void addSong(Song song){
        Song[] newlist = new Song[songs.length+1];
        System.arraycopy(this.songs, 0, newlist, 0 ,this.songs.length);
        newlist[songs.length] = song;
        this.songs = newlist;
    }

    void printSortedByTitle(){
        Song[] copy = this.songs.clone();
        Arrays.sort(copy);
        for(Song s : copy)
            System.out.println(s);
    }
    void printSortedByDuration(){
        Song[] copy = this.songs.clone();
        Arrays.sort(copy,new SongDurationComparator());
        for(Song s : copy)
            System.out.println(s);
    }
    int getTotalDuration(){
        int total = 0;
        for(int i=0;i<songs.length;i++)
            total += songs[i].durationSeconds();
        return total;
    }
}

