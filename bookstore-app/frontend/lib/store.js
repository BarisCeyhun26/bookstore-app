import { create } from 'zustand';
import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const useBookStore = create((set, get) => ({
  books: [],
  loading: false,
  error: null,
  cart: [],
  user: null,

  // Fetch books
  fetchBooks: async (params = {}) => {
    set({ loading: true, error: null });
    try {
      const response = await api.get('/api/books', { params });
      set({ books: response.data, loading: false });
    } catch (error) {
      const errorMessage = error.response?.data?.error || error.message || 'Failed to fetch books';
      set({ error: errorMessage, loading: false });
      console.error('Error fetching books:', error);
    }
  },

  // Search books
  searchBooks: async (query) => {
    set({ loading: true, error: null });
    try {
      const response = await api.get(`/api/books/search?q=${encodeURIComponent(query)}`);
      set({ books: response.data, loading: false });
    } catch (error) {
      const errorMessage = error.response?.data?.error || error.message || 'Failed to search books';
      set({ error: errorMessage, loading: false });
      console.error('Error searching books:', error);
    }
  },

  // Get book by ID
  getBookById: async (id) => {
    set({ loading: true, error: null });
    try {
      const response = await api.get(`/api/books/${id}`);
      set({ loading: false });
      return response.data;
    } catch (error) {
      const errorMessage = error.response?.data?.error || error.message || 'Failed to fetch book';
      set({ error: errorMessage, loading: false });
      console.error('Error fetching book:', error);
      return null;
    }
  },

  // Get bestsellers
  getBestsellers: async (limit = 5) => {
    set({ loading: true, error: null });
    try {
      const response = await api.get(`/api/books/bestsellers?limit=${limit}`);
      set({ loading: false });
      return response.data;
    } catch (error) {
      const errorMessage = error.response?.data?.error || error.message || 'Failed to fetch bestsellers';
      set({ error: errorMessage, loading: false });
      console.error('Error fetching bestsellers:', error);
      return [];
    }
  },

  // Cart operations
  addToCart: (book, quantity = 1) => {
    const { cart } = get();
    const existingItem = cart.find(item => item.id === book.id);
    
    if (existingItem) {
      set({
        cart: cart.map(item =>
          item.id === book.id
            ? { ...item, quantity: item.quantity + quantity }
            : item
        )
      });
    } else {
      set({
        cart: [...cart, { ...book, quantity }]
      });
    }
  },

  removeFromCart: (bookId) => {
    const { cart } = get();
    set({
      cart: cart.filter(item => item.id !== bookId)
    });
  },

  updateCartQuantity: (bookId, quantity) => {
    const { cart } = get();
    if (quantity <= 0) {
      get().removeFromCart(bookId);
    } else {
      set({
        cart: cart.map(item =>
          item.id === bookId
            ? { ...item, quantity }
            : item
        )
      });
    }
  },

  clearCart: () => {
    set({ cart: [] });
  },

  getCartTotal: () => {
    const { cart } = get();
    return cart.reduce((total, item) => total + (item.price * item.quantity), 0);
  },

  getCartItemCount: () => {
    const { cart } = get();
    return cart.reduce((total, item) => total + item.quantity, 0);
  },

  // User operations
  setUser: (user) => {
    set({ user });
  },

  logout: () => {
    set({ user: null, cart: [] });
  },

  // Clear error
  clearError: () => {
    set({ error: null });
  },
}));
