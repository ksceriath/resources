RDD : resilient distributed datasets
------------------------------------
This is an abstract data structure(?).



DEVOPS
======

The existing Hadoop infrastructure is composed of three things:
1. Hadoop distributed filesystem (HDFS)
2. Hadoop Map/Reduce framework
3. Yarn resource manager

Spark core is a replacement for the (2) Hadoop Map Reduce, which is the core calculation engine.

Hadoop can work with the existing HDFS. It also works with Tachyon, which is distributed, in-memory filesystem. (Its not a replacement for HDFS, which is a disk-based fs).

And can also work with Yarn resource manager; however, it is compatible with another resource manager : Mesos, and ships with a Standalone resource manager of its own.


This is how the Map-Reduce jobs in the Hadoop ecosystem work...

Hadoop Map Reduce
-----------------
Map-Reduce has a master process called JobTracker, which is a JVM process. It has several slave processes called TaskTrackers, which are also JVM processes, running on the cluster nodes.

HDFS side of things has another master called NameNode (a JVM process), and it has corresponding slaves called DataNodes (also JVM processes) running on the nodes in the cluster.

JobTracker sends 'tasks' to the TaskTrackers, which are then responsible for creating child processes which do a Map job or a Reduce job as required. Important difference from Spark is that these Map and Reduce jobs are individual processes (whereas in Spark, map and reduce are run as tasks inside existing Executors, and each executor can do multiple tasks, whereas here we spawn a new JVM for each Map or Reduce job).

Idea was that if a Map or Reduce process started behaving weirdly, it would not affect or compromise map or reduce tasks running in other JVMs.

Yarn
====
Yarn is a Resource Manager. Its job is to manage the resources in a cluster.
Its basically a bunch of JVMs running in the cluster, with a ResourceManager JVM (which is like a master) and NodeManager JVMs running on each node in the cluster.
Apart from the above processes, there is a ApplicationMaster which runs for each application launched on the Yarn-cluster, and then there are Containers which are created on the cluster nodes where the jobs are carried out.
-- A Client submits an application to run to the ResourceManager.
-- This request contains information on how much resources does the ApplicationMaster of the application would require.
-- ResourceManager (the ApplicationsManager module) looks at the request, and creates a container on a node somewhere in the cluster for the ApplicationMaster.
-- A Container in yarn is basically a 'protected' allocation of the system resources (like cpu, memory).
-- ResourceManager creates the first container for the application on the cluster, by asking corresponding NodeManager processes of the node.
-- NodeManagers are responsible for communication of the node status, resource availability, etc to the ResourceManager, apart from launching the containers.
-- ApplicationMaster is started and registers itself with the ResourceManager.
-- ApplicationMaster is responsible for overseeing the execution of the application.
-- ApplicationMaster is responsible for requesting additional containers from the ResourceManager for the running the tasks.
-- ResourceManager looks at the requests from the ApplicationMaster, and grants permission (some form of key) to the ApplicationMaster to request containers on some nodes.
-- ApplicationMaster then presents these keys to the corresponding NodeMasters and gets the containers created for itself.
-- Containers then directly talk to the ApplicationMaster during the lifecycle of the application.
-- At the end the ApplicationMaster de-registers itself with the ResourceManager, which then deallocates the resources granted to the application.


Spark Deployment
================
Spark deployment modes : Spark is understood to be usable in four modes -- local, standalone, yarn and mesos. This is configured using the '--master' option while starting up the spark applications (spark-submit or spark-shell).

1. Local mode
-------------
-- Local mode is for running spark locally. This is what happens when we run 'spark-shell' on the laptop.
-- There is one machine.
-- There is only one JVM process which is started. This process can be thought of as both a Driver and an Executor.
-- The application can be configured to use different number of threads for running tasks. By default it takes 1 thread.
-- There are three options for this:
    ## spark-shell --master local
        $$ Create just one slot -- this is also the default option, when no --master is provided
    ## spark-shell --master local[N]
        $$ Create N slots for running threads
    ## spark-shell --master local[*]
        $$ Greedily create as many slots as the number of cores available (??? verification)
    ('slots' can be thought of as places where threads can run. This is the potential capacity to run threads, i.e., this is the maximum number of threads the application will spawn)

2. Standalone mode
------------------
-- This should be understood as a cluster manager (compared with yarn and mesos)
-- Spark ships with this cluster manager to be deployed on a cluster w/o a separate cluster manager
-- When the cluster comes up with this cluster manager, each machine comes up with a Worker JVM process, and one (or two, a configurable redundancy) Spark Master JVM process
-- Worker and Spark Master process are very light weight (500M memory)
-- Devops : these master and worker daemons come up as a result of the launch script which is used to start these nodes: ./sbin/start-master.sh and ./sbin/start-slave.sh
-- Workers are able to connect to the master because of the master-url specified to the startup script:
    ## ./sbin/start-slave.sh <master-spark-URL>
-- Spark Master's main job is to make sure that the Workers are available and running
-- Worker's main job is to create Executor processes and make sure they are alive and running
-- Multiple masters can be created as a redundancy for failsafe. Zookeeper is used for election between the masters (and for switch-over in case of failure?).
-- Standalone mode can be started as below, specifying the node for spark-master to run:
    ## spark-submit --master spark://host1:port1 ...
-- As always, spark-submit starts off the driver program: spark-submit with instantiate a driver JVM process in the machine that it is run in. It can be any node in the cluster and need not be the node with spark-master running. (It can be a remote machine like a laptop, in which the execution is similar to the yarn-client mode : driver running outside the cluster).
-- The driver starts and contacts the spark-master process asking for executors.
-- Driver provides spark-master with how many cores it wants on the cluster (spark.cores.max) and the memory it wants for each executor (spark.executor.memory). It can't tell where to allocate the cores it wants or even where to allocate the executors. As of the talk (Spark summit 2015), the driver cannot tell the spark-master how many executors to be allocated. (IS THIS STILL VALID?)
-- Spark-master looks at the available worker nodes it has, and asks them to start Executor JVM processes.
-- As of the talk (2015 spark summit) each worker can instantiate only one executor for an application (IS THIS STILL VALID?). It can instantiate another executor for another application running on the same spark-master. You can instantiate multiple workers on each node by configuring SPARK_WORKER_INSTANCES property.
-- Once the executors are created, they talk to the driver program directly and register with it. Spark-master and worker processes are not involved in communication anymore. They are there only to make workers and executors available and running.
-- Standalone mode exposes several UIs.
    ## Spark-Master exposes a UI on the port 7080
    ## Workers expose a UI 7081
    ## Driver exposes a UI on port 4040. This is also known as Spark UI and is available in other deployment modes as well, whereas other two UIs are only there for standalone mode.
-- Every time an action is executed on an RDD, it becomes as 'JOB'
-- Jobs break down into 'STAGES'
-- Stage is a shuffle-boundary (????)
-- Stages further break down into 'TASKS'
-- Each Task is run on a thread in an executor

3. Yarn mode
------------
Running spark on yarn is more or less similar to running of the yarn applications (described above) and the spark standalone mode.
There is a special ApplicationMaster that runs in the cluster for the spark application.
Spark applications can be submitted in a client mode and in a cluster mode.
The client submits the application to the ResourceManager. The driver process runs (in ApplicationMaster or separately). The executors run in the containers on the nodes.

The executors are requested to be launched by the driver, which through the ApplicationMaster, requests ResourceManager to grant it containers, and through NodeManagers gets the containers created, and then NodeManagers start the executors processes in these containers as described by driver, through ApplicationMaster. Executors would then directly talk to the driver as usual. (? Verify this.)

The applications can be submitted as:
>> spark-submit --master yarn --deploy-mode [client | cluster] ...

    ## Client mode
    In client mode, the driver runs on the machine which submits the application to the ResourceManager. This could be a remote machine (e.g. a laptop), or somewhere else on the cluster (is this how livy works?)

    ## Cluster mode
    In cluster mode, the driver runs in the ApplicationMaster container.


Resources:

1. youtube.com : sparkSummit : https://www.youtube.com/playlist?list=PL-x35fyliRwioDix9XjD3HptH8ro55SuB
2. Academic papers : (downloaded in the directory)
3. https://hadoop.apache.org/docs/r2.7.3/hadoop-yarn/hadoop-yarn-site/WritingYarnApplications.html
4. https://mapr.com/docs/60/MapROverview/c_yarn.html