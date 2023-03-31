package com.design.leetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Design a simple load distributor for a data center that can do the following:

Add and remove machines from the cluster.
Add applications to run on a machine.
Stop applications that are running on a machine.
Return a list of the applications running on a machine.
Implement the DCLoadBalancer class:

DCLoadBalancer() Initializes the object.
void addMachine(int machineId, int capacity) Registers a machine with the given machineId and maximum capacity.
void removeMachine(int machineId) Removes the machine with the given machineId. All applications running on this machine are automatically reallocated 
to other machines in the same order as they were added to this machine. The applications should be reallocated in the same manner as addApplication.
int addApplication(int appId, int loadUse) Allocates an application with the given appId and loadUse to the machine with the largest remaining capacity
 that can handle the additional request. If there is a tie, the machine with the lowest ID is used. Returns the machine ID that the application is allocated to. 
 If no machine can handle the request, return -1.
void stopApplication(int appId) Stops and removes the application with the given appId from the machine it is running on, 
freeing up the machine's capacity by its corresponding loadUse. If the application does not exist, nothing happens.
List<Integer> getApplications(int machineId) Returns a list of application IDs running on a machine with the given machineId in the order in which they were added. 
If there are more than 10 applications, return only the first 10 IDs.
 

Example 1:

Input
["DCLoadBalancer", "addMachine", "addMachine", "addMachine", "addMachine", "addApplication", "addApplication", "addApplication", 
"addApplication", "getApplications", "addMachine", "addApplication", "stopApplication", "addApplication", "getApplications", "removeMachine", "getApplications"]
[[], [1, 1], [2, 10], [3, 10], [4, 15], [1, 3], [2, 11], [3, 6], [4, 5], [2], [5, 10], [5, 5], [3], [6, 5], [4], [4], [2]]

Output
[null, null, null, null, null, 4, 4, 2, 3, [3], null, 5, null, 2, [1, 2], null, [6, 1]]

Explanation
DCLoadBalancer dCLoadBalancer = new DCLoadBalancer();
dCLoadBalancer.addMachine(1, 1); // Capacity Left: [1]
dCLoadBalancer.addMachine(2, 10); // Capacity Left: [1,10]
dCLoadBalancer.addMachine(3, 10); // Capacity Left: [1,10,10]
dCLoadBalancer.addMachine(4, 15); // Capacity Left: [1,10,10,15]
dCLoadBalancer.addApplication(1, 3); // return 4, Capacity Left: [1,10,10,12]
                                     // Machine 4 had the largest capacity left at 15.
dCLoadBalancer.addApplication(2, 11); // return 4, Capacity Left: [1,10,10,1]
                                      // Machine 4 had the largest capacity left at 12.
dCLoadBalancer.addApplication(3, 6); // return 2, Capacity Left: [1,4,10,1]
                                     // Machine 2 and 3 had the same largest capacity but machine 2 has the lower ID.
dCLoadBalancer.addApplication(4, 5); // return 3, Capacity Left: [1,4,5,1]
                                     // Machine 3 had the largest capacity at 10.
dCLoadBalancer.getApplications(2); // return [3], Machine 2 only has application 3.
dCLoadBalancer.addMachine(5, 10); // Capacity Left: [1,4,5,1,10]
dCLoadBalancer.addApplication(5, 5); // return 5, Capacity Left: [1,4,5,1,5]
                                     // Machine 5 had the largest capacity at 10.
dCLoadBalancer.stopApplication(3); // Capacity Left: [1,10,5,1,5], 
                                   // Application 3 was running on machine 2.
dCLoadBalancer.addApplication(6, 5); // return 2, Capacity Left: [1,5,5,1,5]
                                     // Machine 2 had the largest capacity at 10.
dCLoadBalancer.getApplications(4); // return [1, 2], Machine 4 has applications 1 and 2.
dCLoadBalancer.removeMachine(4); // Capacity Left: [1,2,5,-,5]
                                 // Machine 4 had applications 1 and 2.
                                 // Application 1 has a load of 3 and is added to machine 2.
                                 // Application 2 has a load of 11 and cannot be added to any machine so it is removed.
dCLoadBalancer.getApplications(2); // return [6, 1], Machine 2 has applications 6 and 1.
 

Constraints:

1 <= machineId, appId <= 105
1 <= loadUse, capacity <= 105
machineId will not have the same ID as any active machine across all calls to addMachine.
machineId will be the ID of an active machine across all calls to removeMachine and getApplications.
appId will be unique across all calls to addApplication.
At most 10 calls will be made to removeMachine.
At most 2 * 104 calls in total will be made to addMachine, removeMachine, addApplication, stopApplication and getApplications.
 * 
 * @author sukh
 *
 */
public class _1_LoadDistributor {

  private Map<Integer, Machine> machineIdMap;
  private Queue<Machine> machines;
  private Map<Integer, App> appIdMap;

  public _1_LoadDistributor() {
    machines = new PriorityQueue<>(new MachineComparator());
    machineIdMap = new ConcurrentHashMap<>();
    appIdMap = new ConcurrentHashMap<>();
  }

  public void addMachine(int machineId, int capacity) {
    Machine machine = new Machine(machineId, capacity);
    machines.offer(machine);
    machineIdMap.put(machineId, machine);
  }

  public void removeMachine(int machineId) {
    Machine machine = machineIdMap.remove(machineId);
    if (machine == null) {
      return;
    }
    machines.remove(machine);
    Set<App> apps = machine.getApps();
    for (App app : apps) {
      app.setMachineId(addApp(app));
    }
  }

  private int addApp(App app) {
    while (!machines.isEmpty()) {
      Machine top = machines.peek();
      /**
       * if a load cannot be added to any machine --> it is removed.
       */
      if (app.getLoad() > top.getSize()) {
        appIdMap.remove(app.getId());
        return -1;
      }
      appIdMap.put(app.getId(), app);
      Machine poll = machines.poll();
      poll.addApp(app);
      machines.offer(poll);
      return poll.getId();
    }
    appIdMap.remove(app.getId());
    return -1;
  }

  public int addApplication(int appId, int loadUse) {
    App app = new App(appId, loadUse);
    app.setMachineId(addApp(app));
    return app.getMachineId();
  }

  public void stopApplication(int appId) {
    App app = appIdMap.remove(appId);
    if (app != null) {
      Machine machine = machineIdMap.get(app.getMachineId());
      machine.removeApp(app);
      /**
       * Re-balance the Heap
       * 
       * NOTE: this is possible only when we add or remove from the heap, else no
       * changes are reflected in the heap structure
       */
      Machine poll = machines.poll();
      machines.offer(poll);
    }
  }

  public List<Integer> getApplications(int machineId) {
    List<Integer> apps = new ArrayList<>();
    Machine machine = machineIdMap.get(machineId);
    if (machine != null) {
      Set<App> setApps = machine.getApps();
      /**
       * To iterate through Set
       */
      Iterator<App> iterator = setApps.iterator();
      for (int i = 0; iterator.hasNext() && i < 10; i++) {
        App app = iterator.next();
        apps.add(app.getId());
      }
    }
    return apps;
  }

  private static Consumer<? super Object> consumer = (a) -> {
    System.out.print(a + ", ");
  };

  public static void main(String[] args) {
    _1_LoadDistributor obj = new _1_LoadDistributor();
    obj.addMachine(1, 1);
    obj.addMachine(2, 10);
    obj.addMachine(3, 10);
    obj.addMachine(4, 15);

    System.out.println(obj.addApplication(1, 3));
    System.out.println(obj.addApplication(2, 11));
    System.out.println(obj.addApplication(3, 6));
    System.out.println(obj.addApplication(4, 5));
    /**
     * using Lambdas
     */
    obj.getApplications(2).forEach((a) -> {
      System.out.print(a + ", ");
    });
    System.out.println();
    obj.addMachine(5, 10);
    System.out.println(obj.addApplication(5, 5));
    obj.stopApplication(3);
    System.out.println(obj.addApplication(6, 5));
    obj.getApplications(4).forEach(consumer);
    System.out.println();
    obj.removeMachine(4);
    /**
     * using Consumer Function
     */
    obj.getApplications(2).forEach(consumer);
  }
}

class MachineComparator implements Comparator<Machine> {
  public int compare(Machine a, Machine b) {
    if (a.getSize() == b.getSize()) {
      return a.getId() - b.getId();
    }
    return b.getSize() - a.getSize();
  }
}

class App {
  private int id;
  private int load;
  private int machineId;

  public App(int id, int load) {
    this.id = id;
    this.load = load;
  }

  public int getId() {
    return this.id;
  }

  public int getLoad() {
    return this.load;
  }

  public void setMachineId(int machineId) {
    this.machineId = machineId;
  }

  public int getMachineId() {
    return this.machineId;
  }
}

class Machine {
  private int id;
  private int capacity;
  private int size;
  private Set<App> apps;

  public Machine(int id, int capacity) {
    this.id = id;
    this.capacity = capacity;
    this.size = capacity;
    /**
     * To maintain the insertion order
     */
    this.apps = new LinkedHashSet<>();
  }

  public int getId() {
    return this.id;
  }

  public int getSize() {
    return size;
  }

  private void downSize(int load) {
    this.size -= load;
  }

  private void upSize(int load) {
    this.size += load;
  }

  public void addApp(App app) {
    downSize(app.getLoad());
    apps.add(app);
  }

  public void removeApp(App app) {
    upSize(app.getLoad());
    apps.remove(app);
  }

  public Set<App> getApps() {
    return this.apps;
  }

}

/**
 * Your _1_LoadDistributor object will be instantiated and called as such: <br>
 * _1_LoadDistributor obj = new _1_LoadDistributor(); <br>
 * obj.addMachine(machineId,capacity); <br>
 * obj.removeMachine(machineId); <br>
 * int param_3 = obj.addApplication(appId,loadUse); <br>
 * obj.stopApplication(appId); <br>
 * List<Integer> param_5 = obj.getApplications(machineId); <br>
 */