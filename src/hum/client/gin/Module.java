package hum.client.gin;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.inject.client.AbstractGinModule;

public class Module extends AbstractGinModule {

    public Module() {
        // entire if statement would be cut off by GWT Compiler if DebugLevel > Debug
        if (Log.isDebugEnabled()) {
            Log.debug(getClass().getName().replaceAll(".*\\.", "") + " created");
        }
    }

    @Override
    protected void configure() {
    }
}
