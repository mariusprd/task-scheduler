# Task Scheduling System with Java Threads

This project simulates a **task scheduling system** for a multi-threaded environment using **Java Threads**. The system includes a dispatcher (task manager) and multiple hosts (workers) to manage and execute tasks based on various scheduling policies.

---

## **Features**

### **Dispatcher**
- Implements scheduling policies to assign tasks to hosts:
  - **Round Robin (RR):** Assigns tasks cyclically to hosts.
  - **Shortest Queue (SQ):** Allocates tasks to the host with the smallest task queue.
  - **Size Interval Task Assignment (SITA):** Assigns tasks to hosts based on task size (short, medium, long).
  - **Least Work Left (LWL):** Allocates tasks to the host with the least total remaining work.
- Dispatches tasks to hosts based on priority and preemption rules.

### **Host**
- Executes tasks received from the dispatcher.
- Supports:
  - **Task Preemption:** Higher-priority tasks interrupt lower-priority tasks.
  - **Queue Management:** Manages tasks waiting for execution.

### **Task Properties**
Each task has the following properties:
- **ID:** Unique identifier.
- **Start Time:** Time when the task enters the system.
- **Duration:** Execution time required by the task.
- **Type:** Short, medium, or long (relevant for SITA policy).
- **Priority:** Determines task importance.
- **Preemptible:** Boolean value specifying if the task can be preempted.

---

## **Project Structure**
```
.
├── Dockerfile          # Docker configuration for the project
├── README.md           # Project documentation
├── checker/            # Test scripts and input/output files for validation
│   ├── checker.sh      # Script to run tests
│   ├── in/             # Input files for various tests
│   └── skeleton/       # Template files for implementation
├── local.sh            # Script for local testing
└── src/                # Source code
    ├── Dispatcher.java
    ├── Host.java
    ├── MyDispatcher.java
    ├── MyHost.java
    └── ...
```

---

## **Usage**

### **1. Build the Project**
To compile the project:
```bash
cd src
javac *.java
```

### **2. Run the Project**
To execute the system and run all tests:
```bash
java Main
```
To execute specific tests, modify the `tests` array in the `Main` class.

### **3. Local Testing**
To test the project locally with Docker:
```bash
./local.sh checker
```
This script validates the implementation using the test cases in the `checker/in` directory.

#### **Docker Configuration**
Ensure Docker is installed and running on your system. For Apple M1 users, enable **Rosetta** for x86/amd64 emulation in Docker settings.

### **4. Custom Inputs**
Input files in the `checker/in` directory define the task configurations for each test. Modify these files or create new ones to simulate different scenarios.

---

## **Scheduling Policies**

### **Round Robin (RR)**
- Tasks are assigned cyclically to hosts based on their IDs.

### **Shortest Queue (SQ)**
- Tasks are allocated to the host with the smallest queue size (including tasks currently running).

### **Size Interval Task Assignment (SITA)**
- Tasks are grouped into three categories (short, medium, long) and assigned to specific hosts.

### **Least Work Left (LWL)**
- Tasks are allocated to the host with the least remaining execution time.

---

## **Testing**

The `checker/in` directory includes input files for various test cases. Output results are stored in corresponding directories. Each test validates the functionality of the implemented scheduling policies under different scenarios.

---

## **Future Enhancements**
- Improve synchronization mechanisms to reduce debugging complexity.
- Extend the system to support dynamic host addition and removal.
- Enhance logging and debugging tools for better traceability.
