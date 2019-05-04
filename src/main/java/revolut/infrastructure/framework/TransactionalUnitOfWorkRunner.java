package revolut.infrastructure.framework;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Slf4j
public class TransactionalUnitOfWorkRunner implements UnitOfWorkRunner {
    
    private EntityManagerFactory entityManagerFc;
    
    @Inject
    public TransactionalUnitOfWorkRunner(EntityManagerFactory entityManagerFc) {
        this.entityManagerFc = entityManagerFc;
    }
    
    @Override
    public <T, E extends Exception> T run(UnitOfWork<T, E> unitOfWork) throws E {
        EntityManager entityManager = entityManagerFc.createEntityManager();
        entityManager.getTransaction().begin();
        
        try {
            T result = unitOfWork.execute(entityManager);
            
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
    
    public static <T, E extends Exception> T runInTransaction(EntityManagerFactory entityManagerFc, UnitOfWork<T, E> unitOfWork) throws E {
        return new TransactionalUnitOfWorkRunner(entityManagerFc).run(unitOfWork);
    }
}
