# QBit's Event Bus Cluster Sample

This is a sample implementation of a clustered event bus using QBit library.

## Usage

### Start the components:
- Consul server agent
   - it should listen on the standard endpoint `localhost:8500`
- 1st instance of the service
   - `TransactionClassifierServiceInstance1`
- 2nd instance of the service
   - `TransactionClassifierServiceInstance2`

> _Note:_ Send the request to one of the instances and you will see that:
> - the http request is processed by that instance
> - the event (generated based on the request) is consumed by both instances<br/>(through the help of the clustered event bus).

### Example 1: As a GET request to the first instance:

- sending the request:
```bash
$ curl -sS http://localhost:8881/classify/txn/123/456/17/EUR  -H "Content-Type: application/json"
"success"
$
```
- 1st instance output will show:
```
[classifyAsGet] fromAccount='123' toAccount='456' amount=17.000000 currency='EUR'

[onClassifyTransactionEvent] Transaction{ fromAccount='123', toAccount='456', amount=17.000000, currency='EUR' }
```
- 2nd instance output will show:
```
[onClassifyTransactionEvent] Transaction{ fromAccount='123', toAccount='456', amount=17.000000, currency='EUR' }
```

### Example 2: As a POST request to the second instance:

- sending the request:
```bash
$ curl -sS -X POST  http://localhost:8881/classify/txn \
       -H "Content-Type: application/json" \
       -d '{ "fromAccount":"123", "toAccount":"456", "amount":17, "currency":"EUR" }'
"success"
$
```
- 1st instance output will show:
```
[classifyAsPost] transaction=Transaction{ fromAccount='123', toAccount='456', amount=17.000000, currency='EUR' }

[onClassifyTransactionEvent] Transaction{ fromAccount='123', toAccount='456', amount=17.000000, currency='EUR' }
```

- 2nd instance output will show:
```
[onClassifyTransactionEvent] Transaction{ fromAccount='123', toAccount='456', amount=17.000000, currency='EUR' }
```
Both instances are using the console as the standard output as configured on `logback.xml`.

