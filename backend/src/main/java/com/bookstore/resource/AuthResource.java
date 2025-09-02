package com.bookstore.resource;

import com.bookstore.service.AuthenticationService;
import com.bookstore.service.AuthenticationService.LoginResponse;
import com.bookstore.service.AuthenticationService.RegisterRequest;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "Kullanıcı girişi ve kaydı işlemleri")
public class AuthResource {

    @Inject
    AuthenticationService authenticationService;

    @POST
    @Path("/login")
    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı ve şifre ile giriş yapar")
    public Response login(LoginRequest request) {
        try {
            LoginResponse response = authenticationService.login(request.getUsername(), request.getPassword());
            return Response.ok(response).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/register")
    @Operation(summary = "Kullanıcı kaydı", description = "Yeni kullanıcı kaydı oluşturur")
    public Response register(RegisterRequest request) {
        try {
            var customer = authenticationService.register(request);
            return Response.status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Kullanıcı başarıyla oluşturuldu", customer))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/refresh")
    @Operation(summary = "Token yenileme", description = "Refresh token ile yeni access token oluşturur")
    public Response refreshToken(RefreshTokenRequest request) {
        try {
            String newAccessToken = authenticationService.refreshToken(request.getRefreshToken());
            return Response.ok(new TokenResponse(newAccessToken)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // Request/Response sınıfları
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    public static class TokenResponse {
        private String accessToken;

        public TokenResponse(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
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

