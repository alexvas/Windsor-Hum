package hum.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Unindexed;

 @Cached(expirationSeconds=600)
 public class User {
    @Id
    public Long id;

    @Embedded
    public List<Info> info = new ArrayList<Info>();

    @Version
    private Integer version;

    private Date created = new Date();

    public Date updated;

    public static class Info {
        public String identifier;

        public String providerName;

        @Unindexed
        public String displayName;

        @Unindexed
        public String photo;
    }

    @PrePersist
    void updateTimestamp() {
        updated = new Date();
    }
}
