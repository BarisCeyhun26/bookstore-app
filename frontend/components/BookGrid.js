'use client';

import BookCard from './BookCard';

export default function BookGrid({ books }) {
  if (!books || books.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500 text-lg">No books found.</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
      {books.map((book) => (
        <BookCard key={book.book_id} book={book} />
      ))}
    </div>
  );
}
