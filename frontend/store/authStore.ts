import { create } from 'zustand';
import { User } from '@/types';
import { authService } from '@/services/authService';

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  setUser: (user: User | null) => void;
  setAuthenticated: (value: boolean) => void;
  setLoading: (value: boolean) => void;
  logout: () => Promise<void>;
  initialize: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,
  isLoading: true,

  setUser: (user) => set({ user }),
  setAuthenticated: (value) => set({ isAuthenticated: value }),
  setLoading: (value) => set({ isLoading: value }),

  logout: async () => {
    await authService.logout();
    set({ user: null, isAuthenticated: false });
  },

  initialize: async () => {
    set({ isLoading: true });
    try {
      const loggedIn = await authService.isLoggedIn();
      if (loggedIn) {
        const user = await authService.getCurrentUser();
        set({ user, isAuthenticated: true });
      }
    } catch {
      // token expired or invalid — clear silently
      await authService.logout();
    } finally {
      set({ isLoading: false });
    }
  },
}));
