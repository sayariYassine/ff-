package com.esprit.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evenement {
    private int id;
    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String lieu;
    private int capaciteMax;
    private int capaciteActuelle;
    private String categorie;
    private String image;



    public Evenement(int id, String titre, String description, LocalDateTime dateDebut, LocalDateTime dateFin, String lieu, int capaciteMax, int capaciteActuelle, String categorie,String image) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.capaciteActuelle = capaciteActuelle;
        this.categorie = categorie;
        this.image=image;
    }

    public Evenement() {
    }

    public boolean isCurrentEvent() {
        LocalDateTime now = LocalDateTime.now();
        return dateDebut.isBefore(now.plusDays(7)) && dateFin.isAfter(now);
    }

    public boolean isUpcomingEvent() {
        return dateDebut.isAfter(LocalDateTime.now());
    }

    public boolean isPastEvent() {
        return dateFin.isBefore(LocalDateTime.now());
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return dateDebut.format(formatter) + " - " + dateFin.format(formatter);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public int getCapaciteActuelle() {
        return capaciteActuelle;
    }

    public void setCapaciteActuelle(int capaciteActuelle) {
        this.capaciteActuelle = capaciteActuelle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", lieu='" + lieu + '\'' +
                ", capaciteMax=" + capaciteMax +
                ", capaciteActuelle=" + capaciteActuelle +
                ", image=" + image +
                '}';
    }
}
