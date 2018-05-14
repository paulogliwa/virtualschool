/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirtualnaszkola;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Paulo
 */
public class Klasa implements Comparable {
    private String nauczyciel;
    private String nazwa;
    private String profil;
    private Set<Uczen> uczniowie;
    
    //constructor
    Klasa(String name) {
        this.nazwa = name;
        this.profil = "profile not set";
        this.nauczyciel = "not set";
        uczniowie = new TreeSet<>((Uczen o1, Uczen o2) -> {
            if(o1.getNazwisko().equals(o2.getNazwisko())) {
                //Sort by firstname if lastnames are the same
                return o1.getImie().compareTo(o2.getImie());
            } else {
                //Sort by lastname
                return o1.getNazwisko().compareTo(o2.getNazwisko());
            }       
        }); 
    }
    
    //set profile
    void ustawProfil(String profil) {
        this.profil = profil;
    }
    
    //add new student
    void dodaj(Uczen u) {
        uczniowie.add(u);
    }
    
    void usun(String pesel, Przedmiot p, double ocena) {
        for(Uczen u : uczniowie) {
            if(u.equals(new Uczen(pesel))) {
                u.usun(p, ocena);
            }
        }
    }
    
    //add grade
    void dodaj(String pesel, Przedmiot p, double ocena) {
        for(Uczen u : uczniowie) {
            if(u.equals(new Uczen(pesel))) {
                u.dodaj(p, ocena);
            }
        }
    }
    
    //get students count
    int getStudentsCount() {
        return this.uczniowie.size();
    }
    
    //get student
    Uczen getStudent(String pesel) {
        for(Uczen u : this.uczniowie) {
            if(u.equals(new Uczen(pesel))) {
                return u;
            }
        }
        return null;
    }
    
    //get class average
    double srednia() {
        double suma = 0;
        int ilosc = 0;
        for(Uczen u : uczniowie) {
            suma += u.srednia();
            ilosc++;
        }
        return suma/ilosc;
    }
    
    //get student's average
    double srednia(String nazwisko) {
        double srednia = 0;
        for(Uczen u : uczniowie) {
            if(u.equals(new Uczen(nazwisko))) {
                srednia = u.srednia();
            }
        }
        return srednia;
    }
    
    //get student's class subject average
    double srednia(String pesel, Przedmiot p) {
        double srednia = 0;
        for(Uczen u : uczniowie) {
            if(u.equals(new Uczen(pesel))) {
                srednia = u.srednia(p);
            }
        }
        return srednia;
    }
    
    //get student's grades
    Map<String, List<Double>> oceny(Przedmiot p) {
        Map<String, List<Double>> mapa = new HashMap<>();
        List<Double> lista;
        for(Uczen u : uczniowie) {
            lista = u.Oceny(p);
            mapa.put(u.toString(), lista);
        }
        return mapa;
    }
    
    //get class name
    public String getName() {
        return this.nazwa;
    }
    
    //get profile name
    public String getProfile() {
        return this.profil;
    }
    
    public String getTeacher() {
        return this.nauczyciel;
    }
    
    //get students as UCZEN
    public Set<Uczen> listStudents() {
        return this.uczniowie;
    }
    
    //get students list as STRING
    public List<String> listStudentsAsString() {
        List<String> list = new ArrayList<>();
        for(Uczen u : uczniowie) {
            list.add(u.getImie() + " " + u.getNazwisko());
        }
        return list;
    }
    
    //set teacher's name
    void setWychowawca(String wychowawca) {
        this.nauczyciel = wychowawca;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }
    
    boolean equals(Klasa u) {
        return u.nazwa.equals(this.nazwa);
    }
    
    @Override
    public String toString() {        
        return this.nazwa;
    }

    @Override
    public int compareTo(Object o) {
        return this.nazwa.compareTo(o.toString());
    }
}
