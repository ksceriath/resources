#DistributedSystems

Apache Airflow is a platform to build and run workflows.

### Architecture
It contains following components:
1. Scheduler
	- Triggers scheduled workflows
	- Submits tasks to executor to run
		- QQ : Does it submit individual task at a time? or the entire DAG?
2. Executor
	- Runs the tasks given by scheduler
	- **the executor's logic runs inside the scheduler process - if you're running a scheduler, you're running the executor.**
	- Default installation runs everything inside the scheduler process itself.
		- *but most production-suitable executors actually push task execution out to workers*
			- Executor may internally introduce other components like a *task queue* to talk to workers, etc
3. Webserver
	-> Gives a UI
4. DAG directory
	- Stores the python files containing the DAGs
	- this is read by : **scheduler**, **executor** and any **workers**
	- Airflow >=1.10.7 supports *DAG serialization and DB persistence*
		- Scheduler reads the DAGs and persistes inside the database
		- Webserver then reads the DAGs from the database
5. Metadata database
	- used by: scheduler, executor and webserver
	- to store state
	- This also stores the DAG code by the DAG name, and also the DAG file location


### DAG
=> Nodes in the DAGs are *tasks*

3 types of tasks:
1. Operators : existing predefined tasks
2. Sensors : special operators which wait for an external event to happen
3. @task : annotated custom python functions

### Executor

Executors can 
- run the task locally inside the scheduler process,
	- local executor
	- sequential executor
- or, run the task remotely in a pool of workers
	- [[Airflow#kubernetes executor|Kubernetes Executor]]
	- celery executor
	- dask executor
	- celery kubernetes executor
	- local kubernentes executor

#### Kubernetes Executor
- runs as a process inside Scheduler - Scheduler need not run on k8s, but needs access to k8s cluster
- On task submission, executor requests a worker pod from ‘k8s API’ - which
	- runs the task
	- reports the result
	- terminates
- worker process require access to DAG files … Here, it can be achieved as
	- include dags in the image used by the worker image
	- git-sync, which pulls the code from the dags repo before starting the worker container
	- storing dags in a PV, which is then mounted
- `pod_template_file` option in `kubernetes_executor` section of airflow.cfg → customize the pod used for worker processes
	- `pod_template_file` must have a container named `base`
	- *you are free to create `sidecar` containers*
