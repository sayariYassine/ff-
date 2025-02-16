package com.esprit.controllers;

import com.esprit.models.Evenement;
import com.esprit.models.Participation;
import com.esprit.models.User;
import com.esprit.services.ParticipationService;
import com.esprit.services.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.List;

public class EventDetailsController {

    @FXML private ImageView eventImage;
    @FXML private Label eventTitle;
    @FXML private Label eventDescription;
    @FXML private Label eventStatus;
    @FXML private Label eventDate;
    @FXML private Label eventLocation;
    @FXML private Button participateButton;

    // Participant fields (you may need to adjust these based on your data model)
    @FXML private ImageView participantImage1;
    @FXML private Label participantName1;
    @FXML private Label participantEmail1;
    @FXML private ImageView participantImage2;
    @FXML private Label participantName2;
    @FXML private Label participantEmail2;

    public void setEvent(Evenement event) {
        eventTitle.setText(event.getTitre());
        eventDescription.setText(event.getDescription());
        eventDate.setText(event.getFormattedDate());
        eventLocation.setText(event.getLieu()); // Assuming you have a getLieu() method

        // Set event status
        if (event.isCurrentEvent()) {
            eventStatus.setText("Ongoing");
        } else if (event.isUpcomingEvent()) {
            eventStatus.setText("Upcoming");
        } else {
            eventStatus.setText("Past");
        }

        // Set event image
        //eventImage.setImage(new Image("/images/placeholder_event.png")); // Replace with actual event image

        // Set up participate button action
        participateButton.setOnAction(e -> handleParticipate(event));

        // Load participant data (you'll need to implement this based on your data model)
        loadParticipants(event);
    }

    private void handleParticipate(Evenement event) {
        // Implement participation logic here
        System.out.println("Participating in event: " + event.getTitre());
    }

    @FXML
    private VBox participantsSection; // The VBox where participants will be added


    private void loadParticipants(Evenement event) {
        ParticipationService participationService = new ParticipationService();
        List<Participation> participations = participationService.getParticipantsForEvent(event.getId());

        // Loop through the participations and add dynamic UI elements
        for (int i = 0; i < participations.size(); i++) {
            Participation participation = participations.get(i);
            UserService userService = new UserService();
            User user = userService.getUserById(participation.getIdUtilisateur());

            // Create HBox to hold the participant's image and information
            HBox participantBox = new HBox(10);
            participantBox.setAlignment(Pos.CENTER_LEFT);

            // Create ImageView for the participant's profile image
            ImageView participantImage = new ImageView();
            participantImage.setFitWidth(40);
            participantImage.setFitHeight(40);
//            participantImage.setImage(new Image(user.getProfileImageUrl())); // Assuming the user has a profile image URL

            // Create VBox for the participant's name and email with styling
            VBox participantInfo = new VBox();

            // Apply styles to name and email labels
            Label nameLabel = new Label(user.getNom());
            nameLabel.getStyleClass().add("info-content"); // Apply 'info-content' style for name

            Label emailLabel = new Label(user.getEmail());
            emailLabel.getStyleClass().add("info-label"); // Apply 'info-label' style for email

            participantInfo.getChildren().addAll(nameLabel, emailLabel);

            // Add the image and info VBox to the HBox
            participantBox.getChildren().addAll(participantImage, participantInfo);

            // Add the participantBox to the participantsSection VBox
            participantsSection.getChildren().add(participantBox);
        }
    }





}
