'use client';

import { useState } from 'react';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import { useBookStore } from '../lib/store';

export default function SearchBar() {
  const [query, setQuery] = useState('');
  const { searchBooks } = useBookStore();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (query.trim()) {
      searchBooks(query);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="max-w-md mx-auto">
      <div className="relative">
        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <MagnifyingGlassIcon className="h-5 w-5 text-gray-400" />
        </div>
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search for books, authors, or genres..."
          className="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
        />
        <button
          type="submit"
          className="absolute inset-y-0 right-0 px-3 flex items-center bg-primary-600 text-white rounded-r-md hover:bg-primary-700 transition-colors"
        >
          Search
        </button>
      </div>
    </form>
  );
}
