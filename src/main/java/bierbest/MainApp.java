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
import javassist.bytecode.stackmap.TypeData;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainApp extends Application {
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());

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
        if(args.length < 5)
            printUsage();
        HashMap<String, String> connectionProperties = new HashMap<>();
        connectionProperties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + args[0]);
        connectionProperties.put("javax.persistence.jdbc.user", args[1]);
        connectionProperties.put("javax.persistence.jdbc.password", args[2]);
        if (args[5].equals("simulated")) {
            connectionProperties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
        } else {
            connectionProperties.put("javax.persistence.schema-generation.database.action", "create");
        }
        File file = new File(args[3]);
        if (!file.exists()) {
            LOGGER.log(Level.SEVERE, "keyStore file does not exist");
            return;
        }
        System.setProperty("javax.net.ssl.keyStore", args[3]);
        System.setProperty("javax.net.ssl.keyStorePassword", args[4]);

        int port = 4488;
        if(System.getProperty("bierbest.communication.port") != null) {
            port = Integer.parseInt(System.getProperty("bierbest.communication.port"));
        }


        // Create an EMF
        sessionFactory = Persistence.createEntityManagerFactory("BierBest-owner", connectionProperties);
        serverThread = new BierBestServer(port, sessionFactory);
        serverThread.start();

        // Launch client simulator that adds some sample data
        if (args[5].equals("simulated")) {
            new BierBestClientSimulator("127.0.0.1", port).start();
        }

        launch(args);
    }

    public static void printUsage() {
        System.out.println("arguments: <db_address> <db_username> <db_password> <keystore_path> <keystore_password> [simulated]");
        System.out.println("Server port set in bierbest.communication.port property, default: 4488");
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
