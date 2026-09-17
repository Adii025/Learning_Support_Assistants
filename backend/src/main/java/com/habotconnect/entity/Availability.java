package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "availability")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    public LsaProfile lsa;

    public String dayOfWeek;
    public String startTime;
    public String endTime;
    public Boolean available = true;
}