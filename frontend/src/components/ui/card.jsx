import * as React from "react"

export function Card({ children, className = "", ...props }) {
  return (
    <div className={"rounded-2xl bg-white shadow-xl p-8 " + className} {...props}>
      {children}
    </div>
  )
}

export function CardHeader({ children, className = "", ...props }) {
  return (
    <div className={"text-center pb-2 " + className} {...props}>
      {children}
    </div>
  )
}

export function CardTitle({ children, className = "", ...props }) {
  return (
    <h2 className={"text-3xl font-bold text-slate-900 " + className} {...props}>
      {children}
    </h2>
  )
}

export function CardDescription({ children, className = "", ...props }) {
  return (
    <p className={"text-lg text-slate-600 mt-2 " + className} {...props}>
      {children}
    </p>
  )
}

export function CardContent({ children, className = "", ...props }) {
  return (
    <div className={"text-center " + className} {...props}>
      {children}
    </div>
  )
}
