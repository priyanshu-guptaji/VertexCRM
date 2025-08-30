import { ArrowRight, Play } from 'lucide-react'
import { Link } from 'react-router-dom';

const Hero = () => {
  return (
    <section id="home" className="pt-20 pb-16 bg-gradient-to-br from-blue-50 via-white to-orange-50 min-h-screen flex items-center">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid lg:grid-cols-2 gap-12 items-center">
          <div>
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-gray-900 leading-tight">
              Streamline Your Business with{' '}
              <span className="text-blue-600">VertexCRM</span>
            </h1>
            <p className="text-xl text-gray-600 mt-6 leading-relaxed">
              From leads to deals, manage everything in one place. Boost your sales team's productivity and grow your business faster than ever.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 mt-8">
              <Link to="/signup" className="bg-blue-600 text-white px-8 py-4 rounded-lg hover:bg-blue-700 transition-all duration-200 font-semibold text-lg flex items-center justify-center group">
                Get Started
                <ArrowRight className="ml-2 group-hover:translate-x-1 transition-transform" size={20} />
              </Link>
              <button className="border-2 border-blue-600 text-blue-600 px-8 py-4 rounded-lg hover:bg-blue-600 hover:text-white transition-all duration-200 font-semibold text-lg flex items-center justify-center">
                <Play className="mr-2" size={20} />
                View Demo
              </button>
            </div>
          </div>
          <div className="relative">
            <div className="bg-white rounded-2xl shadow-2xl p-8 transform rotate-1 hover:rotate-0 transition-transform duration-300">
              <div className="bg-gradient-to-r from-blue-600 to-blue-700 rounded-lg p-6 text-white">
                <h3 className="text-xl font-semibold mb-4">Dashboard Overview</h3>
                <div className="space-y-3">
                  <div className="flex justify-between items-center">
                    <span>Active Deals</span>
                    <span className="bg-white/20 px-2 py-1 rounded">127</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span>New Leads</span>
                    <span className="bg-white/20 px-2 py-1 rounded">43</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span>Revenue</span>
                    <span className="bg-white/20 px-2 py-1 rounded">₹2.0Cr</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}

export default Hero