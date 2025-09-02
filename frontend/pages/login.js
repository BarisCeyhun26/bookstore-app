import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuth } from '../contexts/AuthContext';
import LoginForm from '../components/LoginForm';
import Head from 'next/head';

export default function LoginPage() {
  const { isAuthenticated, loading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!loading && isAuthenticated) {
      // If user is already logged in, redirect to home or intended page
      const redirectTo = router.query.redirect || '/';
      router.push(redirectTo);
    }
  }, [isAuthenticated, loading, router]);

  // Show loading while checking authentication
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  // Don't render login form if user is already authenticated
  if (isAuthenticated) {
    return null;
  }

  return (
    <>
      <Head>
        <title>Sign In - Bookstore</title>
        <meta name="description" content="Sign in to your Bookstore account" />
        <meta name="format-detection" content="telephone=no" />
        <meta name="robots" content="noindex, nofollow" />
        <script
          dangerouslySetInnerHTML={{
            __html: `
              // Prevent all password manager suggestions and change prompts
              (function() {
                if (typeof window === 'undefined') return;
                
                function disablePasswordManagers() {
                  // Disable LastPass
                  if (window.lastpass) {
                    window.lastpass = null;
                  }
                  
                  // Disable 1Password
                  if (window._1password) {
                    window._1password = null;
                  }
                  
                  // Disable Bitwarden
                  if (window.bitwarden) {
                    window.bitwarden = null;
                  }
                  
                  // Disable Chrome password manager
                  const inputs = document.querySelectorAll('input[type="password"]');
                  inputs.forEach(input => {
                    input.setAttribute('autocomplete', 'off');
                    input.setAttribute('data-lpignore', 'true');
                    input.setAttribute('data-1p-ignore', 'true');
                    input.setAttribute('data-bwignore', 'true');
                  });
                }
                
                // Run immediately and on DOM ready
                disablePasswordManagers();
                if (document.readyState === 'loading') {
                  document.addEventListener('DOMContentLoaded', disablePasswordManagers);
                }
              })();
            `,
          }}
        />
      </Head>
      <LoginForm />
    </>
  );
}

