import { useEffect } from 'react';
import Header from '../components/Header';
import { useBookStore } from '../lib/store';

export default function AuthorsPage() {
  const { authors, loading, error, fetchAuthors } = useBookStore();

  useEffect(() => {
    if (authors.length === 0) {
      fetchAuthors();
    }
  }, [authors.length, fetchAuthors]);

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Page Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-4">Authors</h1>
          <p className="text-gray-600">Discover amazing authors and their works</p>
        </div>

        {/* Authors Grid */}
        {loading && authors.length === 0 && (
          <div className="flex justify-center items-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
          </div>
        )}
        
        {error && (
          <div className="text-center py-12">
            <p className="text-red-600">Error: {error}</p>
          </div>
        )}
        
        {!error && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {authors.length > 0 ? (
              authors.map((author) => (
                <div key={author.id} className="bg-white rounded-lg shadow-md p-6">
                  <div className="flex items-center space-x-4">
                    <div className="flex-shrink-0">
                      <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center">
                        <span className="text-2xl font-bold text-primary-600">
                          {author.firstName?.[0]}{author.lastName?.[0]}
                        </span>
                      </div>
                    </div>
                    <div className="flex-1 min-w-0">
                      <h3 className="text-lg font-semibold text-gray-900">
                        {author.firstName} {author.lastName}
                      </h3>
                      {author.biography && (
                        <p className="text-sm text-gray-600 mt-1 line-clamp-2">
                          {author.biography}
                        </p>
                      )}
                      {author.birthDate && (
                        <p className="text-xs text-gray-500 mt-2">
                          Born: {new Date(author.birthDate).toLocaleDateString()}
                        </p>
                      )}
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="col-span-full text-center py-12">
                <p className="text-gray-500 text-lg">No authors found.</p>
              </div>
            )}
          </div>
        )}
      </main>
    </div>
  );
}
