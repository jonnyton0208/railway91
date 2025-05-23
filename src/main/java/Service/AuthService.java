package Service;

import DTOs.AuthRequest;
import DTOs.AuthResponse;
import DTOs.RegisterRequest;
import DTOs.UpdateAccountRequest;
import Entity.Account;
import Entity.Role;
import Repository.AccountRepository;
import Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegisterRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole(accountRepository.count() == 0 ? Role.ADMIN : Role.USER);
        accountRepository.save(account);
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token);
    }

    @Transactional
    public void setAdminRole(Long userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với ID: " + userId));
        account.setRole(Role.ADMIN);
        accountRepository.save(account);
    }

    @Transactional
    public void updateAccount(Long id, UpdateAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với ID: " + id));

        // Kiểm tra nếu username mới đã tồn tại và khác với username hiện tại
        if (!account.getUsername().equals(request.getUsername()) &&
            accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        account.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        accountRepository.save(account);
    }

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        String username = authentication.getName();
        return accountRepository.findByUsername(username)
                .map(account -> account.getId().equals(userId))
                .orElse(false);
    }
}
