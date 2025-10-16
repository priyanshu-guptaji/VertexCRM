import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Segmentation = () => {
  const [segments, setSegments] = useState([]);
  const [contacts, setContacts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [selectedSegment, setSelectedSegment] = useState(null);
  const [segmentForm, setSegmentForm] = useState({
    segmentName: '',
    description: ''
  });
  const [ruleForm, setRuleForm] = useState({
    fieldName: 'contact_email',
    operator: 'CONTAINS',
    fieldValue: '',
    logicalOperator: 'AND'
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const token = localStorage.getItem('token');
      const [segmentsRes, contactsRes] = await Promise.all([
        axios.get('/api/segments', { headers: { Authorization: `Bearer ${token}` } }),
        axios.get('/api/contacts', { headers: { Authorization: `Bearer ${token}` } })
      ]);
      
      setSegments(segmentsRes.data);
      setContacts(contactsRes.data);
    } catch (err) {
      console.error('Failed to fetch data', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateSegment = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post('/api/segments', segmentForm, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      // Add the new segment to the list
      setSegments([...segments, response.data]);
      setShowCreateForm(false);
      setSegmentForm({ segmentName: '', description: '' });
    } catch (err) {
      console.error('Failed to create segment', err);
    }
  };

  const handleAddRule = async (segmentId) => {
    try {
      const token = localStorage.getItem('token');
      const ruleData = {
        ...ruleForm,
        segmentId: segmentId
      };
      
      await axios.post(`/api/segments/${segmentId}/rules`, ruleData, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      setRuleForm({
        fieldName: 'contact_email',
        operator: 'CONTAINS',
        fieldValue: '',
        logicalOperator: 'AND'
      });
      
      fetchData();
    } catch (err) {
      console.error('Failed to add rule', err);
    }
  };

  const getSegmentContacts = async (segmentId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`/api/segments/${segmentId}/contacts`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      return response.data;
    } catch (err) {
      console.error('Failed to get segment contacts', err);
      return [];
    }
  };

  const operatorOptions = [
    { value: 'EQUALS', label: 'Equals' },
    { value: 'NOT_EQUALS', label: 'Not Equals' },
    { value: 'CONTAINS', label: 'Contains' },
    { value: 'NOT_CONTAINS', label: 'Does Not Contain' },
    { value: 'STARTS_WITH', label: 'Starts With' },
    { value: 'ENDS_WITH', label: 'Ends With' },
    { value: 'GREATER_THAN', label: 'Greater Than' },
    { value: 'LESS_THAN', label: 'Less Than' },
    { value: 'IN', label: 'In List' }
  ];

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
            <h1 className="text-3xl font-bold text-gray-800 mb-2">Contact Segmentation</h1>
            <p className="text-gray-600">Create and manage contact segments for targeted campaigns</p>
          </div>
          <button
            onClick={() => setShowCreateForm(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg flex items-center"
          >
            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Create Segment
          </button>
        </div>
      </div>

      {/* Create Segment Form Modal */}
      {showCreateForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-md mx-4">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">Create New Segment</h2>
            </div>
            <form onSubmit={handleCreateSegment} className="px-6 py-4">
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Segment Name</label>
                <input
                  type="text"
                  value={segmentForm.segmentName}
                  onChange={(e) => setSegmentForm({...segmentForm, segmentName: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                <textarea
                  value={segmentForm.description}
                  onChange={(e) => setSegmentForm({...segmentForm, description: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  rows="3"
                />
              </div>
              <div className="flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => setShowCreateForm(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  Create Segment
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Segments List */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">Segments</h2>
            </div>
            <div className="divide-y divide-gray-200">
              {segments.map(segment => (
                <div
                  key={segment.segmentId}
                  className={`p-4 cursor-pointer hover:bg-gray-50 ${
                    selectedSegment?.segmentId === segment.segmentId ? 'bg-blue-50 border-l-4 border-blue-500' : ''
                  }`}
                  onClick={() => setSelectedSegment(segment)}
                >
                  <div className="font-medium text-gray-900">{segment.segmentName}</div>
                  {segment.description && (
                    <div className="text-sm text-gray-500 mt-1">{segment.description}</div>
                  )}
                  <div className="text-xs text-gray-400 mt-2">
                    {segment.contactCount || 0} contacts
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="lg:col-span-2">
          {selectedSegment ? (
            <div className="bg-white rounded-lg shadow">
              <div className="px-6 py-4 border-b border-gray-200">
                <h2 className="text-xl font-semibold text-gray-800">{selectedSegment.segmentName}</h2>
                {selectedSegment.description && (
                  <p className="text-gray-600 mt-1">{selectedSegment.description}</p>
                )}
              </div>
              
              {/* Add Rule Form */}
              <div className="px-6 py-4 border-b border-gray-200">
                <h3 className="text-lg font-medium text-gray-800 mb-3">Add Rule</h3>
                <div className="grid grid-cols-1 md:grid-cols-4 gap-3">
                  <div>
                    <select
                      value={ruleForm.fieldName}
                      onChange={(e) => setRuleForm({...ruleForm, fieldName: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="contact_email">Email</option>
                      <option value="contact_name">Name</option>
                      <option value="phone">Phone</option>
                      <option value="company">Company</option>
                    </select>
                  </div>
                  <div>
                    <select
                      value={ruleForm.operator}
                      onChange={(e) => setRuleForm({...ruleForm, operator: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                      {operatorOptions.map(option => (
                        <option key={option.value} value={option.value}>{option.label}</option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <input
                      type="text"
                      value={ruleForm.fieldValue}
                      onChange={(e) => setRuleForm({...ruleForm, fieldValue: e.target.value})}
                      placeholder="Value"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div>
                    <button
                      onClick={() => handleAddRule(selectedSegment.segmentId)}
                      className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md"
                    >
                      Add Rule
                    </button>
                  </div>
                </div>
              </div>
              
              {/* Segment Rules */}
              <div className="px-6 py-4">
                <h3 className="text-lg font-medium text-gray-800 mb-3">Segment Rules</h3>
                {selectedSegment.rules && selectedSegment.rules.length > 0 ? (
                  <div className="space-y-3">
                    {selectedSegment.rules.map((rule, index) => (
                      <div key={rule.segmentRuleId} className="flex items-center p-3 bg-gray-50 rounded-md">
                        <div className="flex-1">
                          <span className="font-medium">{rule.fieldName}</span>
                          <span className="mx-2 text-gray-500">{rule.operator}</span>
                          <span className="font-medium">{rule.fieldValue}</span>
                        </div>
                        {index > 0 && (
                          <span className="ml-2 px-2 py-1 bg-gray-200 text-gray-700 text-xs rounded">
                            {rule.logicalOperator}
                          </span>
                        )}
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="text-center py-8 text-gray-500">
                    <p>No rules defined for this segment yet.</p>
                    <p className="text-sm mt-1">Add rules above to filter contacts.</p>
                  </div>
                )}
              </div>
              
              {/* Preview Contacts */}
              <div className="px-6 py-4 border-t border-gray-200">
                <h3 className="text-lg font-medium text-gray-800 mb-3">Preview Contacts</h3>
                <button
                  onClick={() => getSegmentContacts(selectedSegment.segmentId)}
                  className="bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium py-2 px-4 rounded-md"
                >
                  Show Matching Contacts
                </button>
                <div className="mt-3 text-sm text-gray-600">
                  {selectedSegment.contactCount || 0} contacts match this segment
                </div>
              </div>
            </div>
          ) : (
            <div className="bg-white rounded-lg shadow h-full flex items-center justify-center">
              <div className="text-center p-12">
                <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                </svg>
                <h3 className="mt-2 text-lg font-medium text-gray-900">No segment selected</h3>
                <p className="mt-1 text-gray-500">Select a segment from the list to view and manage its rules.</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Segmentation;
