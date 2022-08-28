... is used to maintain consistency in entities like a [[replicated log]]

- It should never return an incorrect result under all [[Byzantine#Non Byzantine Conditions|non-byzantine]] conditions
- It should ensure availability as long as any **majority** of the servers are operational and can communicate with each other and with the clients.
- It should not depend on timing to ensure the consistency of the logs





#DistributedSystems 