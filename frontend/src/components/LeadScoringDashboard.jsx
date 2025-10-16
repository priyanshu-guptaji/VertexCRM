import React, { useState, useEffect } from 'react';
import axios from 'axios';

const LeadScoringDashboard = () => {
  const [leadScores, setLeadScores] = useState([]);
  const [topLeads, setTopLeads] = useState([]);
  const [loading, setLoading] = useState(true);
  const [minScore, setMinScore] = useState(60);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchLeadScores();
    fetchTopLeads();
  }, [minScore]);

  const fetchLeadScores = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      const response = await axios.get('/api/sales/lead-scores', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setLeadScores(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch lead scores');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const fetchTopLeads = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`/api/sales/top-leads?minScore=${minScore}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setTopLeads(response.data);
    } catch (err) {
      console.error('Failed to fetch top leads', err);
    }
  };

  const getGradeColor = (grade) => {
    const colors = {
      'A': 'bg-green-100 text-green-800',
      'B': 'bg-blue-100 text-blue-800',
      'C': 'bg-yellow-100 text-yellow-800',
      'D': 'bg-red-100 text-red-800'
    };
    return colors[grade] || 'bg-gray-100 text-gray-800';
  };

  const getScoreBarWidth = (score) => {
    return `${Math.min((score / 100) * 100, 100)}%`;
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
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Lead Scoring Dashboard</h1>
        <p className="text-gray-600">Monitor and analyze lead quality metrics</p>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-gray-500 text-sm font-medium mb-2">Total Leads</h3>
          <p className="text-3xl font-bold text-gray-800">{leadScores.length}</p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-gray-500 text-sm font-medium mb-2">Grade A Leads</h3>
          <p className="text-3xl font-bold text-green-600">
            {leadScores.filter(l => l.grade === 'A').length}
          </p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-gray-500 text-sm font-medium mb-2">Auto-Converted</h3>
          <p className="text-3xl font-bold text-blue-600">
            {leadScores.filter(l => l.autoConverted).length}
          </p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-gray-500 text-sm font-medium mb-2">Avg Score</h3>
          <p className="text-3xl font-bold text-purple-600">
            {leadScores.length > 0 
              ? Math.round(leadScores.reduce((sum, l) => sum + l.totalScore, 0) / leadScores.length)
              : 0}
          </p>
        </div>
      </div>

      {/* Top Leads Section */}
      <div className="bg-white rounded-lg shadow mb-8">
        <div className="p-6 border-b border-gray-200 flex justify-between items-center">
          <h2 className="text-xl font-semibold text-gray-800">Top Performing Leads</h2>
          <div className="flex items-center space-x-2">
            <label className="text-sm text-gray-600">Min Score:</label>
            <select 
              value={minScore}
              onChange={(e) => setMinScore(Number(e.target.value))}
              className="border border-gray-300 rounded px-3 py-1 text-sm"
            >
              <option value="40">40+</option>
              <option value="60">60+</option>
              <option value="80">80+</option>
            </select>
          </div>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Lead Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Total Score</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Grade</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Engagement</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Behavior</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {topLeads.map((lead) => (
                <tr key={lead.scoreId} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{lead.leadName}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-500">{lead.leadEmail}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="text-sm font-semibold text-gray-900 mr-2">{lead.totalScore}</div>
                      <div className="w-24 bg-gray-200 rounded-full h-2">
                        <div 
                          className="bg-blue-600 h-2 rounded-full" 
                          style={{ width: getScoreBarWidth(lead.totalScore) }}
                        ></div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getGradeColor(lead.grade)}`}>
                      {lead.grade}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{lead.engagementScore}</div>
                    <div className="text-xs text-gray-500">
                      {lead.emailOpens} opens, {lead.emailClicks} clicks
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{lead.behaviorScore}</div>
                    <div className="text-xs text-gray-500">
                      {lead.websiteVisits} visits, {lead.formSubmissions} forms
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {lead.autoConverted ? (
                      <span className="px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                        Converted
                      </span>
                    ) : (
                      <span className="px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">
                        Active
                      </span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* All Leads Section */}
      <div className="bg-white rounded-lg shadow">
        <div className="p-6 border-b border-gray-200">
          <h2 className="text-xl font-semibold text-gray-800">All Lead Scores</h2>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Lead Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Total Score</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Grade</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Engagement</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Demographic</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Behavior</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Last Activity</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {leadScores.map((lead) => (
                <tr key={lead.scoreId} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{lead.leadName}</div>
                    <div className="text-xs text-gray-500">{lead.leadEmail}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-semibold text-gray-900">{lead.totalScore}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getGradeColor(lead.grade)}`}>
                      {lead.grade}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {lead.engagementScore}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {lead.demographicScore}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {lead.behaviorScore}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {lead.lastActivityDate 
                      ? new Date(lead.lastActivityDate).toLocaleDateString()
                      : 'No activity'}
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

export default LeadScoringDashboard;
