CREATE TABLE Customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    surname VARCHAR(50)
);

CREATE TABLE Products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    price DECIMAL(10, 2)
);

CREATE TABLE Purchases (
    id SERIAL PRIMARY KEY,
    customer_id INT REFERENCES Customers(id),
    product_id INT REFERENCES Products(id),
    purchase_date DATE
);

INSERT INTO public.Customers (name, surname) VALUES
('Антон', 'Иванов'),
('Иван', 'Иванов'),
('Валентин', 'Петров'),
('Анна', 'Антонова'),
('Мария', 'Маркова');

INSERT INTO public.Products (name, price) VALUES
('Минеральная вода', 20.00),
('Хлеб', 30.00),
('Сметана', 50.00),
('Колбаса', 112.00),
('Сыр', 200.00);

INSERT INTO public.Purchases (customer_id, product_id, purchase_date) VALUES
(1, 1, '2020-01-14'),
(1, 2, '2020-01-15'),
(2, 3, '2020-01-16'),
(2, 4, '2020-01-17'),
(3, 1, '2020-01-18'),
(3, 1, '2020-01-18'),
(3, 1, '2020-01-18'),
(3, 1, '2020-01-18'),
(3, 1, '2020-01-18'),
(3, 1, '2020-01-18'),
(3, 2, '2020-01-19'),
(4, 5, '2020-01-20'),
(5, 1, '2020-01-21'),
(1, 3, '2020-01-22'),
(2, 5, '2020-01-23'),
(3, 4, '2020-01-24'),
(4, 2, '2020-01-25'),
(5, 3, '2020-01-26');

