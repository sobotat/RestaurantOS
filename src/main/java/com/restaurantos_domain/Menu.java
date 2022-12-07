package com.restaurantos_domain;

import java.time.LocalDate;

public class Menu {
    private int menuId;
    private LocalDate date;
    private LocalDate createdDate;

    public Menu(int menuId, LocalDate date, LocalDate createdDate) {
        this.menuId = menuId;
        this.date = date;
        this.createdDate = createdDate;
    }

    // Setters
    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    // Getters
    public int getMenuId() {
        return menuId;
    }
    public LocalDate getDate() {
        return date;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
}
