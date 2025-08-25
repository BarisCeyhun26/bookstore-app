#!/bin/bash

echo "🚀 Online Bookstore Application Başlatılıyor..."

# Docker Compose ile servisleri başlat
echo "📦 Docker servisleri başlatılıyor..."
docker-compose up -d

# Servislerin hazır olmasını bekle
echo "⏳ Servislerin hazır olması bekleniyor..."
sleep 30

# Servis durumlarını kontrol et
echo "🔍 Servis durumları kontrol ediliyor..."
docker-compose ps

echo ""
echo "✅ Uygulama başarıyla başlatıldı!"
echo ""
echo "🌐 Erişim URL'leri:"
echo "   Frontend: http://localhost:3000"
echo "   Backend API: http://localhost:8080"
echo "   PostgreSQL: localhost:5432"
echo ""
echo "📚 Veritabanı bilgileri:"
echo "   Database: bookstore"
echo "   Username: bookstore_user"
echo "   Password: bookstore_password"
echo ""
echo "🛑 Uygulamayı durdurmak için: docker-compose down"
