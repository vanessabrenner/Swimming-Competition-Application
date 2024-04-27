package swimmingcompetition.client.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;
import swimmingcompetition.services.ISwimmingCompetitionObserver;
import swimmingcompetition.services.ISwimmingCompetitionServices;
import swimmingcompetition.services.SwimmingCompetitionException;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProfileController implements Initializable, ISwimmingCompetitionObserver {

    private ISwimmingCompetitionServices server;
    private Organizer organizer;
    ObservableList<ChallengeDTO> challengesModel = FXCollections.observableArrayList();
    @FXML
    TableView<ChallengeDTO> challenges;
    @FXML
    TableColumn<ChallengeDTO, Long> idChallengeCol;
    @FXML
    TableColumn<ChallengeDTO, String> styleChallengeCol;
    @FXML
    TableColumn<ChallengeDTO, Integer> distanceChallengeCol;
    @FXML
    TableColumn<ChallengeDTO, Integer> noParticipantsCol;

    ObservableList<ParticipantDTO> participantsModel = FXCollections.observableArrayList();
    @FXML
    TableView<ParticipantDTO> participants;
    @FXML
    TableColumn<ParticipantDTO, Long> idParticipantCol;
    @FXML
    TableColumn<ParticipantDTO, String> nameParticipantCol;
    @FXML
    TableColumn<ParticipantDTO, Integer> ageParticipantCol;
    @FXML
    TableColumn<ParticipantDTO, String> challengesOfParticipantsCol;
    @FXML
    TextField filterByChallenge_txt;

    @FXML
    TextField nameParticipant_txt;
    @FXML
    TextField ageParticipant_txt;
    @FXML
    TextField challengesPartcipant_txt;
    @FXML
    Button submit;


    public void setServer(ISwimmingCompetitionServices server){
        this.server = server;
        //initModel();
    }
    public void setOrganizer(Organizer organizer){
        this.organizer = organizer;
    }



    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        idChallengeCol.setCellValueFactory(new PropertyValueFactory<ChallengeDTO, Long>("id"));
        styleChallengeCol.setCellValueFactory(new PropertyValueFactory<ChallengeDTO, String>("style"));
        distanceChallengeCol.setCellValueFactory(new PropertyValueFactory<ChallengeDTO, Integer>("distance"));
        distanceChallengeCol.setCellValueFactory(cellData -> {
            int distanceValue = cellData.getValue().getDistance().get();
            return new SimpleIntegerProperty(distanceValue).asObject();
        });
        noParticipantsCol.setCellValueFactory(new PropertyValueFactory<ChallengeDTO, Integer>("NoParticipants"));
        challenges.setItems(challengesModel);

        challenges.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Challenge challenge = new Challenge(newValue.getDistance(), newValue.getStyle());
                challenge.setId(newValue.getId());
                updateParticipantsTableByChallenge(challenge);
            }
        });

        challenges.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        idParticipantCol.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, Long>("id"));
        nameParticipantCol.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, String>("name"));
        ageParticipantCol.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, Integer>("age"));
        challengesOfParticipantsCol.setCellValueFactory(cellData -> new SimpleStringProperty(getChallengeIdsAsString(cellData.getValue().getChallenges())));

        participants.setItems(participantsModel);
//
//        filterByChallenge_txt.textProperty().addListener((observable, oldValue, newValue) -> {
//            updateParticipantsTableByChallenge(newValue);
//        });
    }
    @FXML
    public void submit(){
        try {
            String nameParticipant = nameParticipant_txt.getText();
            int ageParticipant = Integer.parseInt(ageParticipant_txt.getText());
            List<ChallengeDTO> challengesDTOList = challenges.getSelectionModel().getSelectedItems();
            List<Challenge>  challengeList = new ArrayList<>();
            for(ChallengeDTO challengeDTO : challengesDTOList){
                Challenge challenge = new Challenge(challengeDTO.getDistance(), challengeDTO.getStyle());
                challenge.setId(challengeDTO.getId());
                challengeList.add(challenge);
            }
            Participant participant = server.addParticipant(nameParticipant, ageParticipant);
            server.addParticipantToMoreChallenges(participant, challengeList);
            //initModel();
        }
        catch (SwimmingCompetitionException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        logout();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    void logout() {
        try {
            server.logout(organizer, this);
        } catch (SwimmingCompetitionException e) {
            System.out.println("Logout error " + e);
        }

    }
    public void initModel(){
        try {
            ChallengeDTO[] challenges = server.findAllChallenges();
            List<ChallengeDTO> challengeList = Arrays.asList(challenges);
            challengesModel.setAll(challengeList);
        }
        catch (SwimmingCompetitionException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateParticipantsTableByChallenge(Challenge challenge){
        participantsModel.clear();
        try {
            List<ParticipantDTO> participantsByChallenge = server.findParticipantsByChallenge(challenge);
            participantsModel.addAll(participantsByChallenge);
        }
        catch (SwimmingCompetitionException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateTables(ChallengeDTO[] challengeDTOS) {
        Platform.runLater(()->{
            System.out.println("updateTables");
            challengesModel.clear();
            participantsModel.clear();
            this.challengesModel.setAll(Arrays.stream(challengeDTOS).toList());
        });
    }

    private String getChallengeIdsAsString(List<Challenge> challenges) {
        List<Long> challengeIds = challenges.stream().map(Challenge::getId).collect(Collectors.toList());

        if (challengeIds == null || challengeIds.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Long challengeId : challengeIds) {
            stringBuilder.append(challengeId).append(", ");
        }

        stringBuilder.setLength(stringBuilder.length() - 2);

        return stringBuilder.toString();
    }
}
