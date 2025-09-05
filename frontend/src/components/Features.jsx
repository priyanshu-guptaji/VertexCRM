import { featuresData } from '../data/featuresData.js'

const Features = () => {
  return (
    <section id="features" className="py-20 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
            Everything You Need to Manage Your Business
          </h2>
          <p className="text-xl text-gray-600 max-w-3xl mx-auto">
            Built from the ground up with all the essential CRM features your team needs to succeed
          </p>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {featuresData.map((feature, index) => {
            const IconComponent = feature.icon
            return (
              <div 
                key={index} 
                className="bg-gray-50 p-8 rounded-xl hover:shadow-lg transition-all duration-300 group hover:-translate-y-1"
              >
                <div className="bg-blue-100 w-12 h-12 rounded-lg flex items-center justify-center mb-6 group-hover:bg-blue-600 transition-colors duration-300">
                  <IconComponent className="text-blue-600 group-hover:text-white transition-colors duration-300" size={24} />
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-3">
                  {feature.title}
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  {feature.description}
                </p>
              </div>
            )
          })}
        </div>
      </div>
    </section>
  )
}

export default Features