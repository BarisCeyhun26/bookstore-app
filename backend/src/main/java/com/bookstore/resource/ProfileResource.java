package com.bookstore.resource;

import com.bookstore.entity.Customer;
import com.bookstore.repository.CustomerRepository;
import com.bookstore.service.PasswordService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Profile", description = "Kullanıcı profil işlemleri")
public class ProfileResource {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    PasswordService passwordService;

    @GET
    @Operation(summary = "Profil bilgilerini getir", description = "Kullanıcının profil bilgilerini döndürür")
    public Response getProfile(@QueryParam("username") String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Username is required"))
                        .build();
            }

            Customer customer = customerRepository.findByUsername(username);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("User not found"))
                        .build();
            }

            return Response.ok(new ProfileResponse(customer)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error retrieving profile: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Operation(summary = "Profil bilgilerini güncelle", description = "Kullanıcının profil bilgilerini günceller")
    public Response updateProfile(UpdateProfileRequest request) {
        try {
            // For now, we'll use a hardcoded username since we don't have authentication setup
            // In a real application, this would come from the JWT token or session
            String username = "testuser"; // This should be extracted from the authenticated user
            
            Customer customer = customerRepository.findByUsername(username);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("User not found"))
                        .build();
            }

            // Update profile information
            if (request.getFirstName() != null) {
                customer.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                customer.setLastName(request.getLastName());
            }
            if (request.getEmail() != null) {
                // Check if email is already taken by another user
                Customer existingCustomer = customerRepository.findByEmail(request.getEmail());
                if (existingCustomer != null && !existingCustomer.getId().equals(customer.getId())) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ErrorResponse("Email already taken"))
                            .build();
                }
                customer.setEmail(request.getEmail());
            }
            if (request.getPhone() != null) {
                customer.setPhone(request.getPhone());
            }
            if (request.getAddress() != null) {
                customer.setAddress(request.getAddress());
            }

            customerRepository.persist(customer);

            return Response.ok(new SuccessResponse("Profile updated successfully", new ProfileResponse(customer))).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error updating profile: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/change-password")
    @Operation(summary = "Şifre değiştir", description = "Kullanıcının şifresini değiştirir")
    public Response changePassword(ChangePasswordRequest request) {
        try {
            // For now, we'll use a hardcoded username since we don't have authentication setup
            // In a real application, this would come from the JWT token or session
            String username = "testuser"; // This should be extracted from the authenticated user
            
            Customer customer = customerRepository.findByUsername(username);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("User not found"))
                        .build();
            }

            // Verify current password
            if (!passwordService.verifyPassword(request.getOldPassword(), customer.getPasswordHash())) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Current password is incorrect"))
                        .build();
            }

            // Validate new password
            if (!passwordService.isPasswordStrong(request.getNewPassword())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("New password must be at least 8 characters and contain uppercase, lowercase, number and special character"))
                        .build();
            }

            // Update password
            customer.setPasswordHash(passwordService.hashPassword(request.getNewPassword()));
            customerRepository.persist(customer);

            return Response.ok(new SuccessResponse("Password changed successfully", null)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error changing password: " + e.getMessage()))
                    .build();
        }
    }

    // Request/Response classes
    public static class ProfileResponse {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private Boolean active;
        private Boolean member;
        private Double membershipDiscount;
        private String lastLogin;
        private String createdAt;

        public ProfileResponse(Customer customer) {
            this.id = customer.getId();
            this.username = customer.getUsername();
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
            this.email = customer.getEmail();
            this.phone = customer.getPhone();
            this.address = customer.getAddress();
            this.active = customer.isActive();
            this.member = customer.isMember();
            this.membershipDiscount = customer.getMembershipDiscount();
            this.lastLogin = customer.getLastLogin() != null ? customer.getLastLogin().toString() : null;
            this.createdAt = customer.getCreatedAt() != null ? customer.getCreatedAt().toString() : null;
        }

        // Getters
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public Boolean getActive() { return active; }
        public Boolean getMember() { return member; }
        public Double getMembershipDiscount() { return membershipDiscount; }
        public String getLastLogin() { return lastLogin; }
        public String getCreatedAt() { return createdAt; }
    }

    public static class UpdateProfileRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;

        // Getters and Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        // Getters and Setters
        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    public static class SuccessResponse {
        private String message;
        private Object data;

        public SuccessResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}

