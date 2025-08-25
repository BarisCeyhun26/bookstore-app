# 📚 Online Bookstore Application

Modern bir online kitap satış uygulaması. Backend (Node.js + PostgreSQL) ve Frontend (Next.js + Tailwind CSS) teknolojileri kullanılarak geliştirilmiştir.

## 🚀 Özellikler

- **Kitap Yönetimi**: Kitap listesi, arama, filtreleme
- **Yazar ve Tür Sistemi**: Kategorize edilmiş kitap organizasyonu
- **Modern UI/UX**: Responsive tasarım ve kullanıcı dostu arayüz
- **RESTful API**: Tam özellikli backend API
- **Veritabanı**: PostgreSQL ile normalize edilmiş veri yapısı

## 🛠️ Teknolojiler

### Backend
- **Node.js** - JavaScript runtime
- **Express.js** - Web framework
- **PostgreSQL** - Veritabanı
- **Docker** - Containerization

### Frontend
- **Next.js** - React framework
- **Tailwind CSS** - CSS framework
- **Zustand** - State management
- **Axios** - HTTP client

## 📁 Proje Yapısı

```
bookstore-app/
├── backend/          # Node.js API
├── frontend/         # Next.js uygulaması
├── database/         # Veritabanı şeması
├── docker-compose.yml # Docker konfigürasyonu
└── start.sh         # Başlatma scripti
```

## 🚀 Kurulum ve Çalıştırma

### Gereksinimler
- Docker ve Docker Compose
- Git

### Hızlı Başlangıç

1. **Repository'yi klonlayın:**
   ```bash
   git clone <repository-url>
   cd bookstore-app
   ```

2. **Uygulamayı başlatın:**
   ```bash
   chmod +x start.sh
   ./start.sh
   ```

3. **Tarayıcıda açın:**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080

### Manuel Kurulum

#### Backend
```bash
cd backend
npm install
npm start
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

## 🌐 API Endpoints

- `GET /api/books` - Tüm kitapları listele
- `GET /api/books/:id` - Kitap detayı
- `GET /api/books/search?q=query` - Kitap arama
- `GET /api/authors` - Yazar listesi
- `GET /api/genres` - Tür listesi

## 📊 Veritabanı Şeması

- **books**: Kitap bilgileri
- **authors**: Yazar bilgileri
- **genres**: Kitap türleri
- **publishers**: Yayınevi bilgileri
- **customers**: Müşteri bilgileri
- **orders**: Sipariş bilgileri

## 🐳 Docker

Proje Docker ile containerize edilmiştir:

```bash
# Servisleri başlat
docker-compose up -d

# Servisleri durdur
docker-compose down

# Logları görüntüle
docker-compose logs
```

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/AmazingFeature`)
3. Commit yapın (`git commit -m 'Add some AmazingFeature'`)
4. Push yapın (`git push origin feature/AmazingFeature`)
5. Pull Request oluşturun

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 👥 Geliştirici

Bookstore Team

---

⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!
