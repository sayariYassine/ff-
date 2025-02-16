package com.esprit.tests;

import com.esprit.models.Evenement;
import com.esprit.services.EvenementService;

import java.time.LocalDateTime;

public class MainProg {
    public static void main(String[] args) {
//       ps.ajouter(new Personne("Say", "Anas"));
//
//
        EvenementService evs = new EvenementService();
        evs.ajouter(new Evenement(2,"Competition BasketBall",
                "Competition Tennis",
                LocalDateTime.of(2025, 02, 05, 19, 0),
                LocalDateTime.of(2025, 03, 13, 22, 0),
                "Salle de Sports laouina",
                50,
                45,
                "Evenement Sportif",
                ""
        ));

        System.out.println(evs.rechercher());
//
////        evs.modifier(new Evenement(2,"Competition football",
////                "Competition 5v5 de football total de 5 equipes",
////                LocalDateTime.of(2025, 5, 15, 19, 0),
////                LocalDateTime.of(2025, 5, 15, 22, 0),
////                "Salle de Sports laouina",
////                50,10));
//        ParticipationService pts=new ParticipationService();
//
//        pts.ajouter(new Participation(2, 1, LocalDateTime.now()));
//        System.out.println(pts.rechercher());

    }
}
