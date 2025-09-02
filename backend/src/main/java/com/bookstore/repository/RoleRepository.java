package com.bookstore.repository;

import com.bookstore.entity.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {
    
    /**
     * Rol adına göre rol bulur
     */
    public Role findByName(String name) {
        return find("name", name).firstResult();
    }
}

