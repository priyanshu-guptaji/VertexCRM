import React, { useState, useEffect } from 'react';
import axios from 'axios';

const PermissionMatrix = () => {
  const [roles, setRoles] = useState([]);
  const [permissions, setPermissions] = useState([]);
  const [rolePermissions, setRolePermissions] = useState({});
  const [loading, setLoading] = useState(true);
  const [selectedRole, setSelectedRole] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const token = localStorage.getItem('token');
      const [rolesRes, permissionsRes] = await Promise.all([
        axios.get('/api/roles', { headers: { Authorization: `Bearer ${token}` } }),
        axios.get('/api/rbac/permissions', { headers: { Authorization: `Bearer ${token}` } })
      ]);
      
      setRoles(rolesRes.data);
      setPermissions(permissionsRes.data);
      
      // Fetch permissions for each role
      const rolePermissionsData = {};
      for (const role of rolesRes.data) {
        const permRes = await axios.get(`/api/rbac/roles/${role.roleId}/permissions`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        rolePermissionsData[role.roleId] = permRes.data.map(p => p.permissionId);
      }
      setRolePermissions(rolePermissionsData);
    } catch (err) {
      console.error('Failed to fetch data', err);
    } finally {
      setLoading(false);
    }
  };

  const handlePermissionToggle = async (roleId, permissionId) => {
    try {
      const token = localStorage.getItem('token');
      const currentPermissions = rolePermissions[roleId] || [];
      const hasPermission = currentPermissions.includes(permissionId);
      
      if (hasPermission) {
        // Remove permission
        await axios.delete(`/api/rbac/roles/${roleId}/permissions/${permissionId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        
        // Update state
        setRolePermissions({
          ...rolePermissions,
          [roleId]: currentPermissions.filter(id => id !== permissionId)
        });
      } else {
        // Add permission
        await axios.post(`/api/rbac/roles/${roleId}/permissions`, 
          { permissionId },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        
        // Update state
        setRolePermissions({
          ...rolePermissions,
          [roleId]: [...currentPermissions, permissionId]
        });
      }
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

  const hasPermission = (roleId, permissionId) => {
    const rolePerms = rolePermissions[roleId] || [];
    return rolePerms.includes(permissionId);
  };

  const groupedPermissions = groupPermissionsByResource();

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Permission Matrix</h1>
        <p className="text-gray-600">Manage role-based access control permissions</p>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="bg-gray-50">
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider sticky left-0 bg-gray-50">
                  Permission
                </th>
                {roles.map(role => (
                  <th 
                    key={role.roleId} 
                    className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
                    style={{ minWidth: '120px' }}
                  >
                    <div className="flex flex-col items-center">
                      <span className="font-medium">{role.roleName}</span>
                      {role.isSystem && (
                        <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-800 mt-1">
                          System
                        </span>
                      )}
                    </div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {Object.entries(groupedPermissions).map(([resource, perms]) => (
                <React.Fragment key={resource}>
                  <tr className="bg-gray-100">
                    <td colSpan={roles.length + 1} className="px-6 py-2 text-sm font-medium text-gray-700">
                      {resource}
                    </td>
                  </tr>
                  {perms.map(permission => (
                    <tr key={permission.permissionId} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 sticky left-0 bg-white">
                        <div>
                          <div>{permission.action}</div>
                          {permission.description && (
                            <div className="text-xs text-gray-500 mt-1">{permission.description}</div>
                          )}
                        </div>
                      </td>
                      {roles.map(role => (
                        <td key={`${role.roleId}-${permission.permissionId}`} className="px-6 py-4 whitespace-nowrap text-center">
                          <input
                            type="checkbox"
                            checked={hasPermission(role.roleId, permission.permissionId)}
                            onChange={() => handlePermissionToggle(role.roleId, permission.permissionId)}
                            disabled={role.isSystem && permission.isSystem}
                            className="h-5 w-5 text-blue-600 focus:ring-blue-500 border-gray-300 rounded cursor-pointer disabled:opacity-50"
                          />
                        </td>
                      ))}
                    </tr>
                  ))}
                </React.Fragment>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Role Summary */}
      <div className="mt-8">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">Role Permission Summary</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {roles.map(role => {
            const rolePerms = rolePermissions[role.roleId] || [];
            const totalPerms = permissions.length;
            const grantedPerms = rolePerms.length;
            const percentage = totalPerms > 0 ? Math.round((grantedPerms / totalPerms) * 100) : 0;
            
            return (
              <div key={role.roleId} className="bg-white rounded-lg shadow p-4">
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="font-medium text-gray-900">{role.roleName}</h3>
                    {role.description && (
                      <p className="text-sm text-gray-500 mt-1">{role.description}</p>
                    )}
                  </div>
                  {role.isSystem && (
                    <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-800">
                      System
                    </span>
                  )}
                </div>
                <div className="mt-3">
                  <div className="flex justify-between text-sm mb-1">
                    <span className="text-gray-500">Permissions</span>
                    <span className="font-medium">{grantedPerms}/{totalPerms}</span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-blue-600 h-2 rounded-full" 
                      style={{ width: `${percentage}%` }}
                    ></div>
                  </div>
                  <div className="text-right text-xs text-gray-500 mt-1">
                    {percentage}% coverage
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default PermissionMatrix;
