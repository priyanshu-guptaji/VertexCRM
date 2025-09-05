import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from './ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Checkbox } from './ui/checkbox';
import { BarChart3 } from 'lucide-react';

const LoginPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    rememberMe: false
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Add authentication logic here
    console.log('Login attempt:', formData);
    // Simulate successful login
    setTimeout(() => {
      console.log('Login successful, redirecting to dashboard...');
      navigate('/dashboard');
    }, 800);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100">
      <Card className="w-full max-w-md border-0 shadow-xl">
        <CardHeader>
          <div className="flex items-center justify-center mb-2">
            <div className="w-10 h-10 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg flex items-center justify-center">
              <BarChart3 className="w-6 h-6 text-white" />
            </div>
          </div>
          <CardTitle className="text-center text-2xl">Sign in to VertexCRM</CardTitle>
          <CardDescription className="text-center">Enter your email and password to access your account.</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <Label htmlFor="email" className="block text-left font-medium mb-1">Email</Label>
              <Input
                id="email"
                type="email"
                autoComplete="email"
                required
                value={formData.email}
                onChange={e => setFormData({ ...formData, email: e.target.value })}
                className="mt-1"
              />
            </div>
            <div>
              <Label htmlFor="password" className="block text-left font-medium mb-1">Password</Label>
              <Input
                id="password"
                type="password"
                autoComplete="current-password"
                required
                value={formData.password}
                onChange={e => setFormData({ ...formData, password: e.target.value })}
                className="mt-1"
              />
            </div>
            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <Checkbox
                  id="rememberMe"
                  checked={formData.rememberMe}
                  onCheckedChange={checked => setFormData({ ...formData, rememberMe: checked })}
                />
                <Label htmlFor="rememberMe" className="ml-2">Remember me</Label>
              </div>
              <Link to="#" className="text-blue-600 hover:text-blue-700 text-sm font-medium">Forgot password?</Link>
            </div>
            <Button type="submit" className="w-full">Sign In</Button>
          </form>
          <div className="mt-6 text-center">
            <p className="text-gray-600 text-sm">
              Don't have an account?{' '}
              <Link to="/signup" className="text-blue-600 hover:text-blue-700 font-semibold transition-colors">
                Sign up for free
              </Link>
            </p>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default LoginPage;
