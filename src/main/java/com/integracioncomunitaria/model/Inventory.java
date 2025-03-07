package com.integracioncomunitaria.model;



import java.math.BigDecimal;
import java.sql.Timestamp;

import com.integracioncomunitaria.model.Article;

public class Inventory {
    private int idInventory;
    private Article article;
    private Integer cant;
    private BigDecimal cost;
    private Integer idPostulation;
    private Integer idUserUpdate;
    private Integer idUserCreate;
    private Timestamp dateUpdate;
    private Timestamp dateCreate;
    private int quantity;

    // Constructor vacío
    public Inventory() {
    }

    public Inventory(int idInventory, String articleName, int quantity, BigDecimal cost) {
        this.idInventory = idInventory;
        this.article = new Article(); // Crea el objeto
        this.article.setName(articleName); // Asigna el nombre
        this.quantity = quantity;
        this.cost = cost;
    }
    






    // Getters y Setters
    public int getIdInventory() {
        return idInventory;
    }

    public void setIdInventory(int idInventory) {
        this.idInventory = idInventory;
    }

    public Article getArticleObject() {
        return article;
    }

    public String getArticle() {
        return article != null ? article.getName() : "Desconocido";
    }

    public Integer getCant() {
        return cant;
    }

    public void setCant(Integer cant) {
        this.cant = cant;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getIdPostulation() {
        return idPostulation;
    }

    public void setIdPostulation(Integer idPostulation) {
        this.idPostulation = idPostulation;
    }

    public Integer getIdUserUpdate() {
        return idUserUpdate;
    }

    public void setIdUserUpdate(Integer idUserUpdate) {
        this.idUserUpdate = idUserUpdate;
    }

    public Integer getIdUserCreate() {
        return idUserCreate;
    }

    public void setIdUserCreate(Integer idUserCreate) {
        this.idUserCreate = idUserCreate;
    }

    public Timestamp getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Timestamp dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Timestamp getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Timestamp dateCreate) {
        this.dateCreate = dateCreate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
