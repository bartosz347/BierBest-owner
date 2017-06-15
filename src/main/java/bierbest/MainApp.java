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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainApp extends Application {
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());
    public static EntityManagerFactory sessionFactory;
    private static BierBestServer serverThread;

    public static void main(String[] args) {
        if (args.length < 5) {
            printUsage();
        }
        Map connectionProperties = new HashMap<>();
        connectionProperties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + args[0]);
        connectionProperties.put("javax.persistence.jdbc.user", args[1]);
        connectionProperties.put("javax.persistence.jdbc.password", args[2]);
        connectionProperties.put("javax.persistence.schema-generation.database.action", "none");

        File file = new File(args[3]);
        if (!file.exists()) {
            LOGGER.log(Level.SEVERE, "keyStore file does not exist");
            printUsage();
        }
        System.setProperty("javax.net.ssl.keyStore", args[3]);
        System.setProperty("javax.net.ssl.keyStorePassword", args[4]);

        int port = 4488;
        if (System.getProperty("bierbest.communication.port") != null) {
            port = Integer.parseInt(System.getProperty("bierbest.communication.port"));
        }

        // Create an EMF
        try {
            sessionFactory = Persistence.createEntityManagerFactory("BierBest-owner", connectionProperties);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "cannot open DB connection " + e.getMessage());
            System.exit(-1);
        }

        if (args.length > 5 && args[5].equals("simulated")) {
            connectionProperties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
            LOGGER.log(Level.INFO, "simulated run, dropping and creating db schema");
        } else {
            EntityManager entityManager = sessionFactory.createEntityManager();
            entityManager.getTransaction().begin();
            if (entityManager.createNativeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'client'").getResultList().size() < 1
                    || entityManager.createNativeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'client'").getResultList().size() < 1) {
                connectionProperties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
                LOGGER.log(Level.INFO, "one or more tables do not exist, recreating db schema");
            }
            entityManager.getTransaction().commit();
            entityManager.close();
        }


        Persistence.generateSchema("BierBest-owner", connectionProperties);
        serverThread = new BierBestServer(port, sessionFactory);
        serverThread.start();

        // Launch client simulator that adds some sample data
        if (args.length > 5 && args[5].equals("simulated")) {
            new BierBestClientSimulator("127.0.0.1", port).start();
        }

        launch(args);
    }

    public static void printUsage() {
        System.out.println("arguments: <db_address> <db_username> <db_password> <keystore_path> <keystore_password> [simulated]");
        System.out.println("Server port set in bierbest.communication.port property, default: 4488");
        LOGGER.log(Level.SEVERE, "wrong arguments");
        System.exit(-1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        Parent root = fxmlLoader.load();

        ((MainScreenView) fxmlLoader.getController()).mainApp = this;
        serverThread.setMainScreenView(((MainScreenView) fxmlLoader.getController()));

        primaryStage.getIcons().add(new Image("bierbest/images/icon.png"));
        primaryStage.setTitle("BierBest backoffice");
        primaryStage.setScene(new Scene(root, 750, 600));

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        serverThread.close();
        sessionFactory.close();
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
        stage.getIcons().add(new Image("bierbest/images/icon.png"));
        ClientDetailsScreenView clientDetailsScreenView = fxmlLoader.getController();
        clientDetailsScreenView.setClientViewModel(clientViewModel);
        clientDetailsScreenView.setOwnStage(stage);
        stage.show();
    }
}
