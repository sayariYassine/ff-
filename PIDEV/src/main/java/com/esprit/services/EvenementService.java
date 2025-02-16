package com.esprit.services;

import com.esprit.models.Evenement;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IService<Evenement>{

    Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Evenement evenement) {
        String req = "INSERT INTO evenement (titre, description, dateDebut, dateFin, lieu, capacite_max, capacite_actuelle,categorie,image) VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, evenement.getTitre());
            ps.setString(2, evenement.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(evenement.getDateDebut()));
            ps.setTimestamp(4, Timestamp.valueOf(evenement.getDateFin()));
            ps.setString(5, evenement.getLieu());
            ps.setInt(6, evenement.getCapaciteMax());
            ps.setInt(7, evenement.getCapaciteActuelle());
            ps.setString(8, evenement.getCategorie());
            ps.setString(9, evenement.getImage());
            ps.executeUpdate();
            System.out.println("::::::::::Evenement ajouté::::::::::");

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Evenement evenement) {
        String req = "UPDATE evenement SET titre = ?, description = ?, datedebut = ?, datefin = ?, lieu = ?, capacite_max = ?, capacite_actuelle = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, evenement.getTitre());
            ps.setString(2, evenement.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(evenement.getDateDebut()));
            ps.setTimestamp(4, Timestamp.valueOf(evenement.getDateFin()));
            ps.setString(5, evenement.getLieu());
            ps.setInt(6, evenement.getCapaciteMax());
            ps.setInt(7, evenement.getCapaciteActuelle());
            ps.setInt(8, evenement.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("::::::::::evenement mis a jour::::::::::");
            } else {
                System.out.println("No rows updated. Check if the ID exists.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating event: " + e.getMessage());
        }
    }


    @Override
    public void supprimer(Evenement evenement) {

        String req = "DELETE FROM evenement WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, evenement.getId());
            ps.executeUpdate();
            System.out.println("::::::::::Participation supprimé::::::::::");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Evenement> rechercher() {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Evenement evenement = new Evenement(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getTimestamp("dateDebut").toLocalDateTime(),
                        rs.getTimestamp("dateFin").toLocalDateTime(),
                        rs.getString("lieu"),
                        rs.getInt("capacite_max"),
                        rs.getInt("capacite_actuelle"),
                        rs.getString("categorie"),
                        rs.getString("image")
                );
                evenements.add(evenement);
            }

        } catch (SQLException e) {
            System.out.println( e.getMessage());
        }

        return evenements;
    }

    public void incrementCurrentCapacity(int id) {
        String req = "UPDATE evenement SET  capacite_actuelle = capacite_actuelle - 1 WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, String.valueOf(id));


            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("::::::::::CAPACITE INCREASED::::::::::");
            } else {
                System.out.println("No rows updated. Check if the ID exists.");
            }
        } catch (SQLException e) {
            System.out.println("Error INCREASING CAP: " + e.getMessage());
        }
    }
}
