package com.integracioncomunitaria.model;

import java.util.Date;

public class Customer {
    private int idCustomer;
    private String name;
    private Date dateYear;
    private String dni;
    private String email;
    private String phone;
    private String address;
    private int idGenderType;
    private int idCity;
    private double gpsLat;
    private double gpsLon;
    private int idUserCreate;
    private int idUserUpdate;
    private Date dateCreate;
    private Date dateUpdate;
    private int idUser;

    // Constructor vacío
    public Customer() {}

    public Customer(String name, int idUser) {
        this.name = name;
        this.idUser = idUser;
    }

    // Constructor con parámetros
    public Customer(int idCustomer, String name, Date dateYear, String dni, String email, String phone, String address,
                    int idGenderType, int idCity, double gpsLat, double gpsLon, int idUserCreate, int idUserUpdate,
                    Date dateCreate, Date dateUpdate, int idUser) {
        this.idCustomer = idCustomer;
        this.name = name;
        this.dateYear = dateYear;
        this.dni = dni;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.idGenderType = idGenderType;
        this.idCity = idCity;
        this.gpsLat = gpsLat;
        this.gpsLon = gpsLon;
        this.idUserCreate = idUserCreate;
        this.idUserUpdate = idUserUpdate;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.idUser = idUser;
    }

    // Getters y Setters
    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateYear() {
        return dateYear;
    }

    public void setDateYear(Date dateYear) {
        this.dateYear = dateYear;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdGenderType() {
        return idGenderType;
    }

    public void setIdGenderType(int idGenderType) {
        this.idGenderType = idGenderType;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public double getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(double gpsLat) {
        this.gpsLat = gpsLat;
    }

    public double getGpsLon() {
        return gpsLon;
    }

    public void setGpsLon(double gpsLon) {
        this.gpsLon = gpsLon;
    }

    public int getIdUserCreate() {
        return idUserCreate;
    }

    public void setIdUserCreate(int idUserCreate) {
        this.idUserCreate = idUserCreate;
    }

    public int getIdUserUpdate() {
        return idUserUpdate;
    }

    public void setIdUserUpdate(int idUserUpdate) {
        this.idUserUpdate = idUserUpdate;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

}
