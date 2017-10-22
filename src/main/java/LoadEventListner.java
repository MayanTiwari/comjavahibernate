import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.LoadEvent;
import org.hibernate.event.spi.LoadEventListener;

/**
 * Created by mayan on 22/10/17.
 */
public class LoadEventListner extends DefaultLoadEventListener {
    @Override
    public void onLoad(LoadEvent event, LoadType loadType) throws HibernateException {
        System.out.println("From LoadEventListner- Entity Loaded");
        System.out.println(event.getEntityId());
    }
}
