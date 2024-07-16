create table if not exists product_item(
    id INT PRIMARY KEY,
    name VARCHAR(8) not null,
    pid INT not null
);