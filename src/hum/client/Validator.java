package hum.client;

import hum.client.model.HumProxy;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

import com.google.inject.Singleton;

@Singleton
public class Validator {
    public List<ConstraintViolation<HumProxy>> validate(HumProxy hum) {
        List<ConstraintViolation<HumProxy>> violations = new ArrayList<ConstraintViolation<HumProxy>>();
        if (hum.getPoint() == null) {
            violations.add(new NotProvided(hum, "Click on the Map"));
        }
        if (hum.getLevel() == null) {
            violations.add(new NotProvided(hum, "Specify How Strong Hum Was"));
        }
        if (hum.getStart() == null) {
            violations.add(new NotProvided(hum, "Specify When Hum Started"));
        }
        if (hum.getPoint() != null && hum.getAddress() == null) {
            violations.add(new NotProvided(hum, "Address Is Not Resolved. Move Pin a Little."));
        }

        return violations;
    }

    private static class NotProvided implements ConstraintViolation<HumProxy> {
        private final HumProxy hum;
        private final String message;

        public NotProvided(HumProxy hum, String message) {
            this.hum = hum;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public HumProxy getRootBean() {
            return hum;
        }

        @Override
        public Class<HumProxy> getRootBeanClass() {
            return HumProxy.class;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
