import React, { useState, useEffect } from 'react';
import axios from 'axios';

const FeedbackSystem = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showFeedbackForm, setShowFeedbackForm] = useState(false);
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [feedbackForm, setFeedbackForm] = useState({
    ticketId: '',
    rating: 5,
    comment: '',
    responseTimeRating: 5,
    resolutionRating: 5,
    professionalismRating: 5
  });
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const token = localStorage.getItem('token');
      const [feedbacksRes, ticketsRes] = await Promise.all([
        axios.get('/api/service/feedback', { headers: { Authorization: `Bearer ${token}` } }),
        axios.get('/api/tickets?status=closed', { headers: { Authorization: `Bearer ${token}` } })
      ]);
      
      setFeedbacks(feedbacksRes.data);
      setTickets(ticketsRes.data);
    } catch (err) {
      console.error('Failed to fetch data', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateFeedback = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post('/api/service/feedback', feedbackForm, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      setFeedbacks([...feedbacks, response.data]);
      setShowFeedbackForm(false);
      setFeedbackForm({
        ticketId: '',
        rating: 5,
        comment: '',
        responseTimeRating: 5,
        resolutionRating: 5,
        professionalismRating: 5
      });
    } catch (err) {
      console.error('Failed to create feedback', err);
    }
  };

  const getRatingColor = (rating) => {
    if (rating >= 4) {
      return 'text-green-600';
    } else if (rating >= 3) {
      return 'text-yellow-600';
    } else {
      return 'text-red-600';
    }
  };

  const getRatingText = (rating) => {
    if (rating >= 4) {
      return 'Excellent';
    } else if (rating >= 3) {
      return 'Good';
    } else if (rating >= 2) {
      return 'Average';
    } else {
      return 'Poor';
    }
  };

  const getStarRating = (rating) => {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
  };

  const filteredFeedbacks = filter === 'all' 
    ? feedbacks 
    : feedbacks.filter(fb => {
        if (filter === 'satisfied') {
          return fb.satisfied;
        } else if (filter === 'unsatisfied') {
          return !fb.satisfied;
        }
        return fb.rating === parseInt(filter);
      });

  const stats = {
    total: feedbacks.length,
    satisfied: feedbacks.filter(fb => fb.satisfied).length,
    unsatisfied: feedbacks.filter(fb => !fb.satisfied).length,
    avgRating: feedbacks.length > 0 
      ? (feedbacks.reduce((sum, fb) => sum + fb.rating, 0) / feedbacks.length).toFixed(1)
      : 0
  };

  const ratingDistribution = [1, 2, 3, 4, 5].map(rating => ({
    rating,
    count: feedbacks.filter(fb => fb.rating === rating).length,
    percentage: feedbacks.length > 0 
      ? ((feedbacks.filter(fb => fb.rating === rating).length / feedbacks.length) * 100).toFixed(1)
      : 0
  }));

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
            <h1 className="text-3xl font-bold text-gray-800 mb-2">Customer Feedback</h1>
            <p className="text-gray-600">Collect and analyze customer satisfaction feedback</p>
          </div>
          <button
            onClick={() => setShowFeedbackForm(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg flex items-center"
          >
            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Add Feedback
          </button>
        </div>
      </div>

      {/* Feedback Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
        <div className="bg-white rounded-lg shadow p-4">
          <div className="text-gray-500 text-sm">Total Feedback</div>
          <div className="text-2xl font-bold text-gray-800">{stats.total}</div>
        </div>
        <div className="bg-white rounded-lg shadow p-4">
          <div className="text-gray-500 text-sm">Satisfied</div>
          <div className="text-2xl font-bold text-green-600">{stats.satisfied}</div>
          <div className="text-sm text-gray-500">
            {stats.total > 0 ? ((stats.satisfied / stats.total) * 100).toFixed(1) + '%' : '0%'}
          </div>
        </div>
        <div className="bg-white rounded-lg shadow p-4">
          <div className="text-gray-500 text-sm">Unsatisfied</div>
          <div className="text-2xl font-bold text-red-600">{stats.unsatisfied}</div>
          <div className="text-sm text-gray-500">
            {stats.total > 0 ? ((stats.unsatisfied / stats.total) * 100).toFixed(1) + '%' : '0%'}
          </div>
        </div>
        <div className="bg-white rounded-lg shadow p-4">
          <div className="text-gray-500 text-sm">Avg Rating</div>
          <div className="text-2xl font-bold text-purple-600">{stats.avgRating}</div>
          <div className="flex text-yellow-500">
            {getStarRating(Math.round(stats.avgRating))}
          </div>
        </div>
      </div>

      {/* Add Feedback Form Modal */}
      {showFeedbackForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl mx-4">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">Add Customer Feedback</h2>
            </div>
            <form onSubmit={handleCreateFeedback} className="px-6 py-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Ticket</label>
                  <select
                    value={feedbackForm.ticketId}
                    onChange={(e) => setFeedbackForm({...feedbackForm, ticketId: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value="">Select a closed ticket</option>
                    {tickets.map(ticket => (
                      <option key={ticket.ticketId} value={ticket.ticketId}>
                        #{ticket.ticketId} - {ticket.subject}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Overall Rating</label>
                  <div className="flex items-center">
                    {[1, 2, 3, 4, 5].map(star => (
                      <button
                        key={star}
                        type="button"
                        onClick={() => setFeedbackForm({...feedbackForm, rating: star})}
                        className={`text-2xl ${star <= feedbackForm.rating ? 'text-yellow-500' : 'text-gray-300'}`}
                      >
                        ★
                      </button>
                    ))}
                    <span className="ml-3 text-sm text-gray-500">
                      {getRatingText(feedbackForm.rating)}
                    </span>
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Response Time</label>
                  <div className="flex items-center">
                    {[1, 2, 3, 4, 5].map(star => (
                      <button
                        key={star}
                        type="button"
                        onClick={() => setFeedbackForm({...feedbackForm, responseTimeRating: star})}
                        className={`text-xl ${star <= feedbackForm.responseTimeRating ? 'text-yellow-500' : 'text-gray-300'}`}
                      >
                        ★
                      </button>
                    ))}
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Resolution Quality</label>
                  <div className="flex items-center">
                    {[1, 2, 3, 4, 5].map(star => (
                      <button
                        key={star}
                        type="button"
                        onClick={() => setFeedbackForm({...feedbackForm, resolutionRating: star})}
                        className={`text-xl ${star <= feedbackForm.resolutionRating ? 'text-yellow-500' : 'text-gray-300'}`}
                      >
                        ★
                      </button>
                    ))}
                  </div>
                </div>
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Professionalism</label>
                  <div className="flex items-center">
                    {[1, 2, 3, 4, 5].map(star => (
                      <button
                        key={star}
                        type="button"
                        onClick={() => setFeedbackForm({...feedbackForm, professionalismRating: star})}
                        className={`text-xl ${star <= feedbackForm.professionalismRating ? 'text-yellow-500' : 'text-gray-300'}`}
                      >
                        ★
                      </button>
                    ))}
                  </div>
                </div>
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Comments</label>
                  <textarea
                    value={feedbackForm.comment}
                    onChange={(e) => setFeedbackForm({...feedbackForm, comment: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    rows="3"
                    placeholder="Additional feedback..."
                  />
                </div>
              </div>
              <div className="mt-6 flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => setShowFeedbackForm(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  Submit Feedback
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
              { key: 'all', label: 'All Feedback', count: stats.total },
              { key: 'satisfied', label: 'Satisfied', count: stats.satisfied },
              { key: 'unsatisfied', label: 'Unsatisfied', count: stats.unsatisfied },
              { key: '5', label: '5 Stars', count: ratingDistribution[4].count },
              { key: '4', label: '4 Stars', count: ratingDistribution[3].count },
              { key: '3', label: '3 Stars', count: ratingDistribution[2].count },
              { key: '2', label: '2 Stars', count: ratingDistribution[1].count },
              { key: '1', label: '1 Star', count: ratingDistribution[0].count }
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

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Rating Distribution */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">Rating Distribution</h2>
            </div>
            <div className="p-6">
              {ratingDistribution.map(item => (
                <div key={item.rating} className="mb-4">
                  <div className="flex justify-between text-sm mb-1">
                    <span className="flex items-center">
                      <span className="text-yellow-500 mr-1">{getStarRating(item.rating)}</span>
                      <span className="font-medium">{item.rating} stars</span>
                    </span>
                    <span>{item.percentage}%</span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-yellow-500 h-2 rounded-full" 
                      style={{ width: `${item.percentage}%` }}
                    ></div>
                  </div>
                  <div className="text-right text-xs text-gray-500 mt-1">
                    {item.count} feedback{item.count !== 1 ? 's' : ''}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Feedback List */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg shadow">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ticket</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Rating</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Response Time</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Resolution</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Professionalism</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Comment</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {filteredFeedbacks.map(feedback => (
                    <tr key={feedback.feedbackId} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          #{tickets.find(t => t.ticketId === feedback.ticketId)?.ticketId || feedback.ticketId}
                        </div>
                        <div className="text-sm text-gray-500">
                          {tickets.find(t => t.ticketId === feedback.ticketId)?.subject || 'Unknown Ticket'}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className={`text-2xl ${getRatingColor(feedback.rating)}`}>
                          {getStarRating(feedback.rating)}
                        </div>
                        <div className="text-xs text-gray-500">
                          {getRatingText(feedback.rating)}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className={`text-lg ${getRatingColor(feedback.responseTimeRating)}`}>
                          {getStarRating(feedback.responseTimeRating)}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className={`text-lg ${getRatingColor(feedback.resolutionRating)}`}>
                          {getStarRating(feedback.resolutionRating)}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className={`text-lg ${getRatingColor(feedback.professionalismRating)}`}>
                          {getStarRating(feedback.professionalismRating)}
                        </div>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500 max-w-xs">
                        {feedback.comment && (
                          <div className="truncate max-w-xs" title={feedback.comment}>
                            {feedback.comment}
                          </div>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {new Date(feedback.submittedAt).toLocaleDateString()}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FeedbackSystem;
