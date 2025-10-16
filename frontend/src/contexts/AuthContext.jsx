import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api.js';

const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      // Set token in API headers
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      
<<<<<<< HEAD
      // Get user info from token (you might want to decode JWT or make an API call)
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
      setUser(userInfo);
=======
      // Get user info and normalize role casing
      const stored = JSON.parse(localStorage.getItem('userInfo') || '{}');
      const roleRaw = stored && stored.role ? String(stored.role).toUpperCase() : undefined;
      // Canonicalize to match backend @PreAuthorize and frontend routes
      const roleCanonical = roleRaw
        ? (roleRaw.startsWith('ADMIN') ? 'ADMIN'
          : roleRaw.startsWith('MANAGER') ? 'MANAGER'
          : roleRaw.includes('SALES') ? 'SALES'
          : roleRaw.startsWith('USER') ? 'USER'
          : roleRaw.replace(/[^A-Z0-9]/g, ''))
        : undefined;
      const normalized = stored ? { ...stored, role: roleCanonical } : stored;
      setUser(normalized);
      // Attach multi-tenant hints to every request if available
      if (normalized && (normalized.orgId || normalized.accountId)) {
        if (normalized.orgId) api.defaults.headers.common['X-Tenant-Id'] = normalized.orgId;
        if (normalized.accountId) api.defaults.headers.common['X-Account-Id'] = normalized.accountId;
      }
      if (normalized !== stored) {
        localStorage.setItem('userInfo', JSON.stringify(normalized));
      }
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    }
    setLoading(false);
  }, []);

<<<<<<< HEAD
  const login = async (email, password) => {
=======
  // Helper to persist auth data consistently (ADMIN or CUSTOMER)
  const persistAuth = (payload = {}) => {
    const {
      token,
      memberId,
      userId,
      email: userEmail,
      name,
      orgId,
      orgName,
      accountId,
      role
    } = payload;
    if (!token) return { success: false, error: 'Invalid response from server - no token received' };
    const roleUpper = String(role || '').toUpperCase();
    const roleNormalized = roleUpper.startsWith('ADMIN') ? 'ADMIN'
      : roleUpper.startsWith('MANAGER') ? 'MANAGER'
      : roleUpper.includes('SALES') ? 'SALES'
      : roleUpper.startsWith('USER') ? 'USER'
      : roleUpper.replace(/[^A-Z0-9]/g, '');
    const nextUser = {
      memberId: memberId ?? userId,
      email: userEmail,
      name,
      orgId,
      orgName,
      accountId: accountId ?? null,
      role: roleNormalized
    };
    localStorage.setItem('token', token);
    localStorage.setItem('userInfo', JSON.stringify(nextUser));
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    if (nextUser.orgId) api.defaults.headers.common['X-Tenant-Id'] = nextUser.orgId;
    if (nextUser.accountId) api.defaults.headers.common['X-Account-Id'] = nextUser.accountId;
    setUser(nextUser);
    return { success: true, role: roleNormalized, user: nextUser, token };
  };

  const login = async (email, password, tenantHint) => {
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    try {
      // Validate input
      if (!email || !password) {
        return { 
          success: false, 
          error: 'Email and password are required' 
        };
      }

      // Validate email format
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        return { 
          success: false, 
          error: 'Please enter a valid email address' 
        };
      }

      // Validate password length
      if (password.length < 6) {
        return { 
          success: false, 
          error: 'Password must be at least 6 characters long' 
        };
      }

      console.log('Attempting login with:', { email: email.trim(), password: '***' });
      
      // Prepare the request payload exactly as expected by Spring Boot
      const loginData = {
        email: email.trim(),
        password: password
      };
      
      console.log('Sending login request to:', api.defaults.baseURL + '/auth/login');
      console.log('Request payload:', { email: loginData.email, password: '***' });
      
<<<<<<< HEAD
      const response = await api.post('/auth/login', loginData);
      
      console.log('Login response status:', response.status);
      console.log('Login response data:', response.data);
      
      const { token, memberId, email: userEmail, name, orgId, orgName, role } = response.data;
      
      if (!token) {
        return { 
          success: false, 
          error: 'Invalid response from server - no token received' 
        };
      }
      
      // Store token and user info
      localStorage.setItem('token', token);
      localStorage.setItem('userInfo', JSON.stringify({
        memberId,
        email: userEmail,
        name,
        orgId,
        orgName,
        role
      }));
      
      // Set token in API headers
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      
      setUser({
        memberId,
        email: userEmail,
        name,
        orgId,
        orgName,
        role
      });
      
      return { success: true };
=======
      const response = await api.post('/auth/login', loginData, {
        headers: tenantHint ? { 'X-Tenant': tenantHint } : {}
      });
      console.log('Login response status:', response.status);
      console.log('Login response data:', response.data);
      return persistAuth(response.data);
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    } catch (error) {
      console.error('Login error details:', {
        message: error.message,
        response: error.response?.data,
        status: error.response?.status,
        config: {
          url: error.config?.url,
          method: error.config?.method,
          data: error.config?.data
        }
      });
      
      let errorMessage = 'Login failed';
      
      if (error.response) {
        // Server responded with error status
        const { status, data } = error.response;
        
        if (status === 400) {
          // Handle validation errors from Spring Boot
          if (data && typeof data === 'string') {
            errorMessage = data;
          } else if (data && data.message) {
            errorMessage = data.message;
          } else if (data && Array.isArray(data)) {
            errorMessage = data.join(', ');
          } else {
            errorMessage = 'Invalid email or password format';
          }
        } else if (status === 401) {
          errorMessage = 'Invalid credentials';
        } else if (status === 500) {
          errorMessage = 'Server error. Please try again later.';
        } else {
          errorMessage = data?.message || `Server error (${status})`;
        }
      } else if (error.request) {
        // Request was made but no response received
        errorMessage = 'Unable to connect to server. Please check your connection.';
      } else {
        // Something else happened
        errorMessage = error.message || 'An unexpected error occurred';
      }
      
      return { 
        success: false, 
        error: errorMessage 
      };
    }
  };

<<<<<<< HEAD
=======
  // Customer login (returns accountId)
  const loginCustomer = async (email, password, tenantHint) => {
    try {
      if (!email || !password) return { success: false, error: 'Email and password are required' };
      const response = await api.post('/auth/customer/login', { email: email.trim(), password }, {
        headers: tenantHint ? { 'X-Tenant': tenantHint } : {}
      });
      console.log('Customer login response:', response.data);
      return persistAuth(response.data);
    } catch (error) {
      const status = error.response?.status;
      const data = error.response?.data;
      const msg = status === 401 ? 'Invalid credentials'
        : status === 403 ? 'Access denied'
        : status === 400 ? (data?.message || 'Invalid login data')
        : status >= 500 ? 'Server error. Please try again later.'
        : (data?.message || 'Login failed');
      return { success: false, error: msg };
    }
  };

>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
  const register = async (orgName, orgEmail, adminName, adminEmail, adminPassword) => {
    try {
      const response = await api.post('/auth/register', {
        orgName,
        orgEmail,
        adminName,
        adminEmail,
        adminPassword
      });
      
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Registration error:', error);
      
      let errorMessage = 'Registration failed';
      
      if (error.response) {
        const { status, data } = error.response;
        
        if (status === 400) {
          errorMessage = data?.message || 'Invalid registration data';
        } else if (status === 409) {
          errorMessage = 'Email or organization already exists';
        } else {
          errorMessage = data?.message || `Registration error (${status})`;
        }
      } else if (error.request) {
        errorMessage = 'Unable to connect to server. Please check your connection.';
      } else {
        errorMessage = error.message || 'An unexpected error occurred';
      }
      
      return { 
        success: false, 
        error: errorMessage 
      };
    }
  };

<<<<<<< HEAD
=======
  // Customer signup
  const registerCustomer = async ({ fullName, email, password, companyName, phone }) => {
    try {
      const response = await api.post('/auth/customer/signup', {
        fullName, email, password, companyName, phone
      });
      return { success: true, data: response.data };
    } catch (error) {
      const status = error.response?.status;
      const data = error.response?.data;
      const msg = status === 409 ? 'Email already exists'
        : status === 400 ? (data?.message || 'Invalid signup data')
        : status >= 500 ? 'Server error. Please try again later.'
        : (data?.message || 'Signup failed');
      return { success: false, error: msg };
    }
  };

>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    delete api.defaults.headers.common['Authorization'];
<<<<<<< HEAD
=======
    delete api.defaults.headers.common['X-Tenant-Id'];
    delete api.defaults.headers.common['X-Account-Id'];
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    setUser(null);
  };

  const value = {
    user,
    login,
<<<<<<< HEAD
    register,
=======
    loginCustomer,
    register,
    registerCustomer,
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    logout,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}
