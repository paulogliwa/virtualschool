/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirtualnaszkola;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Szkola {
    private String nazwa;
    private Set<Klasa> klasy;
    
    public Szkola(String nazwa) {
        this.nazwa = nazwa;
        klasy = new TreeSet<>();
    }
    
    public boolean addClass(Klasa cl) {
        return klasy.add(cl);
    }
    
    public Klasa givemeClass(String name) {
        for(Klasa c : klasy) {
            if(c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    public Set<Klasa> listClasses() {
        return this.klasy;
    }
    
    public List<String> listClassesAsString() {
        List<String> list = new ArrayList<>();
        for(Klasa cl : klasy) {
            list.add(cl.getName());
        }
        return list;
    }
    
    public String getName() {
        return this.nazwa;
    }
    
    public void setWychowawca(String klasa, String wychowawca) {
        for(Klasa cl : klasy) {
            if(cl.getName().equals(klasa)) {
                cl.setWychowawca(wychowawca);
            }
        }
    }
}
