import { useState, useEffect } from 'react';
import { useRouter } from 'next/router';
import Head from 'next/head';
import { MagnifyingGlassIcon, XMarkIcon } from '@heroicons/react/24/outline';
import { useBookStore } from '../lib/store';
import BookGrid from '../components/BookGrid';
import SearchBar from '../components/SearchBar';

export default function SearchPage() {
  const router = useRouter();
  const { q } = router.query;
  const { books, loading, error, searchBooks, clearError } = useBookStore();
  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    if (q) {
      setSearchQuery(q);
      searchBooks(q);
    }
  }, [q, searchBooks]);

  const handleSearch = (query) => {
    if (query.trim()) {
      router.push(`/search?q=${encodeURIComponent(query)}`);
    }
  };

  const clearSearch = () => {
    setSearchQuery('');
    router.push('/search');
  };

  return (
    <>
      <Head>
        <title>
          {searchQuery ? `Search results for "${searchQuery}"` : 'Search Books'} - Bookstore
        </title>
        <meta name="description" content="Search for books, authors, and genres in our bookstore" />
      </Head>

      <div className="min-h-screen bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {/* Search Header */}
          <div className="text-center mb-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-4">
              {searchQuery ? `Search Results` : 'Search Books'}
            </h1>
            
            {/* Search Bar */}
            <div className="max-w-2xl mx-auto mb-6">
              <SearchBar 
                initialValue={searchQuery}
                onSearch={handleSearch}
              />
            </div>

            {/* Search Query Display */}
            {searchQuery && (
              <div className="flex items-center justify-center gap-2 text-lg text-gray-600">
                <span>Results for:</span>
                <span className="font-semibold text-primary-600">"{searchQuery}"</span>
                <button
                  onClick={clearSearch}
                  className="ml-2 p-1 text-gray-400 hover:text-gray-600 transition-colors"
                  title="Clear search"
                >
                  <XMarkIcon className="h-5 w-5" />
                </button>
              </div>
            )}
          </div>

          {/* Loading State */}
          {loading && (
            <div className="flex items-center justify-center py-12">
              <div className="flex items-center space-x-2 text-gray-600">
                <MagnifyingGlassIcon className="h-6 w-6 animate-pulse" />
                <span>Searching books...</span>
              </div>
            </div>
          )}

          {/* Error State */}
          {error && (
            <div className="text-center py-12">
              <div className="bg-red-50 border border-red-200 rounded-lg p-6 max-w-md mx-auto">
                <div className="text-red-600 font-medium mb-2">Search Error</div>
                <div className="text-red-500 text-sm mb-4">{error}</div>
                <button
                  onClick={clearError}
                  className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition-colors"
                >
                  Try Again
                </button>
              </div>
            </div>
          )}

          {/* No Results */}
          {!loading && !error && searchQuery && books.length === 0 && (
            <div className="text-center py-12">
              <MagnifyingGlassIcon className="h-16 w-16 text-gray-300 mx-auto mb-4" />
              <h3 className="text-xl font-medium text-gray-900 mb-2">No books found</h3>
              <p className="text-gray-600 mb-6">
                We couldn't find any books matching "{searchQuery}". Try different keywords or check your spelling.
              </p>
              <div className="space-y-2 text-sm text-gray-500">
                <p>Search tips:</p>
                <ul className="list-disc list-inside space-y-1">
                  <li>Try searching by book title, author name, or genre</li>
                  <li>Use fewer or different keywords</li>
                  <li>Check your spelling</li>
                </ul>
              </div>
            </div>
          )}

          {/* Search Results */}
          {!loading && !error && books.length > 0 && (
            <div>
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-xl font-semibold text-gray-900">
                  {books.length} book{books.length !== 1 ? 's' : ''} found
                </h2>
              </div>
              <BookGrid books={books} />
            </div>
          )}

          {/* Initial State - No Search Query */}
          {!loading && !error && !searchQuery && (
            <div className="text-center py-12">
              <MagnifyingGlassIcon className="h-16 w-16 text-gray-300 mx-auto mb-4" />
              <h3 className="text-xl font-medium text-gray-900 mb-2">Start your search</h3>
              <p className="text-gray-600 mb-6">
                Enter a book title, author name, or genre to find what you're looking for.
              </p>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-3xl mx-auto text-sm text-gray-500">
                <div className="bg-white p-4 rounded-lg shadow-sm">
                  <h4 className="font-medium text-gray-900 mb-2">Search by Title</h4>
                  <p>Find books by their exact or partial title</p>
                </div>
                <div className="bg-white p-4 rounded-lg shadow-sm">
                  <h4 className="font-medium text-gray-900 mb-2">Search by Author</h4>
                  <p>Discover books by your favorite authors</p>
                </div>
                <div className="bg-white p-4 rounded-lg shadow-sm">
                  <h4 className="font-medium text-gray-900 mb-2">Search by Genre</h4>
                  <p>Explore books in specific categories</p>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </>
  );
}
