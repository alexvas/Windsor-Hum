package hum.server.services;

import hum.server.model.DataObject;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.googlecode.objectify.util.DAOBase;

public class ObjectifyLocator extends Locator<DataObject, Long> {
    @Override
    public DataObject create(Class<? extends DataObject> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataObject find(Class<? extends DataObject> clazz, Long id) {
        DAOBase daoBase = new DAOBase();
        return daoBase.ofy().find(clazz, id);
    }

    @Override
    public Class<DataObject> getDomainType() {
        // Never called
        return null;
    }

    @Override
    public Long getId(DataObject domainObject) {
        return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
        return Long.class;
    }

    @Override
    public Object getVersion(DataObject domainObject) {
        return domainObject.getVersion();
    }
}