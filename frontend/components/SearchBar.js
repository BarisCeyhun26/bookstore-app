'use client';

import { useState, useEffect, useRef } from 'react';
import { useRouter } from 'next/router';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import { useBookStore } from '../lib/store';

export default function SearchBar({ onSearch, initialValue = '' }) {
  console.log('SearchBar component render edildi');
  const [query, setQuery] = useState(initialValue);
  const { searchBooks } = useBookStore();
  const router = useRouter();
  const inputRef = useRef(null);

  // Initial value değiştiğinde query'yi güncelle
  useEffect(() => {
    setQuery(initialValue);
  }, [initialValue]);

  // Input color'ını zorla ayarla - setTimeout ile gecikme
  useEffect(() => {
    console.log('useEffect çalıştı - Component mount');
    const setInputColor = () => {
      console.log('setInputColor çalıştı, inputRef.current:', inputRef.current);
      if (inputRef.current) {
        inputRef.current.style.color = '#000000';
        inputRef.current.style.setProperty('color', '#000000', 'important');
        inputRef.current.style.setProperty('-webkit-text-fill-color', '#000000', 'important');
        console.log('Input color set to black');
      } else {
        console.log('Input ref not found, retrying...');
        setTimeout(setInputColor, 100);
      }
    };
    
    setTimeout(setInputColor, 100);
  }, []);

  // Input değiştiğinde de color'ı ayarla
  useEffect(() => {
    console.log('useEffect çalıştı - Query değişti:', query);
    const setInputColor = () => {
      console.log('setInputColor çalıştı (query), inputRef.current:', inputRef.current);
      if (inputRef.current) {
        inputRef.current.style.color = '#000000';
        inputRef.current.style.setProperty('color', '#000000', 'important');
        inputRef.current.style.setProperty('-webkit-text-fill-color', '#000000', 'important');
        console.log('Input color updated to black');
      }
    };
    
    setTimeout(setInputColor, 50);
  }, [query]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (query.trim()) {
      if (onSearch) {
        // Custom search handler varsa onu kullan
        onSearch(query);
      } else {
        // Arama sonuçları sayfasına yönlendir
        router.push(`/search?q=${encodeURIComponent(query)}`);
      }
    }
  };

  const handleInputChange = (e) => {
    setQuery(e.target.value);
  };

  return (
    <div style={{ maxWidth: '28rem', margin: '0 auto' }}>
      <form onSubmit={handleSubmit}>
        <div style={{ position: 'relative' }}>
          <div style={{ 
            position: 'absolute', 
            top: '50%', 
            left: '12px', 
            transform: 'translateY(-50%)',
            pointerEvents: 'none'
          }}>
            <MagnifyingGlassIcon style={{ width: '20px', height: '20px', color: '#9ca3af' }} />
          </div>
          <input
            ref={inputRef}
            id="search-input"
            type="text"
            value={query}
            onChange={handleInputChange}
            placeholder="Search for books, authors, or genres..."
            style={{ 
              width: '100%',
              paddingLeft: '40px',
              paddingRight: '80px',
              paddingTop: '12px',
              paddingBottom: '12px',
              border: '1px solid #d1d5db',
              borderRadius: '6px',
              fontSize: '16px',
              fontWeight: 'normal',
              outline: 'none',
              color: '#000000',
              backgroundColor: '#ffffff',
              fontFamily: 'ui-sans-serif, system-ui, sans-serif'
            }}
            autoComplete="off"
          />
          <button
            type="submit"
            style={{
              position: 'absolute',
              top: '0',
              right: '0',
              height: '100%',
              padding: '0 12px',
              backgroundColor: '#2563eb',
              color: '#ffffff',
              border: 'none',
              borderTopRightRadius: '6px',
              borderBottomRightRadius: '6px',
              cursor: 'pointer',
              fontFamily: 'ui-sans-serif, system-ui, sans-serif'
            }}
          >
            Search
          </button>
        </div>
      </form>
    </div>
  );
}