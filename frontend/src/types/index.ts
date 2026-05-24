// Auth types
export interface User {
  email: string
  firstName: string | null
  lastName: string | null
  role: 'USER' | 'ADMIN'
}

export interface AuthResponse {
  token: string
  email: string
  firstName: string | null
  lastName: string | null
  role: 'USER' | 'ADMIN'
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  email: string
  password: string
  firstName?: string
  lastName?: string
}

// API error type
export interface ApiError {
  message: string
  status: number
  timestamp: string
}
