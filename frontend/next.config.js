 /** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    domains: ['localhost', 'images.moviesanywhere.com'],
  },
  // output: 'standalone',
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:8080/api/:path*',
      },
    ];
  },
};

module.exports = nextConfig;
