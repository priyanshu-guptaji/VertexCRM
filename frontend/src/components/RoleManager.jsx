import React, { useState, useEffect } from 'react';
import axios from 'axios';

const RoleManager = () => {
  const [roles, setRoles] = useState([]);
  const [permissions, setPermissions] = useState([]);
  const [selectedRole, setSelectedRole] = useState(null);
  const [rolePermissions, setRolePermissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchRoles();
    fetchPermissions();
  }, []);

  useEffect(() => {
    if (selectedRole) {
      fetchRolePermissions(selectedRole.roleId);
    }
  }, [selectedRole]);

  const fetchRoles = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('/api/roles', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setRoles(response.data);
    } catch (err) {
      setError('Failed to fetch roles');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const fetchPermissions = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('/api/rbac/permissions', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setPermissions(response.data);
    } catch (err) {
      console.error('Failed to fetch permissions', err);
    }
  };

  const fetchRolePermissions = async (roleId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`/api/rbac/roles/${roleId}/permissions`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setRolePermissions(response.data);
    } catch (err) {
      console.error('Failed to fetch role permissions', err);
    }
  };

  const hasPermission = (permissionId) => {
    return rolePermissions.some(p => p.permissionId === permissionId);
  };

  const togglePermission = async (permissionId) => {
    if (!selectedRole) return;

    const token = localStorage.getItem('token');
    const hasIt = hasPermission(permissionId);

    try {
      if (hasIt) {
        // Remove permission
        await axios.delete(
          `/api/rbac/roles/${selectedRole.roleId}/permissions/${permissionId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
      } else {
        // Add permission
        await axios.post(
          `/api/rbac/roles/${selectedRole.roleId}/permissions`,
          { permissionId },
          { headers: { Authorization: `Bearer ${token}` } }
        );
      }
      // Refresh role permissions
      fetchRolePermissions(selectedRole.roleId);
    } catch (err) {
      console.error('Failed to update permission', err);
    }
  };

  const groupPermissionsByResource = () => {
    const grouped = {};
    permissions.forEach(permission => {
      if (!grouped[permission.resource]) {
        grouped[permission.resource] = [];
      }
      grouped[permission.resource].push(permission);
    });
    return grouped;
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  const groupedPermissions = groupPermissionsByResource();

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Role & Permission Manager</h1>
        <p className="text-gray-600">Manage role-based access control for your organization</p>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Roles List */}
        <div className="bg-white rounded-lg shadow">
          <div className="p-6 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-800">Roles</h2>
          </div>
          <div className="divide-y divide-gray-200">
            {roles.map((role) => (
              <div
                key={role.roleId}
                onClick={() => setSelectedRole(role)}
                className={`p-4 cursor-pointer hover:bg-gray-50 transition ${
                  selectedRole?.roleId === role.roleId ? 'bg-blue-50 border-l-4 border-blue-500' : ''
                }`}
              >
                <div className="font-medium text-gray-900">{role.roleName}</div>
                {role.description && (
                  <div className="text-sm text-gray-500 mt-1">{role.description}</div>
                )}
                {role.isSystem && (
                  <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-800 mt-2">
                    System Role
                  </span>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Permission Matrix */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg shadow">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">
                {selectedRole ? `Permissions for ${selectedRole.roleName}` : 'Select a role to view permissions'}
              </h2>
            </div>
            {selectedRole ? (
              <div className="p-6">
                {Object.entries(groupedPermissions).map(([resource, perms]) => (
                  <div key={resource} className="mb-6">
                    <h3 className="text-lg font-semibold text-gray-700 mb-3 flex items-center">
                      <span className="inline-flex items-center justify-center h-8 w-8 rounded-full bg-blue-100 text-blue-600 mr-2">
                        {resource.charAt(0)}
                      </span>
                      {resource}
                    </h3>
                    <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                      {perms.map((permission) => (
                        <label
                          key={permission.permissionId}
                          className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg hover:bg-gray-100 cursor-pointer transition"
                        >
                          <input
                            type="checkbox"
                            checked={hasPermission(permission.permissionId)}
                            onChange={() => togglePermission(permission.permissionId)}
                            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                          />
                          <div className="flex-1">
                            <div className="text-sm font-medium text-gray-900">
                              {permission.action}
                            </div>
                            {permission.description && (
                              <div className="text-xs text-gray-500">
                                {permission.description}
                              </div>
                            )}
                          </div>
                        </label>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="p-12 text-center text-gray-500">
                <svg className="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                </svg>
                <p>Select a role from the left to manage its permissions</p>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Permission Summary */}
      {selectedRole && (
        <div className="mt-6 bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">Permission Summary</h3>
          <div className="flex items-center space-x-4">
            <div className="flex-1">
              <div className="text-3xl font-bold text-blue-600">
                {rolePermissions.length}
              </div>
              <div className="text-sm text-gray-500">Permissions Granted</div>
            </div>
            <div className="flex-1">
              <div className="text-3xl font-bold text-gray-600">
                {permissions.length - rolePermissions.length}
              </div>
              <div className="text-sm text-gray-500">Permissions Denied</div>
            </div>
            <div className="flex-1">
              <div className="text-3xl font-bold text-purple-600">
                {Math.round((rolePermissions.length / permissions.length) * 100)}%
              </div>
              <div className="text-sm text-gray-500">Coverage</div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default RoleManager;
