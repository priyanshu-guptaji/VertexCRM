import React, { useEffect, useState } from 'react';
import api from '../services/api';

export default function Campaigns() {
  const [items, setItems] = useState([]);
  const [orgId, setOrgId] = useState(() => JSON.parse(localStorage.getItem('userInfo')||'{}')?.orgId || 1);

  useEffect(() => {
    api.get(`/campaigns/org/${orgId}`).then(r => setItems(r.data)).catch(console.error);
  }, [orgId]);

  return (
    <div className="p-6">
      <h1 className="text-xl font-semibold mb-4">Campaigns</h1>
      <ul className="space-y-2">
        {items.map(c => (
          <li key={c.campaignId} className="p-3 border rounded">
            <div className="font-medium">{c.name}</div>
            <div className="text-sm text-gray-500">{c.type} • {c.status}</div>
          </li>
        ))}
      </ul>
    </div>
  );
}
