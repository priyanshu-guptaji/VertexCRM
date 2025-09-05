import * as React from "react"

export function Label({ children, className = "", ...props }) {
  return (
    <label className={"block mb-2 font-semibold text-gray-800 " + className} {...props}>
      {children}
    </label>
  )
}
