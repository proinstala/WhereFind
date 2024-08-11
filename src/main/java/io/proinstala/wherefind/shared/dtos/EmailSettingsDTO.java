package io.proinstala.wherefind.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class EmailSettingsDTO {
    private String smtpHost;
    private String smtpAuth;
    private String smtpStarttlsEnable;
    private String smtpPort;
    private String email;
    private String password;
}
