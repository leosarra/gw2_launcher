package framework;

public final class TaskExecutor {
    private static TaskExecutor theTaskExecutor = new TaskExecutor();
    private TaskExecutor(){}
    public synchronized static TaskExecutor getInstance() { return theTaskExecutor; }
    public synchronized void perform(Task t) { t.esegui(); }
}
