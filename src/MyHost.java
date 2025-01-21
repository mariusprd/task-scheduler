/* Implement this class. */
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class MyHost extends Host {
    private PriorityBlockingQueue<Task> pq = new PriorityBlockingQueue<>(100, (t1, t2) -> {
        int priorityComparison = Integer.compare(t2.getPriority(), t1.getPriority());
        if (priorityComparison == 0)
            return Integer.compare(t1.getStart(), t2.getStart());

        return priorityComparison;
    });

    private AtomicBoolean shutdown = new AtomicBoolean(false);
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private AtomicLong startTime = new AtomicLong(0);

    private Task executionTask;
    private Object lock = new Object();

    @Override
    public void run() {
        long global_time = System.currentTimeMillis();  // used for debug prints
        while (!shutdown.get()) {
            synchronized (this) {
                if (!isRunning.get()) {
                    if (pq.size() > 0) {
                        isRunning.set(true);
                        executionTask = pq.poll();
                        startTime.set(System.currentTimeMillis());
                        // System.out.println("Started task" + executionTask.getId() + " at " + (System.currentTimeMillis() - global_time));
                    }
                }
            }

            if (isRunning.get()) {
                startTime.set(System.currentTimeMillis());
                synchronized (lock) {
                    try {
                        // wait until a task is added with a higher priority or finish execution
                        lock.wait(executionTask.getLeft());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                synchronized (executionTask) {
                    long elapsedTime = System.currentTimeMillis() - startTime.get();
                    elapsedTime = Math.round((double) elapsedTime / 1000) * 1000;
                    executionTask.setLeft(executionTask.getLeft() -  elapsedTime);

                    if (executionTask.getLeft() <= 0) {
                        executionTask.finish();
                        isRunning.set(false);
                        // System.out.println("Finished task" + executionTask.getId() + " at " + (System.currentTimeMillis() - global_time));
                    } else {
                        // System.out.println("Preempted task" + executionTask.getId() + " at " + (System.currentTimeMillis() - global_time));
                        pq.add(executionTask);
                        isRunning.set(false);
                    }
                }
            }
        }



    }

    @Override
    public synchronized void addTask(Task task) {
        // add task => if cur_task is preemptible and task.priority > cur_task.priority: wake up cur_execution
        this.pq.add(task);

        if (!isRunning.get())
                return;

        synchronized (executionTask) {
            if (executionTask.isPreemptible() && task.getPriority() > executionTask.getPriority()) {
                synchronized (lock) {
                    lock.notify();
                    // System.out.println("Woke up to preempt task!");
                }
            }
        }
        // System.out.println(this.getName() + " Added task" + task.getId() + " to pq");
    }

    @Override
    public synchronized int getQueueSize() {
        int pqSize = this.pq.size();
        return this.isRunning.get() ? pqSize + 1 : pqSize;
    }

    @Override
    public synchronized long getWorkLeft() {
        long res = 0;
        for (Task task : pq) {
            res += task.getLeft();
        }

        if (executionTask == null)
            return res;

        if (isRunning.get()) {
            long tmp = executionTask.getLeft() - (System.currentTimeMillis() - startTime.get());
            if (tmp > 0)
                res += tmp;
        }

        res = Math.round((double) res / 1000) * 1000;
        // System.out.println("Work left: " + res);

        return res;
    }

    @Override
    public void shutdown() {
        shutdown.set(true);
        synchronized (lock) {
            lock.notify();
        }
    }
}
