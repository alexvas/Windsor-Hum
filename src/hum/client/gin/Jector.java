package hum.client.gin;

import hum.client.Main;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({Module.class})
public interface Jector extends Ginjector {
    public Main main();
}
