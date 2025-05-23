package Controller;

import DTOs.AuthRequest;
import DTOs.AuthResponse;
import DTOs.RegisterRequest;
import DTOs.UpdateAccountRequest;
import Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PutMapping("/set-admin/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> setAdminRole(@PathVariable Long userId) {
        authService.setAdminRole(userId);
        return ResponseEntity.ok("Đã set quyền ADMIN thành công");
    }

    @PutMapping("/account/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @authService.isCurrentUser(#id)")
    public ResponseEntity<String> updateAccount(
            @PathVariable Long id,
            @RequestBody UpdateAccountRequest request) {
        authService.updateAccount(id, request);
        return ResponseEntity.ok("Cập nhật tài khoản thành công");
    }
}