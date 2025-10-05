import React from 'react';
import { createBrowserRouter, RouterProvider, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider, useAuth } from './contexts/AuthContext.jsx';
import Login from './components/Login.jsx';
import Register from './components/Register.jsx';
import Dashboard from './components/Dashboard.jsx';
import ManagerDashboard from './components/Dashboard.jsx';
import SalesDashboard from './components/Dashboard.jsx';
import CustomerDashboard from './components/Dashboard.jsx';
import Leads from './components/Leads.jsx';
import Contacts from './components/Contacts.jsx';
import Accounts from './components/Accounts.jsx';
import Deals from './components/Deals.jsx';
import Activities from './components/Activities.jsx';
import Members from './components/Members.jsx';
import Organizations from './components/Organizations.jsx';
import Layout from './components/Layout.jsx';
import CRMLandingPage from './components/CRMLandingPage.jsx';

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  
  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="spinner spinner-lg" />
      </div>
    );
  }
  
  return user ? children : <Navigate to="/home" />;
}

function RoleRoute({ children, allowedRoles = [] }) {
  const { user, loading } = useAuth();
  
  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="spinner spinner-lg" />
      </div>
    );
  }
  
  if (!user) {
    return <Navigate to="/home" />;
  }
  
  if (allowedRoles.length > 0 && !allowedRoles.includes(user.role)) {
    return <Navigate to="/" />;
  }
  
  return children;
}

function RedirectToRoleHome() {
  const { user } = useAuth();
  if (!user) return <Navigate to="/home" />;
  switch (user.role) {
    case 'Admin':
      return <Navigate to="/admin" />;
    case 'Manager':
      return <Navigate to="/manager" />;
    case 'Sales':
      return <Navigate to="/sales" />;
    case 'Customer':
      return <Navigate to="/customer" />;
    default:
      return <Navigate to="/home" />;
  }
}

// Create router with future flags to suppress warnings
const ErrorElement = ({ error }) => {
  const message = (error && (error.message || String(error))) || 'Unexpected error';
  return (
    <div className="p-6">
      <h2 className="text-xl font-semibold mb-2">Something went wrong</h2>
      <pre className="text-sm bg-secondary-100 p-3 rounded">{message}</pre>
    </div>
  );
};

const router = createBrowserRouter([
  {
    path: "/home",
    element: <CRMLandingPage />,
    errorElement: <ErrorElement />
  },
  {
    path: "/login",
    element: <Login />,
    errorElement: <ErrorElement />
  },
  {
    path: "/register", 
    element: <Register />,
    errorElement: <ErrorElement />
  },
  {
    path: "/",
    element: (
      <ProtectedRoute>
        <RedirectToRoleHome />
      </ProtectedRoute>
    ),
    errorElement: <ErrorElement />
  },
  {
    path: "/admin",
    element: (
      <RoleRoute allowedRoles={["Admin"]}>
        <Layout>
          <Dashboard />
        </Layout>
      </RoleRoute>
    )
  },
  {
    path: "/manager",
    element: (
      <RoleRoute allowedRoles={["Manager","Admin"]}>
        <Layout>
          <ManagerDashboard />
        </Layout>
      </RoleRoute>
    )
  },
  {
    path: "/sales",
    element: (
      <RoleRoute allowedRoles={["Sales","Manager","Admin"]}>
        <Layout>
          <SalesDashboard />
        </Layout>
      </RoleRoute>
    )
  },
  {
    path: "/customer",
    element: (
      <RoleRoute allowedRoles={["Customer"]}>
        <Layout>
          <CustomerDashboard />
        </Layout>
      </RoleRoute>
    )
  },
  {
    path: "/leads",
    element: (
      <ProtectedRoute>
        <Layout>
          <Leads />
        </Layout>
      </ProtectedRoute>
    )
  },
  {
    path: "/contacts",
    element: (
      <ProtectedRoute>
        <Layout>
          <Contacts />
        </Layout>
      </ProtectedRoute>
    )
  },
  {
    path: "/accounts",
    element: (
      <ProtectedRoute>
        <Layout>
          <Accounts />
        </Layout>
      </ProtectedRoute>
    )
  },
  {
    path: "/deals",
    element: (
      <ProtectedRoute>
        <Layout>
          <Deals />
        </Layout>
      </ProtectedRoute>
    )
  },
  {
    path: "/activities",
    element: (
      <ProtectedRoute>
        <Layout>
          <Activities />
        </Layout>
      </ProtectedRoute>
    )
  },
  {
    path: "/members",
    element: (
      <RoleRoute allowedRoles={["Admin", "Manager"]}>
        <Layout>
          <Members />
        </Layout>
      </RoleRoute>
    )
  },
  {
    path: "/organizations",
    element: (
      <RoleRoute allowedRoles={["Admin", "Manager"]}>
        <Layout>
          <Organizations />
        </Layout>
      </RoleRoute>
    )
  }
], {
  future: {
    v7_startTransition: true,
    v7_relativeSplatPath: true
  }
});

function App() {
  return (
    <AuthProvider>
      <div className="App">
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: '#363636',
              color: '#fff',
            },
            success: {
              duration: 3000,
              iconTheme: {
                primary: '#10b981',
                secondary: '#fff',
              },
            },
            error: {
              duration: 4000,
              iconTheme: {
                primary: '#ef4444',
                secondary: '#fff',
              },
            },
          }}
        />
        <RouterProvider router={router} />
      </div>
    </AuthProvider>
  );
}

export default App;
