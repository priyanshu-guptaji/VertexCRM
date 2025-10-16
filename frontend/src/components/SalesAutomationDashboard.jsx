import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SalesAutomationDashboard = () => {
  const [pipelineData, setPipelineData] = useState([]);
  const [automationRules, setAutomationRules] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      
      // Fetch pipeline data
      const pipelineResponse = await axios.get('/api/sales/pipeline', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setPipelineData(pipelineResponse.data);
      
      // Fetch automation rules
      const rulesResponse = await axios.get('/api/sales/automation-rules', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setAutomationRules(rulesResponse.data);
      
      setError(null);
    } catch (err) {
      setError('Failed to fetch sales automation data');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const getStageColor = (stage) => {
    const colors = {
      'Prospecting': 'bg-blue-100 text-blue-800',
      'Qualification': 'bg-purple-100 text-purple-800',
      'Needs Analysis': 'bg-yellow-100 text-yellow-800',
      'Proposal': 'bg-orange-100 text-orange-800',
      'Negotiation': 'bg-red-100 text-red-800',
      'Closed Won': 'bg-green-100 text-green-800',
      'Closed Lost': 'bg-gray-100 text-gray-800'
    };
    return colors[stage] || 'bg-gray-100 text-gray-800';
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(value);
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Sales Automation Dashboard</h1>
        <p className="text-gray-600">Monitor and manage your sales automation workflows</p>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-gray-500 text-sm font-medium mb-2">Active Deals</h3>
          <p className="text-3xl font-bold text-gray-800">
            {pipelineData.reduce((sum, stage) => sum + stage.dealCount, 0)}
          </p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-gray-500 text-sm font-medium mb-2">Total Value</h3>
          <p className="text-3xl font-bold text-blue-600">
            {formatCurrency(pipelineData.reduce((sum, stage) => sum + stage.totalValue, 0))}
          </p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-gray-500 text-sm font-medium mb-2">Automation Rules</h3>
          <p className="text-3xl font-bold text-purple-600">{automationRules.length}</p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-gray-500 text-sm font-medium mb-2">Win Rate</h3>
          <p className="text-3xl font-bold text-green-600">
            {pipelineData.length > 0 
              ? Math.round((pipelineData.find(s => s.stage === 'Closed Won')?.dealCount || 0) / 
                  pipelineData.reduce((sum, stage) => sum + (stage.stage.includes('Closed') ? stage.dealCount : 0), 0) * 100 || 0)
              : 0}%
          </p>
        </div>
      </div>

      {/* Pipeline Visualization */}
      <div className="bg-white rounded-lg shadow mb-8">
        <div className="p-6 border-b border-gray-200">
          <h2 className="text-xl font-semibold text-gray-800">Sales Pipeline</h2>
        </div>
        <div className="p-6">
          <div className="flex overflow-x-auto pb-4 space-x-4">
            {pipelineData.map((stage, index) => (
              <div key={index} className="flex-shrink-0 w-48">
                <div className="text-center mb-4">
                  <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getStageColor(stage.stage)}`}>
                    {stage.stage}
                  </span>
                  <div className="mt-2 text-lg font-semibold text-gray-800">{stage.dealCount}</div>
                  <div className="text-sm text-gray-500">{formatCurrency(stage.totalValue)}</div>
                </div>
                <div className="space-y-2 max-h-64 overflow-y-auto">
                  {stage.deals && stage.deals.slice(0, 5).map((deal) => (
                    <div key={deal.dealId} className="p-3 bg-gray-50 rounded border border-gray-200">
                      <div className="font-medium text-sm text-gray-900 truncate">{deal.dealName}</div>
                      <div className="text-xs text-gray-500">{formatCurrency(deal.dealValue)}</div>
                    </div>
                  ))}
                  {stage.deals && stage.deals.length > 5 && (
                    <div className="text-xs text-gray-500 text-center">+{stage.deals.length - 5} more</div>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Automation Rules */}
      <div className="bg-white rounded-lg shadow">
        <div className="p-6 border-b border-gray-200 flex justify-between items-center">
          <h2 className="text-xl font-semibold text-gray-800">Active Automation Rules</h2>
          <button className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded text-sm font-medium">
            + New Rule
          </button>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Rule Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Trigger</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Action</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Last Run</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {automationRules.map((rule) => (
                <tr key={rule.ruleId} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{rule.name}</div>
                    <div className="text-xs text-gray-500">{rule.description}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{rule.triggerType}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{rule.actionType}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      rule.active ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                    }`}>
                      {rule.active ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {rule.lastRunAt ? new Date(rule.lastRunAt).toLocaleDateString() : 'Never'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default SalesAutomationDashboard;