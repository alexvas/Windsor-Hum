package hum.server.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Unindexed;

 @Cached(expirationSeconds=600)
 public class User extends DataObject {

    @Embedded
    public List<Info> info = new ArrayList<Info>();

    public static class Info {
        public String identifier;

        public String providerName;

        public String displayName;

        @Unindexed
        public String photo;
    }
}
