package hum.server.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Version;

public class DataObject {
   @Id
   public Long id;

   @Version
   public Integer version = 0;

   public Date created = new Date();

   public Date updated;

   @PrePersist
   void prePersist() {
       version++;
       updated = new Date();
   }
}
