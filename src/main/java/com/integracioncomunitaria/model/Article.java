package com.integracioncomunitaria.model;

import java.sql.Date;

public class Article {
    private int idArticle;
    private String name;
    private int categoryId;
    private int id_user_create;
    private int id_user_update;
    private Date date_create;
    private Date date_update;

    public Article(int idArticle, String name, int categoryId) {
        this.idArticle = idArticle;
        this.name = name;
        this.categoryId = categoryId;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }
}

