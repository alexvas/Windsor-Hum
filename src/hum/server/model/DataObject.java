package hum.server.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Version;

public class DataObject {
   @Id
   private Long id;

   @Version
   private Integer version = 0;

   public Date created = new Date();

   public Date updated;

   @PrePersist
   void prePersist() {
       version++;
       updated = new Date();
   }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
