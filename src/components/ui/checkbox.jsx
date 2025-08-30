import * as React from "react"

export function Checkbox(props) {
  return (
    <input
      type="checkbox"
      className="form-checkbox h-4 w-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500"
      {...props}
    />
  )
}
