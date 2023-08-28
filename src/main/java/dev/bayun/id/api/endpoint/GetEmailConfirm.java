package dev.bayun.id.api.endpoint;

import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GetEmailConfirm {

    @Setter
    private AccountService accountService;

    @Value("${server.host}")
    private String serverHost;

    public GetEmailConfirm(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/email-confirm")
    public ResponseEntity<Void> handle(@RequestParam String hash, Authentication authentication) {
        Account account = accountService.loadUserByUsername(authentication.getName());
        accountService.emailConfirm(account.getId(), hash);

        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create(serverHost)).body(null);
    }

}
