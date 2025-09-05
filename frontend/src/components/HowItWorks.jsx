import { Building2, Users, Briefcase } from 'lucide-react'

const HowItWorks = () => {
  const steps = [
    {
      icon: Building2,
      title: "Sign Up & Add Organization",
      description: "Create your account and set up your organization profile with all the essential details."
    },
    {
      icon: Users,
      title: "Add Users & Leads",
      description: "Invite your team members, assign roles, and start importing your leads and contacts."
    },
    {
      icon: Briefcase,
      title: "Manage Deals & Grow",
      description: "Track your deals through the pipeline, manage activities, and watch your business grow."
    }
  ]

  return (
  <section id="how-it-works" className="py-20 bg-gray-50 scroll-mt-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
            How It Works
          </h2>
          <p className="text-xl text-gray-600 max-w-3xl mx-auto">
            Get started with VertexCRM in three simple steps
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-8">
          {steps.map((step, index) => {
            const IconComponent = step.icon
            return (
              <div key={index} className="text-center group">
                <div className="relative mb-8">
                  <div className="bg-blue-600 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
                    <IconComponent className="text-white" size={28} />
                  </div>
                  <div className="absolute top-8 left-1/2 transform -translate-x-1/2 w-8 h-8 bg-blue-600 text-white rounded-full flex items-center justify-center text-sm font-bold">
                    {index + 1}
                  </div>
                  {index < steps.length - 1 && (
                    <div className="hidden md:block absolute top-8 left-full w-full h-0.5 bg-blue-200 -translate-y-1/2"></div>
                  )}
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-3">
                  {step.title}
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  {step.description}
                </p>
              </div>
            )
          })}
        </div>
      </div>
    </section>
  )
}

export default HowItWorks