package com.esprit.controllers;

import com.esprit.models.Evenement;
import com.esprit.models.Participation;
import com.esprit.services.EvenementService;
import com.esprit.services.ParticipationService;
import com.esprit.utils.DataSource;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class EventController {
    @FXML
    private VBox currentEventsContainer;
    @FXML
    private VBox upcomingEventsContainer;
    @FXML
    private VBox pastEventsContainer;

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField locationField;

    @FXML
    private TextField categorieField;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField maxCapacityField;
    @FXML
    private TextField imagePathField;
    @FXML
    private TextField currentCapacityField;

    private File selectedImageFile;

    @FXML
    private ImageView imagePreview;



    //admin
    @FXML
    private TableView<Evenement> eventTable;
    @FXML
    private TableColumn<Evenement, String> titleColumn;
    @FXML
    private TableColumn<Evenement, String> categoryColumn;
    @FXML
    private TableColumn<Evenement, String> startDateColumn;
    @FXML
    private TableColumn<Evenement, String> endDateColumn;
    @FXML
    private TableColumn<Evenement, String> locationColumn;
    @FXML
    private TableColumn<Evenement, Integer> maxCapacityColumn;
    @FXML
    private TableColumn<Evenement, Integer> currentCapacityColumn;



    @FXML
    private void handleImageSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Event Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            try {
                // Define the target directory inside your project
                File destinationDir = new File("src/main/resources/images/");
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs(); // Create the directory if it does not exist
                }

                // Generate a unique filename
                String uniqueFileName = System.currentTimeMillis() + "_" + selectedImageFile.getName();
                File destinationFile = new File(destinationDir, uniqueFileName);

                // Copy the file to the project directory
                java.nio.file.Files.copy(selectedImageFile.toPath(), destinationFile.toPath());

                // Store only the filename in the text field
                imagePathField.setText(uniqueFileName);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error saving image.");
            }
        }
    }

    //recherche

    @FXML
    private TextField searchField;

    @FXML
    private ObservableList<Evenement> eventList; // Ensure this is properly initialized

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        FilteredList<Evenement> filteredEvents = new FilteredList<>(eventList, event ->
                event.getTitre().toLowerCase().contains(keyword) ||
                        event.getCategorie().toLowerCase().contains(keyword) ||
                        event.getLieu().toLowerCase().contains(keyword) ||
                        event.getDateDebut().toString().contains(keyword) ||
                        event.getDateFin().toString().contains(keyword)
        );
        eventTable.setItems(filteredEvents);
    }



    @FXML
    private Button addEventButton;

    private EvenementService evenementService;
    private int eventId;

    public void initialize() {
        Connection connection = DataSource.getInstance().getConnection();
        evenementService = new EvenementService();

        if (eventTable != null) {
            setupAdminTable();
            loadAdminEvents();
            List<Evenement> events = evenementService.rechercher();
            eventList = FXCollections.observableArrayList(events); // Initialize eventList
            eventTable.setItems(eventList);
        }

        if (currentEventsContainer != null && upcomingEventsContainer != null && pastEventsContainer != null) {
            loadEvents(); // Load events only once
        }
    }


    private void setupAdminTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        startDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateDebut().toString()));
        endDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateFin().toString()));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        maxCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));
        currentCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capaciteActuelle"));

        // Actions column
        TableColumn<Evenement, Void> actionsColumn = new TableColumn<>("Actions");

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox buttonsBox = new HBox(10, updateButton, deleteButton);

            {
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/com.esprit/images/edit.png")));
                updateIcon.setFitWidth(30);
                updateIcon.setFitHeight(30);

                updateButton.setGraphic(updateIcon);
                updateButton.setStyle("-fx-background-color: transparent;"); // Optional: remove button background
                updateButton.setOnAction(event -> handleUpdateEvent(getTableView().getItems().get(getIndex())));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/com.esprit/images/delete.png")));
                deleteIcon.setFitWidth(30);
                deleteIcon.setFitHeight(30);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnAction(event -> handleDeleteEvent(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }
        });


        eventTable.getColumns().add(actionsColumn);
    }

    private void loadAdminEvents() {
        List<Evenement> evenements = evenementService.rechercher();

        // Wrap list in observable list and set to TableView
        eventTable.setItems(FXCollections.observableArrayList(evenements));
    }

    private void loadEvents() {
        List<Evenement> evenements = evenementService.rechercher();

        for (Evenement event : evenements) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventCard.fxml"));
                Node eventCard = loader.load();
                setEventData(eventCard, event);

                if (event.isCurrentEvent() && currentEventsContainer != null) {
                    currentEventsContainer.getChildren().add(eventCard);
                    System.out.println("EVENT LOADED");
                } else if (event.isUpcomingEvent() && upcomingEventsContainer != null) {
                    upcomingEventsContainer.getChildren().add(eventCard);
                    System.out.println("EVENT LOADED");
                } else if (event.isPastEvent() && pastEventsContainer != null) {
                    pastEventsContainer.getChildren().add(eventCard);
                    System.out.println("EVENT LOADED");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void setEventData(Node eventCard, Evenement event) {
        Label eventTitle = (Label) eventCard.lookup("#eventTitle");
        Label eventDescription = (Label) eventCard.lookup("#eventDescription");
        Label eventDate = (Label) eventCard.lookup("#eventDate");
        ImageView eventImage = (ImageView) eventCard.lookup("#eventImage");
        Label eventLocation = (Label) eventCard.lookup("#eventLocation");
        Label eventCategory = (Label) eventCard.lookup("#eventCategory");
        Label remainingPlaces = (Label) eventCard.lookup("#remainingPlaces");
        Button viewDetailsButton = (Button) eventCard.lookup("#viewDetailsButton");
        Button participateButton = (Button) eventCard.lookup("#participateButton");
        if (eventTitle != null) eventTitle.setText(event.getTitre());
        if (eventDescription != null) eventDescription.setText(event.getDescription());
        if (eventDate != null) eventDate.setText(event.getFormattedDate());
        if (eventLocation != null) eventLocation.setText(event.getLieu());
        if (eventCategory != null) eventCategory.setText(event.getCategorie());
        remainingPlaces.setText(String.valueOf(event.getCapaciteMax()-event.getCapaciteActuelle()));
        // Load the saved image if it exists
        if (eventImage != null) {
            String imagePath = event.getImage(); // The image filename from the event
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File("src/main/resources/images/" + imagePath);
                if (imageFile.exists()) {
                    eventImage.setImage(new Image(imageFile.toURI().toString()));  // Load the image from the resources directory
                } else {
                    eventImage.setImage(new Image("/images/placeholder_event.png"));  // Fallback to placeholder
                }
            } else {
                eventImage.setImage(new Image("/images/placeholder_event.png"));  // Fallback to placeholder
            }
        }

        viewDetailsButton.setOnAction(e -> handleViewDetails(event, (Node) e.getSource()));
        participateButton.setOnAction(e -> handleParticipate(event));

    }


    private void handleParticipate(Evenement event) {
        System.out.println("User wants to participate in: " + event.getTitre());


        // Temporary user ID (until session is set up)
        int userId = 2; // Replace with session or dynamic user ID later
        Participation participation = new Participation(event.getId(), userId, LocalDateTime.now());

        ParticipationService participationService = new ParticipationService();

        if (isUserAlreadyParticipating(event.getId(), userId)) {
            showAlert("You are already a participant in this event.");
            return;
        }


        participationService.ajouter(participation); // Insert participation into database

        evenementService.incrementCurrentCapacity(event.getId());

        showAlert("Success You have successfully registered for the event!");
    }
    private boolean isUserAlreadyParticipating(int eventId, int userId) {
        ParticipationService participationService=new ParticipationService();
        List<Participation> participants = participationService.getParticipantsForEvent(eventId);
        return participants.stream().anyMatch(p -> p.getIdUtilisateur() == userId);
    }




    private void handleViewDetails(Evenement event, Object source) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventDetailsUI.fxml"));
            Parent detailsView = loader.load();

            EventDetailsController detailsController = loader.getController();
            detailsController.setEvent(event);

            // Get the current scene
            Scene currentScene = ((Node) source).getScene();

            // Set the details view as the root of the current scene
            currentScene.setRoot(detailsView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleAddEvent() {
        if (isFieldEmpty(titleField) || isFieldEmpty(descriptionField) || isFieldEmpty(locationField) ||
                startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
                isFieldEmpty(maxCapacityField) || isFieldEmpty(imagePathField) || isFieldEmpty(categorieField)) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            int maxCapacity = Integer.parseInt(maxCapacityField.getText());
            if (maxCapacity <= 0) {
                showAlert("Max Capacity must be a positive number.");
                return;
            }

            if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
                showAlert("Start date must be before end date.");
                return;
            }

            Evenement event = new Evenement();
            event.setTitre(titleField.getText());
            event.setDescription(descriptionField.getText());
            event.setLieu(locationField.getText());
            event.setDateDebut(startDatePicker.getValue().atStartOfDay());
            event.setDateFin(endDatePicker.getValue().atStartOfDay());
            event.setCapaciteMax(maxCapacity);
            event.setCapaciteActuelle(0);
            event.setCategorie(categorieField.getText());
            event.setImage(imagePathField.getText());

            evenementService.ajouter(event);

            showAlert("Event added successfully!");
            loadEvents();
        } catch (NumberFormatException e) {
            showAlert("Max Capacity must be a valid number.");
        }
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleUpdateEvent(Evenement selectedEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateEvent.fxml"));
            Parent updateView = loader.load();

            EventController controller = loader.getController();
            controller.populateUpdateForm(selectedEvent);

            // Set the event ID
            controller.setEventId(selectedEvent.getId());

            Stage stage = (Stage) eventTable.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene updateScene = new Scene(updateView, currentWidth, currentHeight);
            stage.setScene(updateScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEventId(int id) {
        this.eventId = id;
    }




    @FXML
    private void handleSaveUpdatedEvent() {
        if (isFieldEmpty(titleField) || isFieldEmpty(descriptionField) || isFieldEmpty(locationField) ||
                startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
                isFieldEmpty(maxCapacityField)){
            showAlert("Please fill in all fields.");
            return;
        }




        try {
            int maxCapacity = Integer.parseInt(maxCapacityField.getText());

            int currentCapacity = Integer.parseInt(currentCapacityField.getText());
            if (maxCapacity < 0 || currentCapacity < 0) {
                showAlert("Capacity cannot be negative.");
                return;
            }

            if (maxCapacity < currentCapacity) {
                showAlert("Max Capacity must be greater than or equal to Current Capacity.");
                return;
            }

            Evenement updatedEvent = new Evenement();
            updatedEvent.setId(this.eventId); // Set the event ID
            updatedEvent.setTitre(titleField.getText());
            updatedEvent.setDescription(descriptionField.getText());
            updatedEvent.setLieu(locationField.getText());
            updatedEvent.setDateDebut(startDatePicker.getValue().atStartOfDay());
            updatedEvent.setDateFin(endDatePicker.getValue().atStartOfDay());
            updatedEvent.setCapaciteMax(maxCapacity);
            updatedEvent.setCapaciteActuelle(Integer.parseInt(currentCapacityField.getText()));
            updatedEvent.setCategorie(categorieField.getText());
            updatedEvent.setImage(imagePathField.getText());

            evenementService.modifier(updatedEvent);

            showAlert("Event updated successfully!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminEventsUI.fxml"));
            Parent eventsView = loader.load();
            EventController controller = loader.getController();
            controller.loadAdminEvents();

            Stage stage = (Stage) titleField.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene eventsScene = new Scene(eventsView, currentWidth, currentHeight);
            stage.setScene(eventsScene);

        } catch (NumberFormatException e) {
            showAlert("Max Capacity must be a valid number.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean isFieldEmpty(javafx.scene.control.Control field) {
        if (field instanceof TextField) {
            return ((TextField) field).getText() == null || ((TextField) field).getText().trim().isEmpty();
        } else if (field instanceof TextArea) {
            return ((TextArea) field).getText() == null || ((TextArea) field).getText().trim().isEmpty();
        }
        return true; // Default case, should never happen if we pass only TextField or TextArea
    }







    private void populateUpdateForm(Evenement event) {
        titleField.setText(event.getTitre());
        descriptionField.setText(event.getDescription());
        locationField.setText(event.getLieu());
        startDatePicker.setValue(event.getDateDebut().toLocalDate());
        endDatePicker.setValue(event.getDateFin().toLocalDate());
        maxCapacityField.setText(String.valueOf(event.getCapaciteMax()));
        categorieField.setText(event.getCategorie());
        imagePathField.setText(event.getImage());
        currentCapacityField.setText(String.valueOf(event.getCapaciteActuelle()));
    }



    private void handleDeleteEvent(Evenement selectedEvent) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            evenementService.supprimer(selectedEvent);
            showAlert("Event deleted successfully!");
            loadAdminEvents();
        }
    }



}