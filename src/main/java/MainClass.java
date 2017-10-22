import org.hibernate.*;
import org.hibernate.cfg.QuerySecondPass;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by mayan on 10/10/17.
 * Notes
 * https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html
 */
public class MainClass {

    public static void main(String[] args) {
        // batchProcessingSession();
        // SQLQueries();
       // integratorTests();
        //filterTests();
    }

    public static void initialSesstions() {
        System.out.println("Start app");
        Session session = HibernateUtilities.getSessionFactory().openSession();

        session.beginTransaction();
        User user = new User();
        user.setId(1);
        user.setName("Joe");
        user.getProteinData().setGoal(250);
        user.setGoalAlert(new GoalAlert("Congratulations"));
        user.getGoalAlerts().add(new GoalAlertOToM("Congo"));
        user.getGoalAlerts().add(new GoalAlertOToM("You did it"));
        user.addHistory(new UserHistory(new Date(), "Set the goal to 250"));
        //user.getHistoryCollection().add(new UserHistory(new Date(),"Set the goal from collection"));
        session.save(user);
        session.getTransaction().commit();

        session.beginTransaction();
        User user2 = new User();
        user2.setId(2);
        user2.setName("Alen");
        user2.getProteinData().setGoal(300);
        user2.addHistory(new UserHistory(new Date(), "Set the goal to 300"));
        session.save(user2);
        session.getTransaction().commit();

        session.beginTransaction();
        User userLoaded = session.load(User.class, 1);
        for (UserHistory history : userLoaded.getHistory()) {
            System.out.println(history.getEntryTime() + history.getEntry());
        }
        session.getTransaction().commit();
        System.out.println(userLoaded);
        session.close();
    }

    public static void DataReadSessions() {
        System.out.println("Started");

        populateSampleData();
        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from User user where name = :name")
                .setString("name", "joe");

        List<User> users = query.list();
        for (User user : users) {
            System.out.println(user);
        }

        Query alertQ = session.createQuery("from GoalAlertOToM").setMaxResults(2);
        List<GoalAlertOToM> alerts = alertQ.list();
        for (GoalAlertOToM alert : alerts) {
            System.out.println(alert);
        }

        //Named Query
        Query namedQAlerts = session.createNamedQuery("AllGoalAlerts").setMaxResults(1);
        List<GoalAlertOToM> namedAlerts = namedQAlerts.list();
        for (GoalAlertOToM alert : namedAlerts) {
            System.out.println(alert);
        }

        //Dynamic instance
        Query dynamicObj = session.createQuery("select new UserTotal(user.name,user.proteinData.total)" +
                "from User user").setMaxResults(1);
        List<UserTotal> userTotals = dynamicObj.list();
        for (UserTotal total : userTotals) {
            System.out.println(total.getName() + " " + total.getTotal());

        }

       /* Criteria userCriteria = session.createCriteria(User.class)
                .add(Restrictions.eq("name","joe"));*/

        Criteria userCriteria = session.createCriteria(User.class)
                .add(Restrictions.or(
                        Restrictions.eq("name", "joe"),
                        Restrictions.eq("name", "Bob")
                ));
        //Projections
        /*Criteria userCriteriaProjections = session.createCriteria(User.class)
                .add(Restrictions.or(
                        Restrictions.eq("name", "joe"),
                        Restrictions.eq("name", "Bob")
                ))
                .setProjection(Projections.property("name"));

        List<Object[]> usersCriteriaListPro = userCriteriaProjections.list();
        for (Object[] userarray : usersCriteriaListPro) {
            for (Object ob : userarray) {
                System.out.println(ob.toString());
            }
        }*/

        //Criteria with example
        User usereg = new User();
        usereg.setName("joe");
        Example ex = Example.create(usereg)
                .ignoreCase();
        session.createCriteria(User.class)
                .add(ex);


        session.getTransaction().commit();
        HibernateUtilities.getSessionFactory().close();
    }

    public static void SQLQueries() {
        try {
            populateSampleData();
            Session session = HibernateUtilities.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT * FROM USERS ").addEntity(User.class);
            List<User> users = query.list();
            for (User user : users) {
                System.out.println(user);
            }

            session.getTransaction().commit();

        } catch (HibernateException hibernateException) {

        } finally {
            HibernateUtilities.getSessionFactory().close();
        }

    }
    public static void filterTests(){
        populateSampleData();
        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.enableFilter("nameFilter").setParameter("name","j%");
        session.beginTransaction();
        Query query = session.createQuery("from User");
        List<User> users = query.list();
        for(User user : users){
            System.out.println(user);
        }
        session.getTransaction().commit();
        session.close();
    }
    public static void integratorTests() {
        populateSampleData();
        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();

        User user = session.load(User.class, 1);
        System.out.println(user);

        session.getTransaction().commit();
        session.close();

    }

    public static void batchProcessingSession() {
        try {
            populateSampleData();
            Session session = HibernateUtilities.getSessionFactory().openSession();
            session.beginTransaction();

            //Batch processing
            Query queryBatch = session.createQuery("update ProteinData pd set pd.total = 0");
            queryBatch.executeUpdate();

            //Manual batch processing.
            Criteria criteria = session.createCriteria(User.class);
            ScrollableResults users = criteria.setCacheMode(CacheMode.IGNORE).scroll();
            int count = 0;
            while (users.next()) {
                User user = (User) users.get(0);
                user.setProteinData(new ProteinData());
                session.save(user);
                if (++count % 2 == 0) {
                    session.flush();
                    session.clear();
                }
                System.out.println(user.getName());
            }
            session.getTransaction().commit();

        } catch (HibernateException hibernateException) {

        } finally {
            HibernateUtilities.getSessionFactory().close();
        }

    }


    private static void populateSampleData() {
        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();

        User joe = createUser("Joe", 500, 50, "Good Job", "You made it!");
        session.save(joe);

        User bob = createUser("Bob", 300, 20, "Taco Time");
        session.save(bob);

        User amy = createUser("Amy", 250, 200, "Yes!!");
        session.save(amy);

        session.getTransaction().commit();
        session.close();

    }

    private static User createUser(String name, int goal, int total, String... alerts) {
        User user = new User();
        user.setName(name);
        user.getProteinData().setGoal(goal);
        user.addHistory(new UserHistory(new Date(), "set goal to" + goal));
        user.getProteinData().setTotal(total);
        user.addHistory(new UserHistory(new Date(), "Set toal to" + total));
        for (String alert : alerts) {
            user.getGoalAlerts().add(new GoalAlertOToM(alert));
        }
        return user;
    }

}
