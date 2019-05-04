package revolut.infrastructure.persistence;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Slf4j
public class JpaEntityManagerFactory {
    
    private static final String PERSISTENCE_UNIT_NAME = "revolut";
    private static EntityManagerFactory entityManagerFactory;
    
    private JpaEntityManagerFactory() {
    }
    
    public static EntityManagerFactory getInstance() {
        if (entityManagerFactory == null) {
            synchronized (JpaEntityManagerFactory.class) {
                if (entityManagerFactory == null) {
                    entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                }
            }
        }
        return entityManagerFactory;
    }
    
}
