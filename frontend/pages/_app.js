import '../styles/globals.css';
import { AuthProvider } from '../contexts/AuthContext';
import Head from 'next/head';
import { useEffect } from 'react';

export default function MyApp({ Component, pageProps }) {
  console.log('MyApp component render edildi');
  
  useEffect(() => {
    console.log('useEffect çalıştı - MyApp');
    
    // Tüm input'ları bul ve color'ı ayarla
    const setInputColors = () => {
      console.log('setInputColors çalıştı');
      const inputs = document.querySelectorAll('input');
      console.log('Bulunan input sayısı:', inputs.length);
      
      inputs.forEach((input, index) => {
        console.log(`Input ${index}:`, input);
        console.log(`Type:`, input.type);
        console.log(`Placeholder:`, input.placeholder);
        console.log(`Color:`, getComputedStyle(input).color);
        
        // Color'ı değiştir
        input.style.color = '#000000';
        input.style.setProperty('color', '#000000', 'important');
        input.style.setProperty('-webkit-text-fill-color', '#000000', 'important');
        
        console.log(`Yeni color:`, getComputedStyle(input).color);
      });
    };
    
    // Sayfa yüklendiğinde çalıştır
    setTimeout(setInputColors, 1000);
    
    // Her 1 saniyede bir kontrol et (input'lar dinamik olarak eklenebilir)
    const interval = setInterval(setInputColors, 1000);
    
    return () => clearInterval(interval);
  }, []);

  return (
    <>
      <Head>
        <style dangerouslySetInnerHTML={{
          __html: `
            /* En güçlü CSS kuralı - tüm input'lar için */
            input, input[type="text"], input[type="email"], input[type="password"] {
              color: #000000 !important;
              background-color: #ffffff !important;
              -webkit-text-fill-color: #000000 !important;
              -webkit-background-color: #ffffff !important;
              opacity: 1 !important;
            }
            
            /* Daha spesifik kurallar */
            form input[type="text"] {
              color: #000000 !important;
              -webkit-text-fill-color: #000000 !important;
            }
            
            div input[type="text"] {
              color: #000000 !important;
              -webkit-text-fill-color: #000000 !important;
            }
            
            /* En güçlü kural - ID ile */
            #search-input {
              color: #000000 !important;
              -webkit-text-fill-color: #000000 !important;
            }
            
            /* Body'den inherit edilen color'ı override et */
            body input, body input[type="text"] {
              color: #000000 !important;
              -webkit-text-fill-color: #000000 !important;
            }
            
            /* Dark mode override */
            @media (prefers-color-scheme: dark) {
              input, input[type="text"], input[type="email"], input[type="password"] {
                color: #000000 !important;
                background-color: #ffffff !important;
                -webkit-text-fill-color: #000000 !important;
                -webkit-background-color: #ffffff !important;
                opacity: 1 !important;
              }
              
              body input, body input[type="text"] {
                color: #000000 !important;
                -webkit-text-fill-color: #000000 !important;
              }
            }
          `
        }} />
      </Head>
      <AuthProvider>
        <Component {...pageProps} />
      </AuthProvider>
    </>
  );
}
