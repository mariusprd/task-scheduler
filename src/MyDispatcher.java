/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {
    private int cur_id;
    private int num_nodes;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);

        this.cur_id = 0;
        this.num_nodes = hosts.size();
    }

    @Override
    public synchronized void addTask(Task task) {
        if (this.algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
            // System.out.println("==> Dispatcher added task" + task.getId() + " to host" + cur_id % num_nodes);
            hosts.get(cur_id % num_nodes).addTask(task);
            this.cur_id++;
        }

        if (this.algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {
            Integer min_index = 0, i = 0;
            Integer min_size = Integer.MAX_VALUE;

            for (Host h : this.hosts) {
                if (h.getQueueSize() < min_size) {
                    min_index = i;
                    min_size = h.getQueueSize();
                }

                i++;
            }
            
            // System.out.println("==> Dispatcher added task" + task.getId() + " to host" + min_index);
            hosts.get(min_index).addTask(task);
        }

        if (this.algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {
            if (task.getType() == TaskType.SHORT) {
                // System.out.println("==> Dispatcher added task" + task.getId() + " to host" + 0);
                hosts.get(0).addTask(task);
            }

            if (task.getType() == TaskType.MEDIUM) {
                // System.out.println("==> Dispatcher added task" + task.getId() + " to host" + 1);
                hosts.get(1).addTask(task);
            }

            if (task.getType() == TaskType.LONG) {
                // System.out.println("==> Dispatcher added task" + task.getId() + " to host" + 2);
                hosts.get(2).addTask(task);
            }
        }

        if (this.algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {
            Integer min_index = 0, i = 0;
            Long min_work = Long.MAX_VALUE;

            for (Host h : this.hosts) {
                long cur_work_left = h.getWorkLeft();
                if (cur_work_left < min_work) {
                    min_index = i;
                    min_work = cur_work_left;
                }

                i++;
            }
            
            // System.out.println("==> Dispatcher added task" + task.getId() + " to host" + min_index + " with work left: " + min_work);
            hosts.get(min_index).addTask(task);
        }
    }
}
