import * as React from "react";

// Simple rupee sign icon, styled like lucide icons
export function RupeeSign({ className = "w-6 h-6", ...props }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
      strokeWidth={2}
      className={className}
      {...props}
    >
      <path strokeLinecap="round" strokeLinejoin="round" d="M6 3h12M6 8h12M6 13h8a4 4 0 01-8 0V3m0 10v4m0 0h8" />
    </svg>
  );
}
