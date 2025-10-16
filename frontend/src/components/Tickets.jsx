import React, { useEffect, useState } from 'react';
import api from '../services/api';

export default function Tickets() {
  const [items, setItems] = useState([]);
  const [orgId, setOrgId] = useState(() => JSON.parse(localStorage.getItem('userInfo')||'{}')?.orgId || 1);

  useEffect(() => {
    api.get(`/tickets/org/${orgId}`).then(r => setItems(r.data)).catch(console.error);
  }, [orgId]);

  return (
    <div className="p-6">
      <h1 className="text-xl font-semibold mb-4">Tickets</h1>
      <ul className="space-y-2">
        {items.map(t => (
          <li key={t.ticketId} className="p-3 border rounded">
            <div className="font-medium">{t.subject}</div>
            <div className="text-sm text-gray-500">{t.status} • Priority: {t.priority} • Due: {t.dueAt || '-'}</div>
          </li>
        ))}
      </ul>
    </div>
  );
}
