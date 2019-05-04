package revolut.infrastructure.framework;

import javax.persistence.EntityManager;

public interface UnitOfWork<R, E extends Exception> {
    
    R execute(EntityManager entityManager) throws E;
}
