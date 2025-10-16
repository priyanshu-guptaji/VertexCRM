import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext.jsx';

function CustomerLogin() {
  const { loginCustomer } = useAuth();
  const [form, setForm] = useState({ email: '', password: '' });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const validate = () => {
    const next = {};
    if (!form.email.trim()) next.email = 'Email is required';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email.trim())) next.email = 'Invalid email';
    if (!form.password) next.password = 'Password is required';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const onChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors(prev => ({ ...prev, [name]: '' }));
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setMessage('');
    if (!validate()) return;
    setLoading(true);
    try {
      console.log('Submitting customer login:', { email: form.email.trim(), password: '***' });
      const res = await loginCustomer(form.email, form.password);
      if (res.success) {
        window.location.href = '/customer/dashboard';
      } else {
        setMessage(res.error || 'Login failed');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto p-6 bg-white rounded-xl border border-secondary-200 shadow-sm">
      <h1 className="text-2xl font-semibold mb-4">Customer Login</h1>
      {message && (
        <div className="mb-3 text-sm p-3 rounded bg-danger-50 border border-danger-200 text-danger-700">{message}</div>
      )}
      <form onSubmit={onSubmit} className="space-y-4">
        <div>
          <label className="block text-sm mb-1" htmlFor="email">Email</label>
          <input id="email" name="email" type="email" className="input w-full" value={form.email} onChange={onChange} required />
          {errors.email && <p className="text-xs text-red-600 mt-1">{errors.email}</p>}
        </div>
        <div>
          <label className="block text-sm mb-1" htmlFor="password">Password</label>
          <input id="password" name="password" type="password" className="input w-full" value={form.password} onChange={onChange} required />
          {errors.password && <p className="text-xs text-red-600 mt-1">{errors.password}</p>}
        </div>
        <button type="submit" className="btn btn-primary w-full" disabled={loading}>
          {loading ? 'Signing in...' : 'Sign in'}
        </button>
      </form>
    </div>
  );
}

export default CustomerLogin;

