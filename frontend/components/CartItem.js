'use client';

import { useBookStore } from '../lib/store';
import { MinusIcon, PlusIcon, TrashIcon } from '@heroicons/react/24/outline';
import Link from 'next/link';

export default function CartItem({ item }) {
  const { updateCartQuantity, removeFromCart } = useBookStore();

  const handleQuantityChange = (newQuantity) => {
    if (newQuantity < 1) {
      removeFromCart(item.id);
    } else {
      updateCartQuantity(item.id, newQuantity);
    }
  };

  const handleRemove = () => {
    removeFromCart(item.id);
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
    <div className="p-6">
      <div className="flex items-start space-x-4">
        {/* Book Cover */}
        <div className="flex-shrink-0">
          <div className="w-20 h-28 bg-gray-200 rounded-md overflow-hidden">
            {item.coverImageUrl ? (
              <img
                src={item.coverImageUrl}
                alt={item.title}
                className="w-full h-full object-cover"
              />
            ) : (
              <div className="w-full h-full flex items-center justify-center text-gray-400">
                <div className="text-center">
                  <div className="text-2xl mb-1">📚</div>
                  <p className="text-xs">No Cover</p>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Book Details */}
        <div className="flex-1 min-w-0">
          <div className="flex items-start justify-between">
            <div className="flex-1">
              {/* Format Badge */}
              <div className="mb-2">
                <span className={`inline-block px-2 py-1 text-xs font-medium rounded-full ${getFormatBadgeColor(item.format)}`}>
                  {item.format}
                </span>
              </div>

              {/* Title */}
              <Link href={`/books/${item.bookId}`}>
                <h3 className="text-lg font-semibold text-gray-900 hover:text-primary-600 transition-colors line-clamp-2">
                  {item.title}
                </h3>
              </Link>

              {/* Author */}
              <p className="text-sm text-gray-600 mt-1">
                by {item.authorName}
              </p>

              {/* Genre */}
              {item.genreName && (
                <p className="text-sm text-gray-500 mt-1">
                  {item.genreName}
                </p>
              )}

              {/* Price */}
              <p className="text-lg font-bold text-gray-900 mt-2">
                {formatPrice(item.price)}
              </p>
            </div>

            {/* Remove Button */}
            <button
              onClick={handleRemove}
              className="flex-shrink-0 p-2 text-gray-400 hover:text-red-600 transition-colors"
              title="Remove from cart"
            >
              <TrashIcon className="h-5 w-5" />
            </button>
          </div>

          {/* Quantity Controls */}
          <div className="flex items-center justify-between mt-4">
            <div className="flex items-center space-x-3">
              <span className="text-sm font-medium text-gray-700">Quantity:</span>
              
              <div className="flex items-center border border-gray-300 rounded-md">
                <button
                  onClick={() => handleQuantityChange(item.quantity - 1)}
                  className="p-2 hover:bg-gray-100 transition-colors"
                  disabled={item.quantity <= 1}
                >
                  <MinusIcon className="h-4 w-4" />
                </button>
                
                <span className="px-4 py-2 text-sm font-medium text-gray-900 min-w-[3rem] text-center">
                  {item.quantity}
                </span>
                
                <button
                  onClick={() => handleQuantityChange(item.quantity + 1)}
                  className="p-2 hover:bg-gray-100 transition-colors"
                >
                  <PlusIcon className="h-4 w-4" />
                </button>
              </div>
            </div>

            {/* Item Total */}
            <div className="text-right">
              <p className="text-sm text-gray-600">Item Total</p>
              <p className="text-lg font-bold text-gray-900">
                {formatPrice(item.price * item.quantity)}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
