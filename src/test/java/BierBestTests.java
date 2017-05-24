import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BierBestTests {

    EntityManagerFactory sessionFactory;

    @Before
    public void prepareHibernate() {
        sessionFactory = Persistence.createEntityManagerFactory( "BierBest-owner" );
    }

    @Test
    public void test() {

    }
}
