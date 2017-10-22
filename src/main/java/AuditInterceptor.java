import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by mayan on 21/10/17.
 */
public class AuditInterceptor extends EmptyInterceptor {


    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        //return super.onSave(entity, id, state, propertyNames, types);
        System.out.println("Saving an entity");
        return  false;
    }

    @Override
    public void postFlush(Iterator entities) {
        //super.postFlush(entities);
        System.out.println("After entity has been flushed");
    }
}
