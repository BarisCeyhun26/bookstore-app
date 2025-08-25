-- Online Bookstore Database Schema
-- 3NF Normalized Database Design

-- Create tables
CREATE TABLE publishers (
    publisher_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE authors (
    author_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    biography TEXT,
    birth_date DATE,
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE genres (
    genre_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE,
    author_id INTEGER REFERENCES authors(author_id) ON DELETE CASCADE,
    publisher_id INTEGER REFERENCES publishers(publisher_id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres(genre_id) ON DELETE CASCADE,
    publication_date DATE,
    price DECIMAL(10,2) NOT NULL,
    format VARCHAR(20) NOT NULL CHECK (format IN ('physical', 'e-book', 'audiobook')),
    description TEXT,
    cover_image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory (
    inventory_id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES books(book_id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    is_member BOOLEAN DEFAULT FALSE,
    membership_discount DECIMAL(3,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(customer_id) ON DELETE CASCADE,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'processing', 'shipped', 'delivered', 'cancelled')),
    shipping_address TEXT,
    payment_method VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    order_item_id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(order_id) ON DELETE CASCADE,
    book_id INTEGER REFERENCES books(book_id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shopping_cart (
    cart_id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(customer_id) ON DELETE CASCADE,
    book_id INTEGER REFERENCES books(book_id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(customer_id, book_id)
);

-- Create indexes for better performance
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author_id ON books(author_id);
CREATE INDEX idx_books_genre_id ON books(genre_id);
CREATE INDEX idx_books_format ON books(format);
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_order_date ON orders(order_date);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_inventory_book_id ON inventory(book_id);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_authors_last_name ON authors(last_name);

-- Insert sample data
INSERT INTO publishers (name, address, phone, email) VALUES
('Penguin Random House', '1745 Broadway, New York, NY 10019', '+1-212-782-9000', 'info@penguinrandomhouse.com'),
('HarperCollins', '195 Broadway, New York, NY 10007', '+1-212-207-7000', 'info@harpercollins.com'),
('Simon & Schuster', '1230 Avenue of the Americas, New York, NY 10020', '+1-212-698-7000', 'info@simonandschuster.com'),
('Macmillan', '120 Broadway, New York, NY 10271', '+1-646-307-5151', 'info@macmillan.com'),
('Hachette Book Group', '1290 Avenue of the Americas, New York, NY 10104', '+1-212-364-1100', 'info@hachettebookgroup.com'),
('Scholastic', '557 Broadway, New York, NY 10012', '+1-212-343-6100', 'info@scholastic.com'),
('Bloomsbury', '1385 Broadway, New York, NY 10018', '+1-212-419-5300', 'info@bloomsbury.com'),
('Faber & Faber', '74-77 Great Russell Street, London WC1B 3DA', '+44-20-7927-3800', 'info@faber.co.uk'),
('Vintage Books', '1745 Broadway, New York, NY 10019', '+1-212-782-9000', 'info@vintagebooks.com'),
('Doubleday', '1745 Broadway, New York, NY 10019', '+1-212-782-9000', 'info@doubleday.com');

INSERT INTO authors (first_name, last_name, biography, birth_date, email) VALUES
('J.K.', 'Rowling', 'British author best known for the Harry Potter series', '1965-07-31', 'jk.rowling@example.com'),
('George R.R.', 'Martin', 'American novelist and short story writer', '1948-09-20', 'grrm@example.com'),
('Stephen', 'King', 'American author of horror, supernatural fiction, suspense, and fantasy novels', '1947-09-21', 'stephen.king@example.com'),
('Agatha', 'Christie', 'English writer known for her detective novels', '1890-09-15', 'agatha.christie@example.com'),
('Ernest', 'Hemingway', 'American novelist, short-story writer, and journalist', '1899-07-21', 'ernest.hemingway@example.com'),
('Jane', 'Austen', 'English novelist known for her romantic fiction', '1775-12-16', 'jane.austen@example.com'),
('Charles', 'Dickens', 'English writer and social critic', '1812-02-07', 'charles.dickens@example.com'),
('Mark', 'Twain', 'American writer, humorist, entrepreneur, publisher, and lecturer', '1835-11-30', 'mark.twain@example.com'),
('Virginia', 'Woolf', 'English writer, considered one of the most important modernist authors', '1882-01-25', 'virginia.woolf@example.com'),
('F. Scott', 'Fitzgerald', 'American novelist, essayist, screenwriter, and short-story writer', '1896-09-24', 'fscott.fitzgerald@example.com');

INSERT INTO genres (name, description) VALUES
('Fantasy', 'Fiction with magical and supernatural elements'),
('Science Fiction', 'Fiction dealing with futuristic science and technology'),
('Mystery', 'Fiction involving crime and detective work'),
('Romance', 'Fiction focusing on romantic relationships'),
('Horror', 'Fiction intended to frighten or scare readers'),
('Historical Fiction', 'Fiction set in the past'),
('Literary Fiction', 'Fiction with artistic merit and literary value'),
('Thriller', 'Fiction with suspense and excitement'),
('Biography', 'Non-fiction about a person''s life'),
('Self-Help', 'Non-fiction books for personal development');

INSERT INTO books (title, isbn, author_id, publisher_id, genre_id, publication_date, price, format, description, cover_image_url) VALUES
('Harry Potter and the Philosopher''s Stone', '9780747532699', 1, 1, 1, '1997-06-26', 29.99, 'physical', 'The first book in the Harry Potter series', '/covers/harry-potter-1.jpg'),
('Harry Potter and the Philosopher''s Stone', '9780747532698', 1, 1, 1, '1997-06-26', 19.99, 'e-book', 'The first book in the Harry Potter series', '/covers/harry-potter-1.jpg'),
('A Game of Thrones', '9780553103540', 2, 2, 1, '1996-08-01', 34.99, 'physical', 'The first book in A Song of Ice and Fire series', '/covers/game-of-thrones.jpg'),
('The Shining', '9780385121675', 3, 3, 5, '1977-01-28', 24.99, 'physical', 'A horror novel about a haunted hotel', '/covers/shining.jpg'),
('Murder on the Orient Express', '9780062073495', 4, 4, 3, '1934-01-01', 19.99, 'physical', 'A classic detective novel', '/covers/murder-orient-express.jpg'),
('The Old Man and the Sea', '9780684801223', 5, 5, 7, '1952-09-01', 15.99, 'physical', 'A novel about an aging fisherman', '/covers/old-man-sea.jpg'),
('Pride and Prejudice', '9780141439518', 6, 6, 4, '1813-01-28', 12.99, 'physical', 'A classic romance novel', '/covers/pride-prejudice.jpg'),
('Great Expectations', '9780141439563', 7, 7, 6, '1861-12-01', 18.99, 'physical', 'A coming-of-age novel', '/covers/great-expectations.jpg'),
('The Adventures of Tom Sawyer', '9780143039563', 8, 8, 6, '1876-06-01', 16.99, 'physical', 'A novel about a young boy''s adventures', '/covers/tom-sawyer.jpg'),
('Mrs. Dalloway', '9780156628709', 9, 9, 7, '1925-05-14', 14.99, 'physical', 'A modernist novel', '/covers/mrs-dalloway.jpg'),
('The Great Gatsby', '9780743273565', 10, 10, 7, '1925-04-10', 17.99, 'physical', 'A novel about the American Dream', '/covers/great-gatsby.jpg'),
('Harry Potter and the Chamber of Secrets', '9780747538493', 1, 1, 1, '1998-07-02', 29.99, 'e-book', 'The second book in the Harry Potter series', '/covers/harry-potter-2.jpg'),
('A Clash of Kings', '9780553108033', 2, 2, 1, '1998-11-16', 34.99, 'audiobook', 'The second book in A Song of Ice and Fire series', '/covers/clash-kings.jpg'),
('Carrie', '9780385086950', 3, 3, 5, '1974-04-05', 22.99, 'physical', 'Stephen King''s first published novel', '/covers/carrie.jpg'),
('And Then There Were None', '9780062073488', 4, 4, 3, '1939-11-06', 18.99, 'e-book', 'A mystery novel about ten strangers', '/covers/and-then-none.jpg');

INSERT INTO inventory (book_id, quantity) VALUES
(1, 50), (2, 100), (3, 30), (4, 25), (5, 40), (6, 35), (7, 45), (8, 20), (9, 30), (10, 15),
(11, 25), (12, 100), (13, 50), (14, 40), (15, 100);

INSERT INTO customers (first_name, last_name, email, password_hash, phone, address, is_member, membership_discount) VALUES
('John', 'Doe', 'john.doe@example.com', '$2a$10$hashedpassword1', '+1-555-0101', '123 Main St, New York, NY 10001', TRUE, 0.10),
('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$hashedpassword2', '+1-555-0102', '456 Oak Ave, Los Angeles, CA 90210', TRUE, 0.15),
('Bob', 'Johnson', 'bob.johnson@example.com', '$2a$10$hashedpassword3', '+1-555-0103', '789 Pine St, Chicago, IL 60601', FALSE, 0.00),
('Alice', 'Brown', 'alice.brown@example.com', '$2a$10$hashedpassword4', '+1-555-0104', '321 Elm St, Houston, TX 77001', TRUE, 0.10),
('Charlie', 'Wilson', 'charlie.wilson@example.com', '$2a$10$hashedpassword5', '+1-555-0105', '654 Maple Dr, Phoenix, AZ 85001', FALSE, 0.00),
('Diana', 'Davis', 'diana.davis@example.com', '$2a$10$hashedpassword6', '+1-555-0106', '987 Cedar Ln, Philadelphia, PA 19101', TRUE, 0.15),
('Edward', 'Miller', 'edward.miller@example.com', '$2a$10$hashedpassword7', '+1-555-0107', '147 Birch Rd, San Antonio, TX 78201', FALSE, 0.00),
('Fiona', 'Garcia', 'fiona.garcia@example.com', '$2a$10$hashedpassword8', '+1-555-0108', '258 Spruce Ave, San Diego, CA 92101', TRUE, 0.10),
('George', 'Martinez', 'george.martinez@example.com', '$2a$10$hashedpassword9', '+1-555-0109', '369 Willow Way, Dallas, TX 75201', FALSE, 0.00),
('Helen', 'Anderson', 'helen.anderson@example.com', '$2a$10$hashedpassword10', '+1-555-0110', '741 Poplar Pl, San Jose, CA 95101', TRUE, 0.15);

INSERT INTO orders (customer_id, total_price, status, shipping_address, payment_method) VALUES
(1, 89.97, 'delivered', '123 Main St, New York, NY 10001', 'credit_card'),
(2, 69.98, 'shipped', '456 Oak Ave, Los Angeles, CA 90210', 'paypal'),
(3, 44.98, 'processing', '789 Pine St, Chicago, IL 60601', 'credit_card'),
(4, 59.97, 'delivered', '321 Elm St, Houston, TX 77001', 'credit_card'),
(5, 34.99, 'pending', '654 Maple Dr, Phoenix, AZ 85001', 'paypal'),
(6, 79.96, 'shipped', '987 Cedar Ln, Philadelphia, PA 19101', 'credit_card'),
(7, 29.99, 'delivered', '147 Birch Rd, San Antonio, TX 78201', 'credit_card'),
(8, 54.97, 'processing', '258 Spruce Ave, San Diego, CA 92101', 'paypal'),
(9, 39.98, 'pending', '369 Willow Way, Dallas, TX 75201', 'credit_card'),
(10, 64.95, 'shipped', '741 Poplar Pl, San Jose, CA 95101', 'credit_card');

INSERT INTO order_items (order_id, book_id, quantity, unit_price, total_price) VALUES
(1, 1, 1, 29.99, 29.99),
(1, 3, 1, 34.99, 34.99),
(1, 5, 1, 19.99, 19.99),
(2, 2, 1, 19.99, 19.99),
(2, 4, 1, 24.99, 24.99),
(2, 6, 1, 15.99, 15.99),
(3, 7, 1, 12.99, 12.99),
(3, 8, 1, 18.99, 18.99),
(3, 9, 1, 16.99, 16.99),
(4, 10, 1, 14.99, 14.99),
(4, 11, 1, 17.99, 17.99),
(4, 12, 1, 29.99, 29.99),
(5, 13, 1, 34.99, 34.99),
(6, 14, 1, 22.99, 22.99),
(6, 15, 1, 18.99, 18.99),
(6, 1, 1, 29.99, 29.99),
(7, 2, 1, 19.99, 19.99),
(7, 3, 1, 34.99, 34.99),
(8, 4, 1, 24.99, 24.99),
(8, 5, 1, 19.99, 19.99),
(8, 6, 1, 15.99, 15.99),
(9, 7, 1, 12.99, 12.99),
(9, 8, 1, 18.99, 18.99),
(10, 9, 1, 16.99, 16.99),
(10, 10, 1, 14.99, 14.99),
(10, 11, 1, 17.99, 17.99);

INSERT INTO shopping_cart (customer_id, book_id, quantity) VALUES
(1, 12, 1),
(1, 13, 2),
(2, 14, 1),
(3, 15, 1),
(4, 1, 1),
(5, 2, 1),
(6, 3, 1),
(7, 4, 1),
(8, 5, 1),
(9, 6, 1),
(10, 7, 1);
