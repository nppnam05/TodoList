import { Link } from 'react-router-dom';
import Button from '../components/common/Button';
import Header from '../components/layout/Header';

export default function NotFoundPage() {
  return (
    <>
      <Header />
      <main className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
        <div className="text-center max-w-md">
          <h1 className="text-6xl md:text-8xl font-bold text-gray-900 mb-4">404</h1>
          <h2 className="text-2xl md:text-3xl font-bold text-gray-900 mb-2">
            Page Not Found
          </h2>
          <p className="text-gray-600 text-base md:text-lg mb-8">
            The page you're looking for doesn't exist or has been moved.
          </p>
          <Link to="/">
            <Button size="lg">← Back to Home</Button>
          </Link>
        </div>
      </main>
    </>
  );
}