package revolut.infrastructure.framework;


public interface UnitOfWorkRunner {
    
    <T, E extends Exception> T run(UnitOfWork<T, E> unitOfWork) throws Exception;
}
