import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;



/**
 * Created by mayan on 11/10/17.
 */
public class HibernateUtilities {
    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    public HibernateUtilities(){

    }
    static
    {
        try {
            Configuration configuration = new Configuration().setInterceptor(new AuditInterceptor()).configure("hibernate.cfg.xml");
            configuration.addResource("User.hbm.xml");
            configuration.addResource("UserHistory.hbm.xml");
            configuration.addResource("ProteinData.hbm.xml");
            configuration.addResource("GoalAlert.hbm.xml");
            configuration.addResource("GoalAlertOToM.hbm.xml");
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        }catch(HibernateException ex){
            System.out.println("Problem creating session factory");
        }
    }
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
