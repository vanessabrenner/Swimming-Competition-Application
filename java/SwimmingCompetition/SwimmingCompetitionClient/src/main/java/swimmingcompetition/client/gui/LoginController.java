package swimmingcompetition.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.services.ISwimmingCompetitionServices;
import swimmingcompetition.services.SwimmingCompetitionException;

import java.io.IOException;

public class LoginController {

    private ISwimmingCompetitionServices server;
    private ProfileController profileController;
    Parent mainSwimmingCompetitionParent;

    @FXML
    Label login_lbl;
    @FXML
    TextField username_txtf;
    @FXML
    PasswordField password_pswdf;
    @FXML
    Button login_btn;
    @FXML
    Label error_lbl;

    public void setServer(ISwimmingCompetitionServices server){
        this.server = server;
    }
    public void setProfileController(ProfileController profileController){
        this.profileController = profileController;
    }
    public void setParent(Parent parent){
        mainSwimmingCompetitionParent = parent;
    }
    @FXML
    public void showProfile(ActionEvent actionEvent){
        try{
            this.error_lbl.setText("");
            String username = username_txtf.getText();
            String password = password_pswdf.getText();

            Organizer organizer = server.findAccount(username, password);
            server.login(organizer, profileController);
            Stage stage = new Stage();
            stage.setTitle("Organizer: " + organizer.getUsername());
            stage.setScene(new Scene(mainSwimmingCompetitionParent));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    profileController.logout();
                    System.exit(0);
                }
            });
            profileController.initModel();
            stage.show();
            profileController.setOrganizer(organizer);
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }
        catch (SwimmingCompetitionException ex){
            error_lbl.setText(ex.getMessage());
        }
        username_txtf.clear();
        password_pswdf.clear();
    }
    @FXML
    private void closeButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
