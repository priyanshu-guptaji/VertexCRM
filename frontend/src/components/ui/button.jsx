import * as React from "react"

export function Button({ className = "", ...props }) {
  return (
    <button
      className={
        "w-full py-3 px-4 rounded-lg bg-gradient-to-r from-blue-600 to-indigo-500 text-white font-semibold text-lg shadow hover:from-blue-700 hover:to-indigo-600 transition-all duration-200 " +
        className
      }
      {...props}
    />
  )
}
