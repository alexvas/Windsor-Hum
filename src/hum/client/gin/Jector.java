package hum.client.gin;

import hum.client.Workflow;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({Module.class})
public interface Jector extends Ginjector {
    public Workflow main();
}
