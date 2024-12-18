package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String request;
    private String typeOfSupplier;
    private String email;
    private LocalDate date;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getTypeOfSupplier() {
        return typeOfSupplier;
    }

    public void setTypeOfSupplier(String typeOfSupplier) {
        this.typeOfSupplier = typeOfSupplier;
    }

    public Request(Long id, String request, LocalDate date) {
        this.id = id;
        this.request = request;
        this.date = date;
    }

    public Request() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
