package com.example.lukas.studcalender;

import java.io.Serializable;
import java.util.ArrayList;

public class Kalender implements Serializable {
    ArrayList<Termin> m_termins;
    String owner;

    public Kalender(String owner) {
        this.owner = owner;
        m_termins = new ArrayList<>();
    }

    public void addTermin(Termin t) {
        m_termins.add(t);
    }
    public void printTermins(){
        for (Termin t: m_termins) {
            t.printTermin();
        }
    }
}
