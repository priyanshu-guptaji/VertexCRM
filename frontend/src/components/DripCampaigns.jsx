import React, { useState, useEffect } from 'react';
import axios from 'axios';

const DripCampaigns = () => {
  const [dripCampaigns, setDripCampaigns] = useState([]);
  const [segments, setSegments] = useState([]);
  const [templates, setTemplates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [selectedCampaign, setSelectedCampaign] = useState(null);
  const [campaignForm, setCampaignForm] = useState({
    campaignName: '',
    description: '',
    triggerType: 'LEAD_CREATED',
    targetSegmentId: '',
    isActive: true
  });
  const [stepForm, setStepForm] = useState({
    stepOrder: 1,
    delayDays: 0,
    delayHours: 0,
    templateId: '',
    actionType: 'SEND_EMAIL'
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const token = localStorage.getItem('token');
      const [campaignsRes, segmentsRes, templatesRes] = await Promise.all([
        axios.get('/api/marketing/drip-campaigns', { headers: { Authorization: `Bearer ${token}` } }),
        axios.get('/api/segments', { headers: { Authorization: `Bearer ${token}` } }),
        axios.get('/api/marketing/templates', { headers: { Authorization: `Bearer ${token}` } })
      ]);
      
      setDripCampaigns(campaignsRes.data);
      setSegments(segmentsRes.data);
      setTemplates(templatesRes.data);
    } catch (err) {
      console.error('Failed to fetch data', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateCampaign = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post('/api/marketing/drip-campaigns', campaignForm, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      setDripCampaigns([...dripCampaigns, response.data]);
      setShowCreateForm(false);
      setCampaignForm({
        campaignName: '',
        description: '',
        triggerType: 'LEAD_CREATED',
        targetSegmentId: '',
        isActive: true
      });
    } catch (err) {
      console.error('Failed to create drip campaign', err);
    }
  };

  const handleAddStep = async (campaignId) => {
    try {
      const token = localStorage.getItem('token');
      const stepData = {
        ...stepForm,
        dripCampaignId: campaignId
      };
      
      await axios.post(`/api/marketing/drip-campaigns/${campaignId}/steps`, stepData, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      setStepForm({
        stepOrder: stepForm.stepOrder + 1,
        delayDays: 0,
        delayHours: 0,
        templateId: '',
        actionType: 'SEND_EMAIL'
      });
      
      fetchData();
    } catch (err) {
      console.error('Failed to add step', err);
    }
  };

  const handleToggleCampaign = async (campaignId, isActive) => {
    try {
      const token = localStorage.getItem('token');
      await axios.put(`/api/marketing/drip-campaigns/${campaignId}`, { isActive }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      fetchData();
    } catch (err) {
      console.error('Failed to update campaign', err);
    }
  };

  const triggerTypes = [
    { value: 'LEAD_CREATED', label: 'Lead Created' },
    { value: 'DEAL_STAGE_CHANGED', label: 'Deal Stage Changed' },
    { value: 'FORM_SUBMITTED', label: 'Form Submitted' },
    { value: 'INACTIVITY', label: 'Contact Inactivity' }
  ];

  const actionTypes = [
    { value: 'SEND_EMAIL', label: 'Send Email' },
    { value: 'SEND_SMS', label: 'Send SMS' },
    { value: 'CREATE_TASK', label: 'Create Task' },
    { value: 'UPDATE_LEAD_SCORE', label: 'Update Lead Score' }
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
            <h1 className="text-3xl font-bold text-gray-800 mb-2">Drip Campaigns</h1>
            <p className="text-gray-600">Create automated email sequences and workflows</p>
          </div>
          <button
            onClick={() => setShowCreateForm(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg flex items-center"
          >
            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Create Drip Campaign
          </button>
        </div>
      </div>

      {/* Create Drip Campaign Form Modal */}
      {showCreateForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl mx-4">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">Create New Drip Campaign</h2>
            </div>
            <form onSubmit={handleCreateCampaign} className="px-6 py-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Campaign Name</label>
                  <input
                    type="text"
                    value={campaignForm.campaignName}
                    onChange={(e) => setCampaignForm({...campaignForm, campaignName: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Trigger Type</label>
                  <select
                    value={campaignForm.triggerType}
                    onChange={(e) => setCampaignForm({...campaignForm, triggerType: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    {triggerTypes.map(type => (
                      <option key={type.value} value={type.value}>{type.label}</option>
                    ))}
                  </select>
                </div>
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                  <textarea
                    value={campaignForm.description}
                    onChange={(e) => setCampaignForm({...campaignForm, description: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    rows="3"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Target Segment</label>
                  <select
                    value={campaignForm.targetSegmentId}
                    onChange={(e) => setCampaignForm({...campaignForm, targetSegmentId: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">All contacts</option>
                    {segments.map(segment => (
                      <option key={segment.segmentId} value={segment.segmentId}>
                        {segment.segmentName}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="flex items-center">
                  <input
                    type="checkbox"
                    checked={campaignForm.isActive}
                    onChange={(e) => setCampaignForm({...campaignForm, isActive: e.target.checked})}
                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                  />
                  <label className="ml-2 block text-sm text-gray-700">
                    Active Campaign
                  </label>
                </div>
              </div>
              <div className="mt-6 flex justify-end space-x-3">
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
                  Create Campaign
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Drip Campaigns List */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">Campaigns</h2>
            </div>
            <div className="divide-y divide-gray-200">
              {dripCampaigns.map(campaign => (
                <div
                  key={campaign.dripCampaignId}
                  className={`p-4 cursor-pointer hover:bg-gray-50 ${
                    selectedCampaign?.dripCampaignId === campaign.dripCampaignId ? 'bg-blue-50 border-l-4 border-blue-500' : ''
                  }`}
                  onClick={() => setSelectedCampaign(campaign)}
                >
                  <div className="flex justify-between">
                    <div className="font-medium text-gray-900">{campaign.campaignName}</div>
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      campaign.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                    }`}>
                      {campaign.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </div>
                  <div className="text-sm text-gray-500 mt-1">{campaign.triggerType}</div>
                  <div className="text-xs text-gray-400 mt-2">
                    {campaign.steps ? campaign.steps.length : 0} steps
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="lg:col-span-2">
          {selectedCampaign ? (
            <div className="bg-white rounded-lg shadow">
              <div className="px-6 py-4 border-b border-gray-200">
                <div className="flex justify-between items-center">
                  <div>
                    <h2 className="text-xl font-semibold text-gray-800">{selectedCampaign.campaignName}</h2>
                    <p className="text-gray-600 mt-1">{selectedCampaign.description}</p>
                  </div>
                  <button
                    onClick={() => handleToggleCampaign(selectedCampaign.dripCampaignId, !selectedCampaign.isActive)}
                    className={`px-3 py-1 rounded-full text-sm font-medium ${
                      selectedCampaign.isActive 
                        ? 'bg-red-100 text-red-800 hover:bg-red-200' 
                        : 'bg-green-100 text-green-800 hover:bg-green-200'
                    }`}
                  >
                    {selectedCampaign.isActive ? 'Deactivate' : 'Activate'}
                  </button>
                </div>
              </div>
              
              {/* Add Step Form */}
              <div className="px-6 py-4 border-b border-gray-200">
                <h3 className="text-lg font-medium text-gray-800 mb-3">Add New Step</h3>
                <div className="grid grid-cols-1 md:grid-cols-5 gap-3">
                  <div>
                    <label className="block text-xs text-gray-500 mb-1">Step #</label>
                    <input
                      type="number"
                      value={stepForm.stepOrder}
                      onChange={(e) => setStepForm({...stepForm, stepOrder: parseInt(e.target.value)})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      min="1"
                    />
                  </div>
                  <div>
                    <label className="block text-xs text-gray-500 mb-1">Days</label>
                    <input
                      type="number"
                      value={stepForm.delayDays}
                      onChange={(e) => setStepForm({...stepForm, delayDays: parseInt(e.target.value)})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      min="0"
                    />
                  </div>
                  <div>
                    <label className="block text-xs text-gray-500 mb-1">Hours</label>
                    <input
                      type="number"
                      value={stepForm.delayHours}
                      onChange={(e) => setStepForm({...stepForm, delayHours: parseInt(e.target.value)})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      min="0"
                      max="23"
                    />
                  </div>
                  <div>
                    <label className="block text-xs text-gray-500 mb-1">Template</label>
                    <select
                      value={stepForm.templateId}
                      onChange={(e) => setStepForm({...stepForm, templateId: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="">Select template</option>
                      {templates.map(template => (
                        <option key={template.templateId} value={template.templateId}>
                          {template.templateName}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label className="block text-xs text-gray-500 mb-1">&nbsp;</label>
                    <button
                      onClick={() => handleAddStep(selectedCampaign.dripCampaignId)}
                      className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md"
                    >
                      Add Step
                    </button>
                  </div>
                </div>
              </div>
              
              {/* Campaign Steps */}
              <div className="px-6 py-4">
                <h3 className="text-lg font-medium text-gray-800 mb-3">Campaign Steps</h3>
                {selectedCampaign.steps && selectedCampaign.steps.length > 0 ? (
                  <div className="space-y-4">
                    {selectedCampaign.steps
                      .sort((a, b) => a.stepOrder - b.stepOrder)
                      .map(step => {
                        const template = templates.find(t => t.templateId === step.templateId);
                        return (
                          <div key={step.stepId} className="flex items-start p-4 bg-gray-50 rounded-md">
                            <div className="flex-shrink-0 w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-white font-medium">
                              {step.stepOrder}
                            </div>
                            <div className="ml-4 flex-1">
                              <div className="flex justify-between">
                                <div>
                                  <div className="font-medium text-gray-900">
                                    {template ? template.templateName : 'Unknown Template'}
                                  </div>
                                  <div className="text-sm text-gray-500 mt-1">
                                    {step.actionType.replace('_', ' ')}
                                  </div>
                                </div>
                                <div className="text-sm text-gray-500">
                                  {step.delayDays > 0 && `${step.delayDays} days`}
                                  {step.delayHours > 0 && ` ${step.delayHours} hours`}
                                  {(step.delayDays === 0 && step.delayHours === 0) && 'Immediate'}
                                </div>
                              </div>
                            </div>
                          </div>
                        );
                      })}
                  </div>
                ) : (
                  <div className="text-center py-8 text-gray-500">
                    <p>No steps defined for this campaign yet.</p>
                    <p className="text-sm mt-1">Add steps above to create your drip campaign workflow.</p>
                  </div>
                )}
              </div>
            </div>
          ) : (
            <div className="bg-white rounded-lg shadow h-full flex items-center justify-center">
              <div className="text-center p-12">
                <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <h3 className="mt-2 text-lg font-medium text-gray-900">No campaign selected</h3>
                <p className="mt-1 text-gray-500">Select a drip campaign from the list to view and manage its steps.</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DripCampaigns;
