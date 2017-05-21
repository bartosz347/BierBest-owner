import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.getIcons().add(new Image("images/icon.png"));
        primaryStage.setTitle("BierBEST backoffice");
        primaryStage.setScene(new Scene(root, 650, 600));


        primaryStage.show();
    }

    public static EntityManagerFactory sessionFactory;
    public static void main(String[] args) {

        // Hibernate

        // Create an EMF
        sessionFactory = Persistence.createEntityManagerFactory( "BierBest-owner" );


        try {
            EntityManager entityManager = sessionFactory.createEntityManager();
            entityManager.getTransaction().begin();
            ClientModel client = new ClientModel();
            client.setFirstName("Kate");
            client.setLastName("Kowalski");
            client.setCity("New York");
            entityManager.persist(client);
            entityManager.getTransaction().commit();
            entityManager.close();



        }
        catch (Exception e) {
            System.out.println(e);
        }

        launch(args);
    }

    @Override
    public void stop() throws Exception {
        sessionFactory.close();
    }
}
