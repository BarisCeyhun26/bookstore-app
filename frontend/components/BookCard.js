'use client';

import { useBookStore } from '../lib/store';
import { ShoppingCartIcon } from '@heroicons/react/24/outline';
import Link from 'next/link';

export default function BookCard({ book }) {
  const { addToCart } = useBookStore();

  const handleAddToCart = (e) => {
    e.preventDefault();
    addToCart(book);
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  const getFormatBadgeColor = (format) => {
    switch (format?.toLowerCase()) {
      case 'physical':
        return 'bg-blue-100 text-blue-800';
      case 'e-book':
        return 'bg-green-100 text-green-800';
      case 'audiobook':
        return 'bg-purple-100 text-purple-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
      {/* Book Cover */}
      <div className="aspect-[3/4] bg-gray-200 flex items-center justify-center">
        {book.coverImageUrl ? (
          <img
            src={book.coverImageUrl}
            alt={book.title}
            className="w-full h-full object-cover"
          />
        ) : (
          <div className="text-gray-400 text-center p-4">
            <div className="text-4xl mb-2">📚</div>
            <p className="text-sm">No Cover</p>
          </div>
        )}
      </div>

      {/* Book Info */}
      <div className="p-4">
        {/* Format Badge */}
        <div className="mb-2">
          <span className={`inline-block px-2 py-1 text-xs font-medium rounded-full ${getFormatBadgeColor(book.format)}`}>
            {book.format}
          </span>
        </div>

        {/* Title */}
        <Link href={`/books/${book.book_id}`}>
          <h3 className="text-lg font-semibold text-gray-900 mb-2 hover:text-primary-600 transition-colors">
            {book.title}
          </h3>
        </Link>

        {/* Author */}
        <p className="text-sm text-gray-600 mb-2">
          by {book.author_name}
        </p>

        {/* Genre */}
        {book.genre_name && (
          <p className="text-xs text-gray-500 mb-3">
            {book.genre_name}
          </p>
        )}

        {/* Price and Add to Cart */}
        <div className="flex items-center justify-between">
          <span className="text-lg font-bold text-gray-900">
            {formatPrice(book.price)}
          </span>
          
          <button
            onClick={handleAddToCart}
            className="flex items-center space-x-1 bg-primary-600 text-white px-3 py-2 rounded-md hover:bg-primary-700 transition-colors"
          >
            <ShoppingCartIcon className="h-4 w-4" />
            <span className="text-sm">Add</span>
          </button>
        </div>
      </div>
    </div>
  );
}
