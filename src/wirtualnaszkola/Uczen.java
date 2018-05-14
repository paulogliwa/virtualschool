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

/**
 *
 * @author Paulo
 */
public class Uczen implements Comparable {
    private String pesel;
    private String nazwisko;
    private String imie;
    private Map<Przedmiot, List<Double>> dzienniczek;
    
    Uczen(String pesel) {
        this.pesel = pesel;
        dzienniczek = new HashMap<>();
    }
    
    Uczen(String pesel, String imie, String nazwisko) {
        this.pesel = pesel;
        this.imie = imie;
        this.nazwisko = nazwisko;
        dzienniczek = new HashMap<>();
    }
    
    void usun(Przedmiot p, double ocena) {
        if(ocena >= 1 && ocena <= 6) {
            dzienniczek.get(p).remove(ocena);
        }
        else {
            System.out.println("Bledna ocena (1-6)");
        }
    }
    
    void dodaj(Przedmiot p, double ocena) {
        if(ocena >= 1 && ocena <= 6) {
            if(dzienniczek.containsKey(p)) {
                List<Double> list = dzienniczek.get(p);
                list.add(ocena);
                dzienniczek.put(p, list);
            }
            else {
                List<Double> list = new ArrayList<>();
                list.add(ocena);
                dzienniczek.put(p, list);
            }
        }
        else {
            System.out.println("Bledna ocena (1-6)");
        }
    }
    
    double srednia() {
        double suma = 0;
        int ilosc = 0;
        
        for(Przedmiot p : dzienniczek.keySet()) {
            for(double oc : dzienniczek.get(p)) {
                suma += oc;
            }
            ilosc += dzienniczek.get(p).size();
        }
        
        return suma/ilosc;
    }
    
    double srednia(Przedmiot p) {
        if(dzienniczek.containsKey(p)) {
            double suma = 0;

            for(double oc : dzienniczek.get(p)) {
                suma += oc;
            }

            return suma/dzienniczek.get(p).size();
        }
        else {
            System.out.println("Uczen nie ma oceny z tego przedmiotu");
            return -1;
        }
    }
    
    boolean equals(Uczen u) {
        return u.pesel.equals(this.pesel);
    }
    
    List<Double> Oceny(Przedmiot p) {
        return this.dzienniczek.get(p);
    }
    
    String oceny(Przedmiot p) {
        if(this.dzienniczek.get(p) != null) {
            return this.dzienniczek.get(p).toString();
        } else {
            return "-";
        }
        
    }
    
    //docomparatora
    @Override
    public String toString() {
        return this.pesel;
    }
    //docomparatora
    @Override
    public int compareTo(Object o) {
        return this.pesel.compareTo(o.toString());
    }
    
    public String getPesel() {
        return this.pesel;
    }
    
    public String getImie() {
        return this.imie;
    }
    
    public String getNazwisko() {
        return this.nazwisko;
    }
}
