# money-transfer-service

## Introduciton
This is a money transfer server application providing the service to sent money from one bank account to another.
To lanuch a transfer transaction. The service will need the following details of the person or organisation you are paying.

* Name of the person or business you’re paying.
* Six-digit sort code of the account you’re paying.
* Eight-digit account number of the account you’re paying.
* A payment reference (often your name or customer number) to let them know the money came from you.

(For the time reason, the service does not do the scheduled transfer. The transaction will happen immediately.
Also, the implementation does not use any database. All the data wil be saved in a map);

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



