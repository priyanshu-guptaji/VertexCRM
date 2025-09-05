import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { BarChart3 } from 'lucide-react';

const PlaceholderPage = ({ title, description }) => (
  <div className="flex items-center justify-center h-96">
    <Card className="w-full max-w-md text-center border-0 shadow-lg">
      <CardHeader>
        <CardTitle className="text-2xl font-bold text-gray-900">{title}</CardTitle>
        <CardDescription className="text-gray-600">{description}</CardDescription>
      </CardHeader>
      <CardContent>
        <div className="w-16 h-16 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-xl flex items-center justify-center mx-auto mb-4">
          <BarChart3 className="w-8 h-8 text-white" />
        </div>
        <p className="text-gray-600">This feature is coming soon!</p>
      </CardContent>
    </Card>
  </div>
);

export default PlaceholderPage;