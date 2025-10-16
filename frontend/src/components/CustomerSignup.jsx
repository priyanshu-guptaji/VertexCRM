import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext.jsx';

function CustomerSignup() {
  const { registerCustomer } = useAuth();
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    companyName: '',
    phone: ''
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const validate = () => {
    const next = {};
    if (!form.fullName.trim()) next.fullName = 'Full name is required';
    if (!form.companyName.trim()) next.companyName = 'Company name is required';
    if (!form.email.trim()) next.email = 'Email is required';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email.trim())) next.email = 'Invalid email';
    if (!form.password) next.password = 'Password is required';
    else if (form.password.length < 6) next.password = 'At least 6 characters';
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
      console.log('Submitting customer signup:', { ...form, password: '***' });
      const res = await registerCustomer(form);
      if (res.success) {
        setMessage('Signup successful. Please login.');
        setTimeout(() => { window.location.href = '/customer/login'; }, 800);
      } else {
        setMessage(res.error || 'Signup failed');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto p-6 bg-white rounded-xl border border-secondary-200 shadow-sm">
      <h1 className="text-2xl font-semibold mb-4">Customer Signup</h1>
      {message && (
        <div className="mb-3 text-sm p-3 rounded bg-secondary-50 border border-secondary-200">{message}</div>
      )}
      <form onSubmit={onSubmit} className="space-y-4">
        <div>
          <label className="block text-sm mb-1" htmlFor="fullName">Full Name</label>
          <input id="fullName" name="fullName" className="input w-full" value={form.fullName} onChange={onChange} required />
          {errors.fullName && <p className="text-xs text-red-600 mt-1">{errors.fullName}</p>}
        </div>
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
        <div>
          <label className="block text-sm mb-1" htmlFor="companyName">Company Name</label>
          <input id="companyName" name="companyName" className="input w-full" value={form.companyName} onChange={onChange} required />
          {errors.companyName && <p className="text-xs text-red-600 mt-1">{errors.companyName}</p>}
        </div>
        <div>
          <label className="block text-sm mb-1" htmlFor="phone">Phone (optional)</label>
          <input id="phone" name="phone" className="input w-full" value={form.phone} onChange={onChange} />
        </div>
        <button type="submit" className="btn btn-primary w-full" disabled={loading}>
          {loading ? 'Creating account...' : 'Create Customer Account'}
        </button>
      </form>
    </div>
  );
}

export default CustomerSignup;

