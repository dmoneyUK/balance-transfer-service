DROP TABLE IF EXISTS Account;

CREATE TABLE Account (id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
AccountNumber INTEGER,
AccountHolder VARCHAR(30),
Balance DECIMAL(15,2)
);

INSERT INTO Account (AccountNumber,AccountHolder,Balance) VALUES (11111111,'a',9999999999999.00);
INSERT INTO Account (AccountNumber,AccountHolder,Balance) VALUES (22222222,'b',200.00);
INSERT INTO Account (AccountNumber,AccountHolder,Balance) VALUES (33333333,'c',500.00);
INSERT INTO Account (AccountNumber,AccountHolder,Balance) VALUES (44444444,'d',500.00);
INSERT INTO Account (AccountNumber,AccountHolder,Balance) VALUES (55555555,'e',500.00);
INSERT INTO Account (AccountNumber,AccountHolder,Balance) VALUES (66666666,'f',500.00);
