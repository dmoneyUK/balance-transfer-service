package revolut.infrastructure.framework;

import javax.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionWrapper<R> implements InvocationHandler {
    
    private EntityManagerFactory entityManagerFc;
    private R delegate;
    
    public TransactionWrapper(EntityManagerFactory entityManagerFc, R delegate) {
        this.entityManagerFc = entityManagerFc;
        this.delegate = delegate;
    }
    
    public static <R> R transactionally(EntityManagerFactory entityManagerFc, R delegate) {
        return (R) Proxy.newProxyInstance(delegate.getClass().getClassLoader(), delegate.getClass().getInterfaces(),
                                          new TransactionWrapper(entityManagerFc, delegate));
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        
        return new TransactionalUnitOfWorkRunner(entityManagerFc).run(
                entityManager -> method.invoke(delegate, args));
    }
}
