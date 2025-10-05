import axios from 'axios';

export const API_URL = (import.meta.env?.VITE_API_URL || 'http://localhost:8090/api').replace(/\/$/, '');

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
  timeout: 10000, // 10 second timeout
});

// Request interceptor to add auth token and logging
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // Log request details for debugging
    console.log('API Request:', {
      method: config.method?.toUpperCase(),
      url: config.url,
      baseURL: config.baseURL,
      fullURL: `${config.baseURL}${config.url}`,
      headers: config.headers,
      data: config.data
    });
    
    return config;
  },
  (error) => {
    console.error('Request interceptor error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors and logging
api.interceptors.response.use(
  (response) => {
    // Log successful responses for debugging
    console.log('API Response Success:', {
      status: response.status,
      statusText: response.statusText,
      url: response.config.url,
      data: response.data
    });
    return response;
  },
  (error) => {
    // Log error responses for debugging with backend message
    const backendMessage = typeof error.response?.data === 'string'
      ? error.response.data
      : (error.response?.data?.message || error.response?.data?.error || (error.response?.data ? JSON.stringify(error.response.data) : error.message));

    console.error('API Response Error:', {
      status: error.response?.status,
      statusText: error.response?.statusText,
      url: error.config?.url,
      data: backendMessage,
      message: error.message
    });
    
    // Normalize a user-facing message to avoid passing objects into React nodes
    error.userMessage = backendMessage;

    // Avoid components accidentally rendering an object by ensuring data is a string when it's an error payload
    if (error.response && typeof error.response.data !== 'string') {
      error.response.data = backendMessage;
    }

    if (!error.response) {
      // Network or CORS error
      error.userMessage = 'Backend is not reachable. Please check if the server is running.';
    }

    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      // Only redirect if not already on login page
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
