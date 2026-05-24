import { create } from 'zustand'
import type { User } from '../types'
import { authService } from '../services/auth.service'

interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  register: (email: string, password: string, firstName?: string, lastName?: string) => Promise<void>
  logout: () => void
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  token: localStorage.getItem('token'),
  isAuthenticated: !!localStorage.getItem('token'),

  login: async (email, password) => {
    const response = await authService.login({ email, password })
    localStorage.setItem('token', response.token)
    set({
      token: response.token,
      isAuthenticated: true,
      user: {
        email: response.email,
        firstName: response.firstName,
        lastName: response.lastName,
        role: response.role,
      },
    })
  },

  register: async (email, password, firstName, lastName) => {
    const response = await authService.register({ email, password, firstName, lastName })
    localStorage.setItem('token', response.token)
    set({
      token: response.token,
      isAuthenticated: true,
      user: {
        email: response.email,
        firstName: response.firstName,
        lastName: response.lastName,
        role: response.role,
      },
    })
  },

  logout: () => {
    localStorage.removeItem('token')
    set({ user: null, token: null, isAuthenticated: false })
  },
}))
