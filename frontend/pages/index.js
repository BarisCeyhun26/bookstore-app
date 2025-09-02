import { useEffect, useState } from 'react';
import { useBookStore } from '../lib/store';
import Header from '../components/Header';
import BookGrid from '../components/BookGrid';
import SearchBar from '../components/SearchBar';
import BestsellersSection from '../components/BestsellersSection';

export default function HomePage() {
  const { 
    books, 
    authors, 
    genres,
    loading, 
    error, 
    fetchBooks, 
    fetchAuthors,
    fetchGenres,
    getBestsellers 
  } = useBookStore();
  const [activeTab, setActiveTab] = useState('books');

  useEffect(() => {
    fetchBooks();
    getBestsellers();
    fetchAuthors(); // Pre-load authors
    fetchGenres();  // Pre-load genres
  }, []); // Empty dependency array to run only once

  useEffect(() => {
    if (activeTab === 'authors' && authors.length === 0) {
      fetchAuthors();
    }
    if (activeTab === 'genres' && genres.length === 0) {
      fetchGenres();
    }
  }, [activeTab, authors.length, genres.length, fetchAuthors, fetchGenres]);

  return (

<div className="min-h-screen bg-gray-50">
      <Header />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Hero Section */}
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            Welcome to Our Online Bookstore
          </h1>
          <p className="text-xl text-gray-600 mb-8">
            Discover thousands of books across all genres
          </p>
          <SearchBar />
        </div>

        {/* Bestsellers Section */}
        <BestsellersSection />

        {/* Navigation Tabs */}
        <div className="mt-16 mb-8">
          <div className="border-b border-gray-200">
            <nav className="-mb-px flex space-x-8">
              <button
                onClick={() => setActiveTab('books')}
                className={`py-2 px-1 border-b-2 font-medium text-sm ${
                  activeTab === 'books'
                    ? 'border-primary-500 text-primary-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                Books
              </button>
              <button
                onClick={() => setActiveTab('authors')}
                className={`py-2 px-1 border-b-2 font-medium text-sm ${
                  activeTab === 'authors'
                    ? 'border-primary-500 text-primary-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                Authors
              </button>
              <button
                onClick={() => setActiveTab('genres')}
                className={`py-2 px-1 border-b-2 font-medium text-sm ${
                  activeTab === 'genres'
                    ? 'border-primary-500 text-primary-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                Genres
              </button>
            </nav>
          </div>
        </div>

        {/* Content Sections */}
        {activeTab === 'books' && (
          <section>
            <h2 className="text-2xl font-bold text-gray-900 mb-6">All Books</h2>
                    {loading && (
          <div className="flex justify-center items-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
          </div>
        )}
        
        {error && (
          <div className="text-center py-12">
            <p className="text-red-600">Error: {error}</p>
            <button 
              onClick={() => fetchBooks()} 
              className="mt-2 px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700"
            >
              Retry
            </button>
          </div>
        )}
        
        {!loading && !error && (
          <BookGrid books={books} />
        )}
          </section>
        )}

        {activeTab === 'authors' && (
          <section>
            <h2 className="text-2xl font-bold text-gray-900 mb-6">All Authors</h2>
            {loading && (
              <div className="flex justify-center items-center py-12">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
              </div>
            )}
            
            {error && (
              <div className="text-center py-12">
                <p className="text-red-600">Error: {error}</p>
                <button 
                  onClick={() => fetchAuthors()} 
                  className="mt-2 px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700"
                >
                  Retry
                </button>
              </div>
            )}
            
            {!loading && !error && (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {authors.map((author) => (
                  <div key={author.id} className="bg-white rounded-lg shadow-sm p-6">
                    <h3 className="text-lg font-semibold text-gray-900 mb-2">
                      {author.firstName} {author.lastName}
                    </h3>
                    {author.biography && (
                      <p className="text-gray-600 text-sm mb-3 line-clamp-3">
                        {author.biography}
                      </p>
                    )}
                    {author.email && (
                      <p className="text-gray-500 text-sm">
                        📧 {author.email}
                      </p>
                    )}
                  </div>
                ))}
              </div>
            )}
          </section>
        )}

        {activeTab === 'genres' && (
          <section>
            <h2 className="text-2xl font-bold text-gray-900 mb-6">All Genres</h2>
            {loading && (
              <div className="flex justify-center items-center py-12">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
              </div>
            )}
            
            {error && (
              <div className="text-center py-12">
                <p className="text-red-600">Error: {error}</p>
                <button 
                  onClick={() => fetchGenres()} 
                  className="mt-2 px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700"
                >
                  Retry
                </button>
              </div>
            )}
            
            {!loading && !error && (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {genres.map((genre) => (
                  <div key={genre.id} className="bg-white rounded-lg shadow-sm p-6">
                    <h3 className="text-lg font-semibold text-gray-900 mb-2">
                      {genre.name}
                    </h3>
                    {genre.description && (
                      <p className="text-gray-600 text-sm">
                        {genre.description}
                      </p>
                    )}
                  </div>
                ))}
              </div>
            )}
          </section>
        )}
      </main>
    </div>
  );
}
