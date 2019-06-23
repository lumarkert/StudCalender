package com.example.lukas.studcalender;

import java.util.Date;

public class Termin {
    Date m_date;
    String m_description;
    String m_room;

    public Termin(Date m_date, String m_description, String m_room) {
        this.m_date = m_date;
        this.m_description = m_description;
        this.m_room = m_room;
    }

    public void printTermin(){
        System.out.println(m_date);
        System.out.println(m_description);
        System.out.println(m_room);
    }
}
