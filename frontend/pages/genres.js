import { useEffect } from 'react';
import Header from '../components/Header';
import { useBookStore } from '../lib/store';

export default function GenresPage() {
  const { genres, loading, error, fetchGenres } = useBookStore();

  useEffect(() => {
    if (genres.length === 0) {
      fetchGenres();
    }
  }, [genres.length, fetchGenres]);

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Page Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-4">Genres</h1>
          <p className="text-gray-600">Explore books by genre</p>
        </div>

        {/* Genres Grid */}
        {loading && genres.length === 0 && (
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
            {genres.length > 0 ? (
              genres.map((genre) => (
                <div key={genre.id} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
                  <div className="text-center">
                    <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                      <span className="text-2xl font-bold text-primary-600">
                        {genre.name?.[0]}
                      </span>
                    </div>
                    <h3 className="text-xl font-semibold text-gray-900 mb-2">
                      {genre.name}
                    </h3>
                    {genre.description && (
                      <p className="text-gray-600 text-sm">
                        {genre.description}
                      </p>
                    )}
                  </div>
                </div>
              ))
            ) : (
              <div className="col-span-full text-center py-12">
                <p className="text-gray-500 text-lg">No genres found.</p>
              </div>
            )}
          </div>
        )}
      </main>
    </div>
  );
}
