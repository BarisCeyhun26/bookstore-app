# 📚 Online Bookstore Application

Modern bir online kitap satış uygulaması. Backend (Quarkus + PostgreSQL) ve Frontend (Next.js + Tailwind CSS) teknolojileri kullanılarak geliştirilmiştir.

## 🔐 Authentication System

- **User Registration & Login** - Complete authentication system
- **JWT Tokens** - Secure token-based authentication
- **Role-Based Access Control** - USER, ADMIN, MODERATOR roles
- **Password Security** - BCrypt hashing

## 🚀 Özellikler

- **Kitap Yönetimi**: Kitap listesi, arama, filtreleme
- **Yazar ve Tür Sistemi**: Kategorize edilmiş kitap organizasyonu
- **Modern UI/UX**: Responsive tasarım ve kullanıcı dostu arayüz
- **RESTful API**: Tam özellikli backend API
- **Veritabanı**: PostgreSQL ile normalize edilmiş veri yapısı

## 🛠️ Teknolojiler

### Backend
- **Quarkus** - Supersonic Subatomic Java framework
- **Java 17** - Modern Java runtime
- **Hibernate ORM** - Object-relational mapping
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
├── backend/          # Quarkus Java API
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
   git clone https://github.com/YOUR_USERNAME/bookstore-app.git
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
   - Swagger UI: http://localhost:8080/swagger-ui

4. **Test Kullanıcısı:**
   - Username: `admin`
   - Password: `Admin123!`

### Manuel Kurulum

#### Backend
```bash
cd backend
./mvnw quarkus:dev
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

## 🌐 API Endpoints

### Public Endpoints
- `GET /api/books` - Tüm kitapları listele
- `GET /api/books/:id` - Kitap detayı
- `GET /api/books/search?q=query` - Kitap arama
- `GET /api/authors` - Yazar listesi
- `GET /api/genres` - Tür listesi

### Authentication Endpoints
- `POST /auth/register` - Kullanıcı kaydı
- `POST /auth/login` - Kullanıcı girişi
- `POST /auth/refresh` - Token yenileme

## 📊 Veritabanı Şeması

- **books**: Kitap bilgileri
- **authors**: Yazar bilgileri
- **genres**: Kitap türleri
- **publishers**: Yayınevi bilgileri
- **customers**: Müşteri bilgileri (authentication)
- **roles**: Kullanıcı rolleri (USER, ADMIN, MODERATOR)
- **user_roles**: Kullanıcı-rol ilişkileri
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

## 🔧 Geliştirme

### Backend Geliştirme
```bash
cd backend
./mvnw quarkus:dev
```

### API Dokümantasyonu
- **Swagger UI**: http://localhost:8080/swagger-ui
- **OpenAPI**: http://localhost:8080/openapi

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
