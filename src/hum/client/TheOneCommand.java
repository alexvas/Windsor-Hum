package hum.client;

import com.google.gwt.core.client.Scheduler;

public abstract class TheOneCommand implements Scheduler.ScheduledCommand {
    private int counter = 0;

    private final Scheduler.ScheduledCommand wrapper = new Scheduler.ScheduledCommand() {

        @Override
        public void execute() {
            if (--counter > 0) {
                return;
            }
            TheOneCommand.this.execute();
        }
    };

    public void schedule() {
        ++counter;
        Scheduler.get().scheduleDeferred(wrapper);
    }
}