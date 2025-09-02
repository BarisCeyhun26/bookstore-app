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

-- Enhanced customers table for authentication
CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_member BOOLEAN DEFAULT FALSE,
    membership_discount DECIMAL(3,2) DEFAULT 0.00,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Roles table for authorization
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User roles junction table
CREATE TABLE user_roles (
    user_id INTEGER REFERENCES customers(customer_id) ON DELETE CASCADE,
    role_id INTEGER REFERENCES roles(role_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
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
CREATE INDEX idx_inventory_book_id ON inventory(inventory_id);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_username ON customers(username);
CREATE INDEX idx_authors_last_name ON authors(last_name);

-- Insert basic roles
INSERT INTO roles (name, description) VALUES
('USER', 'Regular customer user'),
('ADMIN', 'Administrator with full access'),
('MODERATOR', 'Moderator with limited admin access');

-- Insert admin user (password: admin123)
INSERT INTO customers (username, first_name, last_name, email, password_hash, is_active, is_member) VALUES
('admin', 'Admin', 'User', 'admin@bookstore.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE, TRUE);

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);

-- Insert sample publishers
INSERT INTO publishers (name, address, phone, email) VALUES
('Penguin Random House', '1745 Broadway, New York, NY 10019', '+1-212-782-9000', 'info@penguinrandomhouse.com'),
('HarperCollins', '195 Broadway, New York, NY 10007', '+1-212-207-7000', 'info@harpercollins.com'),
('Simon & Schuster', '1230 Avenue of the Americas, New York, NY 10020', '+1-212-698-7000', 'info@simonandschuster.com'),
('Macmillan Publishers', '120 Broadway, New York, NY 10271', '+1-646-307-5151', 'info@macmillan.com');

-- Insert sample authors
INSERT INTO authors (first_name, last_name, biography, birth_date, email) VALUES
('J.K.', 'Rowling', 'British author, best known for the Harry Potter fantasy series', '1965-07-31', 'jkrowling@example.com'),
('George', 'Orwell', 'English novelist, essayist, journalist and critic', '1903-06-25', 'george.orwell@example.com'),
('Harper', 'Lee', 'American novelist best known for To Kill a Mockingbird', '1926-04-28', 'harper.lee@example.com'),
('F. Scott', 'Fitzgerald', 'American novelist and short story writer', '1896-09-24', 'fscott.fitzgerald@example.com'),
('Jane', 'Austen', 'English novelist known primarily for her six major novels', '1775-12-16', 'jane.austen@example.com'),
('Mark', 'Twain', 'American writer, humorist, entrepreneur, publisher, and lecturer', '1835-11-30', 'mark.twain@example.com');

-- Insert sample genres
INSERT INTO genres (name, description) VALUES
('Fantasy', 'Fantasy literature featuring magic, supernatural elements, and imaginary worlds'),
('Dystopian Fiction', 'Fiction set in a society that is undesirable or frightening'),
('Classic Literature', 'Timeless works of literature that have stood the test of time'),
('Romance', 'Fiction focusing on romantic relationships and love stories'),
('Adventure', 'Fiction featuring exciting, dangerous, or unusual experiences'),
('Historical Fiction', 'Fiction set in the past, often during significant historical events');

-- Insert sample books
INSERT INTO books (title, isbn, author_id, publisher_id, genre_id, publication_date, price, format, description, cover_image_url) VALUES
('Harry Potter and the Philosopher''s Stone', '9780747532699', 1, 1, 1, '1997-06-26', 12.99, 'physical', 'The first book in the Harry Potter series, following the adventures of a young wizard.', 'https://images.example.com/harry-potter-1.jpg'),
('1984', '9780451524935', 2, 2, 2, '1949-06-08', 9.99, 'physical', 'A dystopian social science fiction novel about totalitarian control.', 'https://images.example.com/1984.jpg'),
('To Kill a Mockingbird', '9780061120084', 3, 3, 3, '1960-07-11', 11.99, 'physical', 'A novel about racial injustice and childhood innocence in the American South.', 'https://images.example.com/mockingbird.jpg'),
('The Great Gatsby', '9780743273565', 4, 4, 3, '1925-04-10', 10.99, 'physical', 'A story of the fabulously wealthy Jay Gatsby and his love for Daisy Buchanan.', 'https://images.example.com/gatsby.jpg'),
('Pride and Prejudice', '9780141439518', 5, 1, 4, '1813-01-28', 8.99, 'physical', 'A romantic novel about Elizabeth Bennet and Mr. Darcy.', 'https://images.example.com/pride-prejudice.jpg'),
('The Adventures of Tom Sawyer', '9780486400778', 6, 2, 5, '1876-12-01', 7.99, 'physical', 'A novel about a young boy growing up along the Mississippi River.', 'https://images.example.com/tom-sawyer.jpg'),
('Harry Potter and the Chamber of Secrets', '9780747538493', 1, 1, 1, '1998-07-02', 13.99, 'e-book', 'The second book in the Harry Potter series.', 'https://images.example.com/harry-potter-2.jpg'),
('Animal Farm', '9780451526342', 2, 2, 2, '1945-08-17', 8.99, 'e-book', 'An allegorical novella about farm animals who rebel against their human farmer.', 'https://images.example.com/animal-farm.jpg'),
('Emma', '9780141439587', 5, 3, 4, '1815-12-23', 9.99, 'audiobook', 'A novel about Emma Woodhouse, a young woman who meddles in the love lives of her friends.', 'https://images.example.com/emma.jpg'),
('The Adventures of Huckleberry Finn', '9780486280615', 6, 4, 5, '1884-12-10', 8.99, 'audiobook', 'A novel about Huck Finn and his journey down the Mississippi River.', 'https://images.example.com/huck-finn.jpg');

-- Insert inventory for books
INSERT INTO inventory (book_id, quantity) VALUES
(1, 50), (2, 75), (3, 60), (4, 45), (5, 80), (6, 70), (7, 100), (8, 55), (9, 40), (10, 65);
