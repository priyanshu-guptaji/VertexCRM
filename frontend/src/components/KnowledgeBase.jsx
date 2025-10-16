import React, { useEffect, useState } from 'react';
import api from '../services/api';

export default function KnowledgeBase() {
  const [items, setItems] = useState([]);
  const [orgId, setOrgId] = useState(() => JSON.parse(localStorage.getItem('userInfo')||'{}')?.orgId || 1);

  useEffect(() => {
    api.get(`/kb/org/${orgId}`).then(r => setItems(r.data)).catch(console.error);
  }, [orgId]);

  return (
    <div className="p-6">
      <h1 className="text-xl font-semibold mb-4">Knowledge Base</h1>
      <ul className="space-y-2">
        {items.map(a => (
          <li key={a.articleId} className="p-3 border rounded">
            <div className="font-medium">{a.title}</div>
            <div className="text-sm text-gray-500">{a.category} • {a.visibility}</div>
          </li>
        ))}
      </ul>
    </div>
  );
}
