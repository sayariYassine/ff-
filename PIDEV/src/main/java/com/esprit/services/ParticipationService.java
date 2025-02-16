package com.esprit.services;

import com.esprit.models.Participation;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipationService {

    private final Connection connection = DataSource.getInstance().getConnection();

    public void ajouter(Participation participation) {
        String req = "INSERT INTO participation (id_evenement_id, id_user_id, date_participation) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, participation.getIdEvenement());
            ps.setInt(2, participation.getIdUtilisateur());
            ps.setTimestamp(3, Timestamp.valueOf(participation.getDateParticipation()));
            ps.executeUpdate();
            System.out.println("::::::::::Participation ajouté::::::::::");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifier(Participation participation) {
        String req = "UPDATE participation SET id_evenement_id = ?, id_user_id = ?, date_participation = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, participation.getIdEvenement());
            ps.setInt(2, participation.getIdUtilisateur());
            ps.setTimestamp(3, Timestamp.valueOf(participation.getDateParticipation()));
            ps.setInt(4, participation.getId());
            ps.executeUpdate();
            System.out.println("::::::::::Participation mise a jour::::::::::");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void supprimer(int id) {
        String req = "DELETE FROM participation WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("::::::::Participation supprimé:::::");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Participation> rechercher() {
        List<Participation> participations = new ArrayList<>();
        String req = "SELECT * FROM participation";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Participation participation = new Participation(
                        rs.getInt("id"),
                        rs.getInt("id_evenement_id"),
                        rs.getInt("id_user_id"),
                        rs.getTimestamp("date_participation").toLocalDateTime()
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            System.out.println( e.getMessage());
        }
        return participations;
    }


    public List<Participation> getParticipantsForEvent(int eventId) {
        List<Participation> participations = new ArrayList<>();
        String req = "SELECT * FROM participation WHERE id_evenement_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, eventId);  // Set the event ID parameter
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Participation participation = new Participation(
                            rs.getInt("id"),
                            rs.getInt("id_evenement_id"),
                            rs.getInt("id_user_id"),
                            rs.getTimestamp("date_participation").toLocalDateTime()
                    );
                    participations.add(participation);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return participations;
    }


}
