# QBit's Event Bus Cluster Sample


## Install



## Usage

Start the components:
- Consul server agent
   - it should listen on the standard port 
- 1st instance of the service
   - `TransactionClassifierServiceInstance1`
- start 2nd instance of the service
   - `TransactionClassifierServiceInstance2`

Send a message to the first istance:
```bash
$ curl -X POST  -H "Content-Type: application/json" http://localhost:8881/classify -d '{ "fromAccountIBAN":"123", "toAccountIBAN":"456", "amount":17 }'
"success"
$
```

## TODO:
- event containing the financial transaction
