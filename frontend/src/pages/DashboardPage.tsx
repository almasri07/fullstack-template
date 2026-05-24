import { useAuthStore } from '../store/auth.store'
import { useNavigate } from 'react-router-dom'

export function DashboardPage() {
  const { user, logout } = useAuthStore()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div style={{ maxWidth: 800, margin: '40px auto', padding: '0 1rem' }}>
      <h1>Dashboard</h1>
      <p>Welcome, {user?.firstName ?? user?.email}!</p>
      <p>Role: {user?.role}</p>
      <button onClick={handleLogout}>Logout</button>
    </div>
  )
}
