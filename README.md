# money-transfer-service

## Introduciton
This is a money transfer server application providing the service to sent money from one bank account to another.
To launch a transfer transaction, the service will need the following details of the person or organisation you are paying.
* Source account: Eight-digit account number of the account
* Target account: Eight-digit account number of the account
* Amount: decimal scale 2 (assuming Currency code to be GBP for now to avoid exchange logic.)

## Assumptions and Limitations
For the time reason, I made assumptions as below to keep things simple. They could be improved:
* The service does not do the scheduled transfer. The transaction will happen immediately.
* The "source" and "target" accounts are both on-us. If transfer to bank accounts not in the system, it will get failure.
(it should invoke other system and is be covered by this implementation.)
* No format validations for the request body. Ideally, there should be validations for the length and format of name, account numbers.
* No currency exchange is not considered. Assuming currency is GBP.

## Demo Tests
revolut.infrastructure.rest.TransferTransactionEndpointsFTest 
(Test accounts are loaded when server startup, sql script is in: /src/main/resources/demo.sql)

## Runtime Environment
* Java 1.8.0_152
* Jetty Server 9.4.14.v20181114
* H2 1.4.197

## Start Server
Run the mvn build command below from the project's root folder to build the
```bash
mvn clean install
```
Execuatble jar file "target/money-transfer-service-1.0-SNAPSHOT.jar";
```bash
java -jar target/money-transfer-service-1.0-SNAPSHOT.jar
```
 
 
## API
* url: /revolut/transfer
* method: POST
* request body:
```json
{
  "source": 11111111,
  "target": 22222222,
  "amount": 100
}
```
* Response:
```
200: when transfer performed successfully
403: when transfer failed due to account issue, including account not found, source account has insufficient balance;
409: when transfer failed and rollback
500: when unexpected exception happens
```

## Questions:
* Without spring transactional management? 
Transactional management should be in the service layer. As spring is not recommended to use and I am not
familiar with alternatives, just move the responsibility to the dao. Not a good practice and I would not
use this approach in any production, but manually write transaction framework seems too heavy for this test. 
Really curious what framework does Revolut use for transaction management.
