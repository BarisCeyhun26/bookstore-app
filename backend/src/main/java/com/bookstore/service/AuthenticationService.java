package com.bookstore.service;

import com.bookstore.entity.Customer;
import com.bookstore.entity.Role;
import com.bookstore.repository.CustomerRepository;
import com.bookstore.repository.RoleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthenticationService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    PasswordService passwordService;

    @Inject
    JwtService jwtService;

    /**
     * Kullanıcı girişi yapar
     */
    public LoginResponse login(String username, String password) {
        Customer customer = customerRepository.findByUsername(username);
        
        if (customer == null || !customer.isActive()) {
            throw new RuntimeException("Kullanıcı bulunamadı veya aktif değil");
        }

        if (!passwordService.verifyPassword(password, customer.getPasswordHash())) {
            throw new RuntimeException("Geçersiz şifre");
        }

        // Son giriş zamanını güncelle
        customer.setLastLogin(LocalDateTime.now());
        customerRepository.persist(customer);

        // Kullanıcı rollerini al
        Set<String> roles = customer.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        // JWT token'ları oluştur
        String accessToken = jwtService.generateAccessToken(
                customer.getUsername(), 
                customer.getEmail(), 
                roles
        );
        String refreshToken = jwtService.generateRefreshToken(customer.getUsername());

        return new LoginResponse(accessToken, refreshToken, customer);
    }

    /**
     * Kullanıcı kaydı yapar
     */
    @Transactional
    public Customer register(RegisterRequest request) {
        // Kullanıcı adı kontrolü
        if (customerRepository.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }

        // Email kontrolü
        if (customerRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Bu email adresi zaten kullanılıyor");
        }

        // Şifre güçlülüğü kontrolü
        if (!passwordService.isPasswordStrong(request.getPassword())) {
            throw new RuntimeException("Şifre en az 8 karakter olmalı ve büyük harf, küçük harf, sayı ve özel karakter içermelidir");
        }

        // Yeni kullanıcı oluştur
        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPasswordHash(passwordService.hashPassword(request.getPassword()));
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setActive(true);
        customer.setMember(false);
        customer.setMembershipDiscount(0.00);

        // Varsayılan USER rolünü ata
        Role userRole = roleRepository.findByName("USER");
        if (userRole != null) {
            customer.getRoles().add(userRole);
        }

        customerRepository.persist(customer);
        return customer;
    }

    /**
     * Refresh token ile yeni access token oluşturur
     */
    public String refreshToken(String refreshToken) {
        // Bu basit implementasyon - gerçek uygulamada refresh token'ı veritabanında saklamalısınız
        // Şimdilik sadece yeni access token döndürüyoruz
        return refreshToken; // Gerçek implementasyon gerekli
    }

    /**
     * Login response sınıfı
     */
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private Customer customer;

        public LoginResponse(String accessToken, String refreshToken, Customer customer) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.customer = customer;
        }

        // Getters
        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public Customer getCustomer() { return customer; }
    }

    /**
     * Register request sınıfı
     */
    public static class RegisterRequest {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String phone;
        private String address;

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }
}

