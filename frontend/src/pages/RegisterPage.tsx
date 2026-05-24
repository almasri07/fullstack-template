import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuthStore } from '../store/auth.store'

export function RegisterPage() {
  const navigate = useNavigate()
  const register = useAuthStore((s) => s.register)
  const [form, setForm] = useState({ email: '', password: '', firstName: '', lastName: '' })
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      await register(form.email, form.password, form.firstName, form.lastName)
      navigate('/dashboard')
    } catch {
      setError('Registration failed. Email may already be in use.')
    } finally {
      setLoading(false)
    }
  }

  const update = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm((f) => ({ ...f, [field]: e.target.value }))

  return (
    <div style={{ maxWidth: 400, margin: '80px auto', padding: '0 1rem' }}>
      <h1>Create account</h1>
      <form onSubmit={handleSubmit}>
        <input placeholder="First name" value={form.firstName} onChange={update('firstName')}
          style={{ display: 'block', width: '100%', marginBottom: '0.75rem' }} />
        <input placeholder="Last name" value={form.lastName} onChange={update('lastName')}
          style={{ display: 'block', width: '100%', marginBottom: '0.75rem' }} />
        <input type="email" placeholder="Email" value={form.email} onChange={update('email')} required
          style={{ display: 'block', width: '100%', marginBottom: '0.75rem' }} />
        <input type="password" placeholder="Password (min 8 chars)" value={form.password}
          onChange={update('password')} required minLength={8}
          style={{ display: 'block', width: '100%', marginBottom: '1rem' }} />
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? 'Creating account...' : 'Register'}
        </button>
      </form>
      <p>Already have an account? <Link to="/login">Sign in</Link></p>
    </div>
  )
}
