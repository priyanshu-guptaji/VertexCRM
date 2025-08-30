import * as React from "react"

export function Input({ className = "", ...props }) {
  return (
    <input
      className={
        "w-full px-4 py-3 rounded border border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 text-base transition-all duration-150 " +
        className
      }
      {...props}
    />
  )
}
