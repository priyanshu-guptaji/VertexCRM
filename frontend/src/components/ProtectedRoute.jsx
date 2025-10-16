import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import axios from 'axios';

const ProtectedRoute = ({ children, resource, action }) => {
  const [hasPermission, setHasPermission] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkPermission = async () => {
      // If no specific resource/action is required, just check if user is authenticated
      if (!resource || !action) {
        const token = localStorage.getItem('token');
        if (token) {
          setHasPermission(true);
        } else {
          setHasPermission(false);
        }
        setLoading(false);
        return;
      }

      try {
        const token = localStorage.getItem('token');
        if (!token) {
          setHasPermission(false);
          setLoading(false);
          return;
        }

        const response = await axios.post('/api/rbac/check-permission', 
          { resource, action },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        
        setHasPermission(response.data.hasPermission);
      } catch (err) {
        console.error('Permission check failed', err);
        setHasPermission(false);
      } finally {
        setLoading(false);
      }
    };

    checkPermission();
  }, [resource, action]);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (!hasPermission) {
    return <Navigate to="/unauthorized" />;
  }

  return children;
};

export default ProtectedRoute;
