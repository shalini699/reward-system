DELETE FROM transactions;
DELETE FROM customers;

INSERT INTO customers(id, name)
VALUES (1, 'John');

INSERT INTO customers(id, name)
VALUES (2, 'Mary');

INSERT INTO transactions
(id, customer_id, amount, transaction_date)
VALUES
(1, 1, 120, '2026-01-15'),
(2, 1, 75, '2026-01-20'),
(3, 1, 200, '2026-02-10'),
(4, 2, 150, '2026-01-25');