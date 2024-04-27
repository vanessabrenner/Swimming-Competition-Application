package swimmingcompetition.client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import swimmingcompetition.client.gui.LoginController;
import swimmingcompetition.client.gui.ProfileController;
import swimmingcompetition.networking.json.SwimmingCompetitionServicesJsonProxy;
import swimmingcompetition.networking.protobuf.ProtoProxy;
import swimmingcompetition.services.ISwimmingCompetitionServices;

import java.io.IOException;
import java.util.Properties;

public class StartClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartJsonClientFX.class.getResourceAsStream("/swimmingcompetitionclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("swimmingcompetition.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("swimmingcompetition.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        ISwimmingCompetitionServices server = new ProtoProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();


        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);




        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("profile.fxml"));
        Parent croot=cloader.load();


        ProfileController profileController =
                cloader.<ProfileController>getController();
        profileController.setServer(server);

        ctrl.setProfileController(profileController);
        ctrl.setParent(croot);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();




    }


}
