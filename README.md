# money-transfer-service

## Introduciton
This is a money transfer server application providing the service to sent money from one bank account to another.
To lanuch a transfer transaction. The service will need the following details of the person or organisation you are paying.
<ul>From account</ul>

    - Name of the person or business sending money.
    - Six-digit sort code of the account
    - Eight-digit account number of the account

<ul>To account</ul>

    - Name of the person or business receiving money
    - Six-digit sort code of the account
    - Eight-digit account number of the account

<ul> A reference customerised by the user. </ul>
<ul> Amount (Currency is hard code to be GBP for now) </ul>

## Assumptions and Limitations
For the time reaason, I made assumptions as below to keep things simple. They ca be improved.
* The service does not do the scheduled transfer. The transaction will happen immediately.
* The "From" and "To" accounts are both on-us. If transfer to bank accounts not in the system, it will get failure.
(it should invoke other system and is be covered by this implementation.)
* No many format validations for the request body. Ideally, there should be validations for the length and format of name, reference, sort code, account numbers.
* No currency is not considered. Assuming currency is GBP.
* No database is used, all account data are store in a map.

## Runtime Environment
* Java 1.8.0_152
* Jetty Server 9.4.14.v20181114

## Start Server
* run the command "mvn clean install" from the project's root folder to build the
 execuatble jar file "target/money-transfer-service-1.0-SNAPSHOT.jar";
* run the command "java -jar target/money-transfer-service-1.0-SNAPSHOT.jar";
 
## API
<li> url: /revolut/transfer </li>
<li> method: POST </li>
<li> body: </li>


### Questions:
* Without spring transactional management
