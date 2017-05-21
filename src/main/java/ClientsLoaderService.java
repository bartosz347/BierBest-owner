import javax.persistence.EntityManager;
import java.util.List;

public class ClientsLoaderService {

    public ClientModel loadData() {
        ClientModel client = null;
        EntityManager entityManager = Main.sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<ClientModel> result = entityManager.createQuery( "from client", ClientModel.class ).getResultList();
        for ( ClientModel cl : result ) {
          client = cl;
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        return client;
    }
}
