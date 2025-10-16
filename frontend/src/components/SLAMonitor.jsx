import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SLAMonitor = () => {
  const [tickets, setTickets] = useState([]);
  const [policies, setPolicies] = useState([]);
  const [categories, setCategories] = useState([]);
  const [agents, setAgents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreatePolicy, setShowCreatePolicy] = useState(false);
  const [policyForm, setPolicyForm] = useState({
    policyName: '',
    description: '',
    categoryId: '',
    priority: 'medium',
    firstResponseMinutes: 120,
    resolutionMinutes: 1440,
    businessHoursOnly: false,
    escalationEnabled: false,
    escalationAssigneeId: ''
  });
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const token = localStorage.getItem('token');
      const [ticketsRes, policiesRes, categoriesRes, agentsRes] = await Promise.all([
        axios.get('/api/tickets', { headers: { Authorization: `Bearer ${token}` } }),
        axios.get('/api/service/sla-policies', { headers: { Authorization: `Bearer ${token}` } }),
        axios.get('/api/service/categories', { headers: { Authorization: `Bearer ${token}` } }),
        axios.get('/api/members', { headers: { Authorization: `Bearer ${token}` } })
      ]);
      
      setTickets(ticketsRes.data);
      setPolicies(policiesRes.data);
      setCategories(categoriesRes.data);
      setAgents(agentsRes.data);
    } catch (err) {
      console.error('Failed to fetch data', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreatePolicy = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post('/api/service/sla-policies', policyForm, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      setPolicies([...policies, response.data]);
      setShowCreatePolicy(false);
      setPolicyForm({
        policyName: '',
        description: '',
        categoryId: '',
        priority: 'medium',
        firstResponseMinutes: 120,
        resolutionMinutes: 1440,
        businessHoursOnly: false,
        escalationEnabled: false,
        escalationAssigneeId: ''
      });
    } catch (err) {
      console.error('Failed to create policy', err);
    }
  };

  const getSLAStatus = (ticket) => {
    const now = new Date();
    const createdAt = new Date(ticket.createdAt);
    
    // Check first response SLA
    if (ticket.firstResponseDue) {
      const firstResponseDue = new Date(ticket.firstResponseDue);
      if (now > firstResponseDue && !ticket.firstResponseAt) {
        return { type: 'breached', message: 'First Response SLA Breached' };
      }
    }
    
    // Check resolution SLA
    if (ticket.resolutionDue) {
      const resolutionDue = new Date(ticket.resolutionDue);
      if (now > resolutionDue && ticket.status !== 'closed') {
        return { type: 'breached', message: 'Resolution SLA Breached' };
      }
    }
    
    // Check if within SLA
    if (ticket.firstResponseDue || ticket.resolutionDue) {
      return { type: 'within', message: 'Within SLA' };
    }
    
    return { type: 'none', message: 'No SLA' };
  };

  const getStatusColor = (status) => {
    if (status.type === 'breached') {
      return 'bg-red-100 text-red-800';
    } else if (status.type === 'within') {
      return 'bg-green-100 text-green-800';
    }
    return 'bg-gray-100 text-gray-800';
  };

  const formatTimeRemaining = (dueDate) => {
    if (!dueDate) return 'N/A';
    
    const now = new Date();
    const due = new Date(dueDate);
    const diffMs = due - now;
    
    if (diffMs < 0) {
      return 'Overdue';
    }
    
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
    const diffHours = Math.floor((diffMs % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const diffMinutes = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));
    
    if (diffDays > 0) {
      return `${diffDays}d ${diffHours}h`;
    } else if (diffHours > 0) {
      return `${diffHours}h ${diffMinutes}m`;
    } else {
      return `${diffMinutes}m`;
    }
  };

  const filteredTickets = filter === 'all' 
    ? tickets 
    : tickets.filter(ticket => {
        const slaStatus = getSLAStatus(ticket);
        if (filter === 'breached') {
          return slaStatus.type === 'breached';
        } else if (filter === 'within') {
          return slaStatus.type === 'within';
        }
        return slaStatus.type === 'none';
      });

  const slaStats = {
    total: tickets.length,
    breached: tickets.filter(t => getSLAStatus(t).type === 'breached').length,
    within: tickets.filter(t => getSLAStatus(t).type === 'within').length,
    none: tickets.filter(t => getSLAStatus(t).type === 'none').length
  };

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
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-gray-800 mb-2">SLA Monitor</h1>
            <p className="text-gray-600">Track and manage service level agreements</p>
          </div>
          <button
            onClick={() => setShowCreatePolicy(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg flex items-center"
          >
            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Create Policy
          </button>
        </div>
      </div>

      {/* SLA Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
        <div className="bg-white rounded-lg shadow p-4">
          <div className="text-gray-500 text-sm">Total Tickets</div>
          <div className="text-2xl font-bold text-gray-800">{slaStats.total}</div>
        </div>
        <div className="bg-white rounded-lg shadow p-4">
          <div className="text-gray-500 text-sm">Within SLA</div>
          <div className="text-2xl font-bold text-green-600">{slaStats.within}</div>
          <div className="text-sm text-gray-500">
            {slaStats.total > 0 ? ((slaStats.within / slaStats.total) * 100).toFixed(1) + '%' : '0%'}
          </div>
        </div>
        <div className="bg-white rounded-lg shadow p-4">
          <div className="text-gray-500 text-sm">SLA Breached</div>
          <div className="text-2xl font-bold text-red-600">{slaStats.breached}</div>
          <div className="text-sm text-gray-500">
            {slaStats.total > 0 ? ((slaStats.breached / slaStats.total) * 100).toFixed(1) + '%' : '0%'}
          </div>
        </div>
        <div className="bg-white rounded-lg shadow p-4">
          <div className="text-gray-500 text-sm">No SLA</div>
          <div className="text-2xl font-bold text-gray-600">{slaStats.none}</div>
        </div>
      </div>

      {/* Create Policy Form Modal */}
      {showCreatePolicy && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl mx-4">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">Create SLA Policy</h2>
            </div>
            <form onSubmit={handleCreatePolicy} className="px-6 py-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Policy Name</label>
                  <input
                    type="text"
                    value={policyForm.policyName}
                    onChange={(e) => setPolicyForm({...policyForm, policyName: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                  <textarea
                    value={policyForm.description}
                    onChange={(e) => setPolicyForm({...policyForm, description: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    rows="2"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Category</label>
                  <select
                    value={policyForm.categoryId}
                    onChange={(e) => setPolicyForm({...policyForm, categoryId: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">All Categories</option>
                    {categories.map(category => (
                      <option key={category.categoryId} value={category.categoryId}>
                        {category.categoryName}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Priority</label>
                  <select
                    value={policyForm.priority}
                    onChange={(e) => setPolicyForm({...policyForm, priority: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="low">Low</option>
                    <option value="medium">Medium</option>
                    <option value="high">High</option>
                    <option value="urgent">Urgent</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">First Response (minutes)</label>
                  <input
                    type="number"
                    value={policyForm.firstResponseMinutes}
                    onChange={(e) => setPolicyForm({...policyForm, firstResponseMinutes: parseInt(e.target.value)})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    min="1"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Resolution (minutes)</label>
                  <input
                    type="number"
                    value={policyForm.resolutionMinutes}
                    onChange={(e) => setPolicyForm({...policyForm, resolutionMinutes: parseInt(e.target.value)})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    min="1"
                  />
                </div>
                <div className="flex items-center">
                  <input
                    type="checkbox"
                    checked={policyForm.businessHoursOnly}
                    onChange={(e) => setPolicyForm({...policyForm, businessHoursOnly: e.target.checked})}
                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                  />
                  <label className="ml-2 block text-sm text-gray-700">
                    Business Hours Only
                  </label>
                </div>
                <div className="flex items-center">
                  <input
                    type="checkbox"
                    checked={policyForm.escalationEnabled}
                    onChange={(e) => setPolicyForm({...policyForm, escalationEnabled: e.target.checked})}
                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                  />
                  <label className="ml-2 block text-sm text-gray-700">
                    Enable Escalation
                  </label>
                </div>
                {policyForm.escalationEnabled && (
                  <div className="md:col-span-2">
                    <label className="block text-sm font-medium text-gray-700 mb-1">Escalation Assignee</label>
                    <select
                      value={policyForm.escalationAssigneeId}
                      onChange={(e) => setPolicyForm({...policyForm, escalationAssigneeId: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="">Select assignee</option>
                      {agents.map(agent => (
                        <option key={agent.memberId} value={agent.memberId}>
                          {agent.memberName}
                        </option>
                      ))}
                    </select>
                  </div>
                )}
              </div>
              <div className="mt-6 flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => setShowCreatePolicy(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  Create Policy
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Filter Tabs */}
      <div className="mb-6">
        <div className="border-b border-gray-200">
          <nav className="-mb-px flex space-x-8">
            {[
              { key: 'all', label: 'All Tickets', count: slaStats.total },
              { key: 'within', label: 'Within SLA', count: slaStats.within },
              { key: 'breached', label: 'SLA Breached', count: slaStats.breached },
              { key: 'none', label: 'No SLA', count: slaStats.none }
            ].map(tab => (
              <button
                key={tab.key}
                onClick={() => setFilter(tab.key)}
                className={`whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm ${
                  filter === tab.key
                    ? 'border-blue-500 text-blue-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                {tab.label}
                <span className="ml-2 bg-gray-100 text-gray-800 text-xs font-medium px-2.5 py-0.5 rounded-full">
                  {tab.count}
                </span>
              </button>
            ))}
          </nav>
        </div>
      </div>

      {/* SLA Policies */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">SLA Policies</h2>
            </div>
            <div className="divide-y divide-gray-200">
              {policies.map(policy => (
                <div key={policy.slaId} className="p-4">
                  <div className="flex justify-between">
                    <div className="font-medium text-gray-900">{policy.policyName}</div>
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      policy.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                    }`}>
                      {policy.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </div>
                  <div className="text-sm text-gray-500 mt-1">
                    {categories.find(c => c.categoryId === policy.categoryId)?.categoryName || 'All Categories'} - {policy.priority}
                  </div>
                  <div className="text-xs text-gray-400 mt-2">
                    Response: {policy.firstResponseMinutes} min, Resolution: {policy.resolutionMinutes} min
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Tickets List */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg shadow">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ticket</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Category</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Priority</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">SLA Status</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Time Remaining</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Assignee</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {filteredTickets.map(ticket => {
                    const slaStatus = getSLAStatus(ticket);
                    return (
                      <tr key={ticket.ticketId} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">#{ticket.ticketId}</div>
                          <div className="text-sm text-gray-500">{ticket.subject}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {categories.find(c => c.categoryId === ticket.categoryId)?.categoryName || 'Uncategorized'}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                            ticket.priority === 'low' ? 'bg-green-100 text-green-800' :
                            ticket.priority === 'medium' ? 'bg-yellow-100 text-yellow-800' :
                            ticket.priority === 'high' ? 'bg-orange-100 text-orange-800' :
                            'bg-red-100 text-red-800'
                          }`}>
                            {ticket.priority}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(slaStatus)}`}>
                            {slaStatus.message}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {ticket.firstResponseDue && formatTimeRemaining(ticket.firstResponseDue)}
                          {ticket.resolutionDue && (
                            <div className="text-xs">
                              Resolution: {formatTimeRemaining(ticket.resolutionDue)}
                            </div>
                          )}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {agents.find(a => a.memberId === ticket.assigneeId)?.memberName || 'Unassigned'}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SLAMonitor;
