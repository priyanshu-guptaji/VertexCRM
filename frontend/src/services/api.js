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
    // Attach tenant header from env or subdomain
    const envTenant = import.meta.env?.VITE_TENANT_ID;
    let tenant = envTenant;
    if (!tenant && typeof window !== 'undefined') {
      const host = window.location.host.split(':')[0];
      const parts = host.split('.');
      if (parts.length >= 3) {
        tenant = parts[0];
      }
    }
    if (!tenant) {
      tenant = localStorage.getItem('tenant');
    }
    if (tenant) {
      config.headers['X-Tenant-ID'] = String(tenant).trim().toLowerCase().replace(/[^a-z0-9_]+/g, '_');
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
    let backendMessage;
    if (typeof error.response?.data === 'string') {
      backendMessage = error.response.data;
    } else if (error.response?.data?.detail) {
      backendMessage = error.response.data.detail;
    } else if (error.response?.data?.title || error.response?.data?.message) {
      backendMessage = `${error.response?.data?.title || 'Error'}: ${error.response?.data?.message || ''}`.trim();
    } else {
      backendMessage = JSON.stringify(error.response?.data);
    }

    console.error('API Response Error:', {
      status: error.response?.status,
      statusText: error.response?.statusText,
      url: error.config?.url,
      data: backendMessage,
      message: error.message
    });
    
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

// ========== Sales Automation APIs ==========

/**
 * Get all lead scores
 */
export const getLeadScores = () => api.get('/sales/lead-scores');

/**
 * Get top-scoring leads
 */
export const getTopLeads = (minScore = 60) => api.get(`/sales/top-leads?minScore=${minScore}`);

/**
 * Update lead score on activity
 */
export const updateLeadScoreOnActivity = (leadId, activityType) => 
  api.post('/sales/lead-scores/activity', { leadId, activityType });

/**
 * Get all pipelines
 */
export const getPipelines = () => api.get('/sales/pipelines');

/**
 * Get default pipeline
 */
export const getDefaultPipeline = () => api.get('/sales/pipelines/default');

/**
 * Create pipeline
 */
export const createPipeline = (pipelineData) => api.post('/sales/pipelines', pipelineData);

/**
 * Update pipeline
 */
export const updatePipeline = (id, pipelineData) => api.put(`/sales/pipelines/${id}`, pipelineData);

/**
 * Delete pipeline
 */
export const deletePipeline = (id) => api.delete(`/sales/pipelines/${id}`);

/**
 * Get deal stage rules
 */
export const getDealStageRules = () => api.get('/sales/stage-rules');

/**
 * Create deal stage rule
 */
export const createDealStageRule = (ruleData) => api.post('/sales/stage-rules', ruleData);

/**
 * Get follow-up rules
 */
export const getFollowUpRules = () => api.get('/sales/follow-up-rules');

/**
 * Create follow-up rule
 */
export const createFollowUpRule = (ruleData) => api.post('/sales/follow-up-rules', ruleData);

/**
 * Trigger follow-up checks
 */
export const triggerFollowUpChecks = () => api.post('/sales/follow-up-rules/trigger');

// ========== RBAC APIs ==========

/**
 * Get all permissions
 */
export const getPermissions = () => api.get('/rbac/permissions');

/**
 * Get permissions by resource
 */
export const getPermissionsByResource = (resource) => api.get(`/rbac/permissions/resource/${resource}`);

/**
 * Create permission
 */
export const createPermission = (permissionData) => api.post('/rbac/permissions', permissionData);

/**
 * Initialize default permissions
 */
export const initializePermissions = () => api.post('/rbac/permissions/initialize');

/**
 * Get role permissions
 */
export const getRolePermissions = (roleId) => api.get(`/rbac/roles/${roleId}/permissions`);

/**
 * Assign permission to role
 */
export const assignPermissionToRole = (roleId, permissionId) => 
  api.post(`/rbac/roles/${roleId}/permissions`, { permissionId });

/**
 * Remove permission from role
 */
export const removePermissionFromRole = (roleId, permissionId) => 
  api.delete(`/rbac/roles/${roleId}/permissions/${permissionId}`);

/**
 * Get current member permissions
 */
export const getMyPermissions = () => api.get('/rbac/members/me/permissions');

/**
 * Get member permissions
 */
export const getMemberPermissions = (memberId) => api.get(`/rbac/members/${memberId}/permissions`);

/**
 * Check if current member has permission
 */
export const checkPermission = (resource, action) => 
  api.post('/rbac/check-permission', { resource, action });

/**
 * Check if current member has permission by name
 */
export const checkPermissionByName = (permissionName) => 
  api.post('/rbac/check-permission-by-name', { permissionName });

