package BierBest;

import BierBest.client.ClientModel;
import BierBest.order.BeerInfo;
import BierBest.order.OrderModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.joda.money.Money;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;



public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        Parent root = fxmlLoader.load();

        MainScreenView m = fxmlLoader.getController(); // todo obsolete


        primaryStage.getIcons().add(new Image("BierBest/images/icon.png"));
        primaryStage.setTitle("BierBEST backoffice");
        primaryStage.setScene(new Scene(root, 750, 600));


        primaryStage.show();
    }

    public static EntityManagerFactory sessionFactory;
    public static void main(String[] args) {

        // Create an EMF
        sessionFactory = Persistence.createEntityManagerFactory( "BierBest-owner" );

        // Generate some sample data
        try {
            EntityManager entityManager = sessionFactory.createEntityManager();
            entityManager.getTransaction().begin();

            ClientModel client1 = new ClientModel();
            client1.setFirstName("Kate");
            client1.setLastName("Kowalski");
            client1.setUsername("k_kowalski");
            client1.setCity("London");
            client1.setPhoneNumber("+44111333666");
            client1.setRegistrationDate(new Date());
            entityManager.persist(client1);

            ClientModel client2 = new ClientModel();
            client2.setFirstName("Andrew");
            client2.setLastName("Stephens");
            client2.setUsername("a_stephens");
            client2.setPhoneNumber("+74999333666");
            client2.setCity("New York");
            client2.setRegistrationDate(new Date());
            entityManager.persist(client2);


            OrderModel order = new OrderModel();
            order.setStatusClientSide("new");
            order.setBeerInfo(new BeerInfo() {{
                setName("pamperifko");
                setPriceFromMoney(Money.parse("USD 20.10"));
                setURL("https://untappd/najlepszePiwo");
                setImgURL("https://scontent.fwaw3-1.fna.fbcdn.net/v/t1.0-9/14316725_1206873812697643_1428947998854593240_n.jpg?oh=8a110acf4e63b59177aaeffe46dacc4d&oe=59B6016B");
            }});
            order.setDate(new Date());
            order.setClient(client1);
            entityManager.persist(order);

            order = new OrderModel();
            order.setStatusClientSide("new");
            order.setBeerInfo(new BeerInfo() {{
                setName("some_beer");
                setURL("https://untappd/some_beer");
                setImgURL("http://www.anagram.pl/wp-content/uploads/krolweskie.jpg");
                setPriceFromMoney(Money.parse("USD 12.30"));
            }});
            order.setDate(new Date());

            order.setClient(client2);
            entityManager.persist(order);

            entityManager.getTransaction().commit();
            entityManager.close();

        }
        catch (Exception e) {
            System.out.println(e);
            return;
        }

        launch(args);
    }

    @Override
    public void stop() throws Exception {
        sessionFactory.close();
    }
}
