package DTOs;

import lombok.Data;

@Data
public class UpdateAccountRequest {
    private String username;
    private String password;
} 