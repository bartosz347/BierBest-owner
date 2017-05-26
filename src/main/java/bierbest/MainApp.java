package bierbest;

import bierbest.client.ClientViewModel;
import bierbest.communication.BierBestClientSimulator;
import bierbest.communication.BierBestServer;
import bierbest.view.ClientDetailsScreenView;
import bierbest.view.MainScreenView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        Parent root = fxmlLoader.load();

        ((MainScreenView) fxmlLoader.getController()).mainApp = this;

        primaryStage.getIcons().add(new Image("BierBest/images/icon.png"));
        primaryStage.setTitle("BierBEST backoffice");
        primaryStage.setScene(new Scene(root, 750, 600));

        primaryStage.show();
    }

    public static EntityManagerFactory sessionFactory;
    private static BierBestServer serverThread;

    public static void main(String[] args) {
        // Create an EMF
        sessionFactory = Persistence.createEntityManagerFactory("BierBest-owner");
        serverThread = new BierBestServer(sessionFactory);
        serverThread.start();

        // Launch client simulator that adds some sample data
        try {
            BierBestClientSimulator bierBestClientSimulator = new BierBestClientSimulator();
            bierBestClientSimulator.start();
        } catch (Exception e) {

        }

        launch(args);
    }

    @Override
    public void stop() throws Exception {
        sessionFactory.close();
        serverThread.close();
    }

    public void showClientDetails(ClientViewModel clientViewModel, boolean allowEditing) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(allowEditing ? "ClientDetailsScreen.fxml" : "ClientDetailsScreenReadOnly.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("client details");
        stage.getIcons().add(new Image("BierBest/images/icon.png"));
        ClientDetailsScreenView clientDetailsScreenView = fxmlLoader.getController();
        clientDetailsScreenView.setClientViewModel(clientViewModel);
        clientDetailsScreenView.setOwnStage(stage);
        stage.show();
    }
}
