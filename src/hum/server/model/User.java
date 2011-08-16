package hum.server.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Unindexed;

 @Cached(expirationSeconds=600)
 public class User extends DataObject {

    @Embedded
    private List<Info> info = new ArrayList<Info>();

    public static class Info {
        public String identifier;

        public String providerName;

        private String displayName;

        @Unindexed
        private String photo;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

     public List<Info> getInfo() {
         return info;
     }
 }
