package hum.client;

import hum.client.gin.Jector;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;

public class HumEntry implements EntryPoint {
    @Override
    public void onModuleLoad() {
        Log.setUncaughtExceptionHandler();

        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                onModuleLoad2();
            }
        });
    }

    private void onModuleLoad2() {
        Log.debug("script is loaded");
//        Resources.RESOURCES.style().ensureInjected();
        Jector jector = GWT.create(Jector.class);
        jector.main().run();
    }
}
