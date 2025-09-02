package com.bookstore.repository;

import com.bookstore.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {
    
    /**
     * Kullanıcı adına göre kullanıcı bulur
     */
    public Customer findByUsername(String username) {
        return find("username", username).firstResult();
    }
    
    /**
     * Email adresine göre kullanıcı bulur
     */
    public Customer findByEmail(String email) {
        return find("email", email).firstResult();
    }
    
    /**
     * Aktif kullanıcıları getirir
     */
    public Customer findActiveByUsername(String username) {
        return find("username = ?1 and active = true", username).firstResult();
    }
    
    /**
     * Kullanıcı adı veya email ile kullanıcı bulur
     */
    public Customer findByUsernameOrEmail(String usernameOrEmail) {
        return find("username = ?1 or email = ?1", usernameOrEmail).firstResult();
    }
}

