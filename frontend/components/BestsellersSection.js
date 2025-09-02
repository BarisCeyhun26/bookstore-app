'use client';

import { useEffect, useState } from 'react';
import BookCard from './BookCard';
import { useBookStore } from '../lib/store';

export default function BestsellersSection() {
  const { getBestsellers } = useBookStore();
  const [bestsellers, setBestsellers] = useState([]);

  useEffect(() => {
    const fetchBestsellers = async () => {
      const books = await getBestsellers(5);
      setBestsellers(books);
    };
    fetchBestsellers();
  }, [getBestsellers]);

  if (!bestsellers || bestsellers.length === 0) {
    return null;
  }

  return (
    <section className="mb-16">
      <h2 className="text-2xl font-bold text-gray-900 mb-6">Bestsellers</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
        {bestsellers.map((book) => (
          <BookCard key={book.bookId} book={book} />
        ))}
      </div>
    </section>
  );
}
