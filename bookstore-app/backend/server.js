const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');

const app = express();
const port = process.env.PORT || 8080;

// Middleware
app.use(cors({
  origin: ['http://localhost:3000', 'http://127.0.0.1:3000'],
  credentials: true
}));
app.use(express.json());

// Database connection
const pool = new Pool({
  user: 'bookstore_user',
  host: process.env.DATABASE_HOST || 'localhost',
  database: 'bookstore',
  password: 'bookstore_password',
  port: 5432,
});

// Test database connection
pool.query('SELECT NOW()', (err, res) => {
  if (err) {
    console.error('Database connection error:', err);
  } else {
    console.log('Database connected successfully');
  }
});

// Routes
app.get('/api/health', (req, res) => {
  res.json({ status: 'OK', message: 'Bookstore API is running' });
});

// Get all books
app.get('/api/books', async (req, res) => {
  try {
    const { page = 0, size = 20, title, author, genre, format } = req.query;
    let query = `
      SELECT b.*, 
             a.first_name as author_first_name, a.last_name as author_last_name,
             g.name as genre_name,
             p.name as publisher_name
      FROM books b
      JOIN authors a ON b.author_id = a.author_id
      JOIN genres g ON b.genre_id = g.genre_id
      JOIN publishers p ON b.publisher_id = p.publisher_id
    `;
    
    const conditions = [];
    const params = [];
    let paramCount = 0;

    if (title) {
      paramCount++;
      conditions.push(`LOWER(b.title) LIKE LOWER($${paramCount})`);
      params.push(`%${title}%`);
    }

    if (author) {
      paramCount++;
      conditions.push(`(LOWER(a.first_name) LIKE LOWER($${paramCount}) OR LOWER(a.last_name) LIKE LOWER($${paramCount}))`);
      params.push(`%${author}%`);
    }

    if (genre) {
      paramCount++;
      conditions.push(`LOWER(g.name) LIKE LOWER($${paramCount})`);
      params.push(`%${genre}%`);
    }

    if (format) {
      paramCount++;
      conditions.push(`b.format = $${paramCount}`);
      params.push(format.toUpperCase());
    }

    if (conditions.length > 0) {
      query += ' WHERE ' + conditions.join(' AND ');
    }

    query += ' ORDER BY b.title LIMIT $' + (paramCount + 1) + ' OFFSET $' + (paramCount + 2);
    params.push(parseInt(size), parseInt(page) * parseInt(size));

    const result = await pool.query(query, params);
    
    // Transform the data to match frontend expectations
    const books = result.rows.map(row => ({
      id: row.book_id,
      title: row.title,
      isbn: row.isbn,
      publicationDate: row.publication_date,
      price: parseFloat(row.price),
      format: row.format,
      description: row.description,
      coverImageUrl: row.cover_image_url,
      author: {
        id: row.author_id,
        firstName: row.author_first_name,
        lastName: row.author_last_name
      },
      genre: {
        id: row.genre_id,
        name: row.genre_name
      },
      publisher: {
        id: row.publisher_id,
        name: row.publisher_name
      }
    }));

    res.json(books);
  } catch (error) {
    console.error('Error fetching books:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Get book by ID
app.get('/api/books/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const query = `
      SELECT b.*, 
             a.first_name as author_first_name, a.last_name as author_last_name,
             g.name as genre_name,
             p.name as publisher_name
      FROM books b
      JOIN authors a ON b.author_id = a.author_id
      JOIN genres g ON b.genre_id = g.genre_id
      JOIN publishers p ON b.publisher_id = p.publisher_id
      WHERE b.book_id = $1
    `;
    
    const result = await pool.query(query, [id]);
    
    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Book not found' });
    }

    const row = result.rows[0];
    const book = {
      id: row.book_id,
      title: row.title,
      isbn: row.isbn,
      publicationDate: row.publication_date,
      price: parseFloat(row.price),
      format: row.format,
      description: row.description,
      coverImageUrl: row.cover_image_url,
      author: {
        id: row.author_id,
        firstName: row.author_first_name,
        lastName: row.author_last_name
      },
      genre: {
        id: row.genre_id,
        name: row.genre_name
      },
      publisher: {
        id: row.publisher_id,
        name: row.publisher_name
      }
    };

    res.json(book);
  } catch (error) {
    console.error('Error fetching book:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Search books
app.get('/api/books/search', async (req, res) => {
  try {
    const { q } = req.query;
    if (!q) {
      return res.json([]);
    }

    const query = `
      SELECT b.*, 
             a.first_name as author_first_name, a.last_name as author_last_name,
             g.name as genre_name,
             p.name as publisher_name
      FROM books b
      JOIN authors a ON b.author_id = a.author_id
      JOIN genres g ON b.genre_id = g.genre_id
      JOIN publishers p ON b.publisher_id = p.publisher_id
      WHERE LOWER(b.title) LIKE LOWER($1) 
         OR LOWER(b.isbn) LIKE LOWER($1)
         OR LOWER(a.first_name) LIKE LOWER($1)
         OR LOWER(a.last_name) LIKE LOWER($1)
      ORDER BY b.title
    `;
    
    const result = await pool.query(query, [`%${q}%`]);
    
    const books = result.rows.map(row => ({
      id: row.book_id,
      title: row.title,
      isbn: row.isbn,
      publicationDate: row.publication_date,
      price: parseFloat(row.price),
      format: row.format,
      description: row.description,
      coverImageUrl: row.cover_image_url,
      author: {
        id: row.author_id,
        firstName: row.author_first_name,
        lastName: row.author_last_name
      },
      genre: {
        id: row.genre_id,
        name: row.genre_name
      },
      publisher: {
        id: row.publisher_id,
        name: row.publisher_name
      }
    }));

    res.json(books);
  } catch (error) {
    console.error('Error searching books:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Get bestsellers
app.get('/api/books/bestsellers', async (req, res) => {
  try {
    const { limit = 5 } = req.query;
    const query = `
      SELECT b.*, 
             a.first_name as author_first_name, a.last_name as author_last_name,
             g.name as genre_name,
             p.name as publisher_name
      FROM books b
      JOIN authors a ON b.author_id = a.author_id
      JOIN genres g ON b.genre_id = g.genre_id
      JOIN publishers p ON b.publisher_id = p.publisher_id
      ORDER BY b.book_id DESC
      LIMIT $1
    `;
    
    const result = await pool.query(query, [parseInt(limit)]);
    
    const books = result.rows.map(row => ({
      id: row.book_id,
      title: row.title,
      isbn: row.isbn,
      publicationDate: row.publication_date,
      price: parseFloat(row.price),
      format: row.format,
      description: row.description,
      coverImageUrl: row.cover_image_url,
      author: {
        id: row.author_id,
        firstName: row.author_first_name,
        lastName: row.author_last_name
      },
      genre: {
        id: row.genre_id,
        name: row.genre_name
      },
      publisher: {
        id: row.publisher_id,
        name: row.publisher_name
      }
    }));

    res.json(books);
  } catch (error) {
    console.error('Error fetching bestsellers:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Get books by author
app.get('/api/books/author/:authorId', async (req, res) => {
  try {
    const { authorId } = req.params;
    const query = `
      SELECT b.*, 
             a.first_name as author_first_name, a.last_name as author_last_name,
             g.name as genre_name,
             p.name as publisher_name
      FROM books b
      JOIN authors a ON b.author_id = a.author_id
      JOIN genres g ON b.genre_id = g.genre_id
      JOIN publishers p ON b.publisher_id = p.publisher_id
      WHERE b.author_id = $1
      ORDER BY b.title
    `;
    
    const result = await pool.query(query, [authorId]);
    
    const books = result.rows.map(row => ({
      id: row.book_id,
      title: row.title,
      isbn: row.isbn,
      publicationDate: row.publication_date,
      price: parseFloat(row.price),
      format: row.format,
      description: row.description,
      coverImageUrl: row.cover_image_url,
      author: {
        id: row.author_id,
        firstName: row.author_first_name,
        lastName: row.author_last_name
      },
      genre: {
        id: row.genre_id,
        name: row.genre_name
      },
      publisher: {
        id: row.publisher_id,
        name: row.publisher_name
      }
    }));

    res.json(books);
  } catch (error) {
    console.error('Error fetching books by author:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Get books by genre
app.get('/api/books/genre/:genreId', async (req, res) => {
  try {
    const { genreId } = req.params;
    const query = `
      SELECT b.*, 
             a.first_name as author_first_name, a.last_name as author_last_name,
             g.name as genre_name,
             p.name as publisher_name
      FROM books b
      JOIN authors a ON b.author_id = a.author_id
      JOIN genres g ON b.genre_id = g.genre_id
      JOIN publishers p ON b.publisher_id = p.publisher_id
      WHERE b.genre_id = $1
      ORDER BY b.title
    `;
    
    const result = await pool.query(query, [genreId]);
    
    const books = result.rows.map(row => ({
      id: row.book_id,
      title: row.title,
      isbn: row.isbn,
      publicationDate: row.publication_date,
      price: parseFloat(row.price),
      format: row.format,
      description: row.description,
      coverImageUrl: row.cover_image_url,
      author: {
        id: row.author_id,
        firstName: row.author_first_name,
        lastName: row.author_last_name
      },
      genre: {
        id: row.genre_id,
        name: row.genre_name
      },
      publisher: {
        id: row.publisher_id,
        name: row.publisher_name
      }
    }));

    res.json(books);
  } catch (error) {
    console.error('Error fetching books by genre:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Get books by format
app.get('/api/books/format/:format', async (req, res) => {
  try {
    const { format } = req.params;
    const query = `
      SELECT b.*, 
             a.first_name as author_first_name, a.last_name as author_last_name,
             g.name as genre_name,
             p.name as publisher_name
      FROM books b
      JOIN authors a ON b.author_id = a.author_id
      JOIN genres g ON b.genre_id = g.genre_id
      JOIN publishers p ON b.publisher_id = p.publisher_id
      WHERE b.format = $1
      ORDER BY b.title
    `;
    
    const result = await pool.query(query, [format.toUpperCase()]);
    
    const books = result.rows.map(row => ({
      id: row.book_id,
      title: row.title,
      isbn: row.isbn,
      publicationDate: row.publication_date,
      price: parseFloat(row.price),
      format: row.format,
      description: row.description,
      coverImageUrl: row.cover_image_url,
      author: {
        id: row.author_id,
        firstName: row.author_first_name,
        lastName: row.author_last_name
      },
      genre: {
        id: row.genre_id,
        name: row.genre_name
      },
      publisher: {
        id: row.publisher_id,
        name: row.publisher_name
      }
    }));

    res.json(books);
  } catch (error) {
    console.error('Error fetching books by format:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Get all authors
app.get('/api/authors', async (req, res) => {
  try {
    const query = `
      SELECT author_id, first_name, last_name, biography, birth_date, email, created_at
      FROM authors
      ORDER BY last_name, first_name
    `;
    
    const result = await pool.query(query);
    
    const authors = result.rows.map(row => ({
      id: row.author_id,
      firstName: row.first_name,
      lastName: row.last_name,
      biography: row.biography,
      birthDate: row.birth_date,
      email: row.email,
      createdAt: row.created_at
    }));

    res.json(authors);
  } catch (error) {
    console.error('Error fetching authors:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Get all genres
app.get('/api/genres', async (req, res) => {
  try {
    const query = `
      SELECT genre_id, name, description, created_at
      FROM genres
      ORDER BY name
    `;
    
    const result = await pool.query(query);
    
    const genres = result.rows.map(row => ({
      id: row.genre_id,
      name: row.name,
      description: row.description,
      createdAt: row.created_at
    }));

    res.json(genres);
  } catch (error) {
    console.error('Error fetching genres:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Start server
app.listen(port, () => {
  console.log(`Bookstore API server running on port ${port}`);
});
