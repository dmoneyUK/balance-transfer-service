package revolut.infrastructure.persistence;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;

public class AtomikoDataSource {
    
    private static AtomikosNonXADataSourceBean instance = getInstance();
    
    private AtomikoDataSource() {
    }
    
    public static AtomikosNonXADataSourceBean getInstance() {
        
        if (instance == null) {
            synchronized (AtomikoDataSource.class) {
                if (instance == null) {
                    instance = new AtomikosNonXADataSourceBean();
                    instance.setUniqueResourceName("RevolutDB");
                    instance.setUrl("jdbc:h2:file:~/testDB:revolut;DB_CLOSE_DELAY=-1");
                    instance.setUser("sa");
                    instance.setPassword("sa");
                    instance.setDriverClassName("org.h2.Driver");
                    instance.setPoolSize(1);
                    instance.setBorrowConnectionTimeout(60);
                }
            }
        }
        return instance;
    }
}
