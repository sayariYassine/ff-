package com.esprit.models;

import java.time.LocalDateTime;

public class Participation {
    private int id;
    private int idEvenement;
    private int idUtilisateur;
    private LocalDateTime dateParticipation;


    public Participation( int idEvenement, int idUtilisateur, LocalDateTime dateParticipation) {
        this.idEvenement = idEvenement;
        this.idUtilisateur = idUtilisateur;
        this.dateParticipation = dateParticipation;
    }

    public Participation(int id, int idEvenement, int idUtilisateur, LocalDateTime dateParticipation) {
        this.id = id;
        this.idEvenement = idEvenement;
        this.idUtilisateur = idUtilisateur;
        this.dateParticipation = dateParticipation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public LocalDateTime getDateParticipation() {
        return dateParticipation;
    }

    public void setDateParticipation(LocalDateTime dateParticipation) {
        this.dateParticipation = dateParticipation;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "id=" + id +
                ", idEvenement=" + idEvenement +
                ", idUtilisateur=" + idUtilisateur +
                ", dateParticipation=" + dateParticipation +
                '}';
    }
}
