DROP TABLE IF EXISTS Account;

CREATE TABLE Account (AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
AccountHolder VARCHAR(30),
Balance DECIMAL(15,2),
AccountNumber INTEGER
);

INSERT INTO Account (AccountHolder,Balance,AccountNumber) VALUES ('a',100.00,11111111);
INSERT INTO Account (AccountHolder,Balance,AccountNumber) VALUES ('b',9999999999999.00,22222222);
INSERT INTO Account (AccountHolder,Balance,AccountNumber) VALUES ('c',500.00,33333333);
INSERT INTO Account (AccountHolder,Balance,AccountNumber) VALUES ('d',500.00,44444444);
INSERT INTO Account (AccountHolder,Balance,AccountNumber) VALUES ('e',500.00,55555555);
INSERT INTO Account (AccountHolder,Balance,AccountNumber) VALUES ('f',500.00,66666666);
