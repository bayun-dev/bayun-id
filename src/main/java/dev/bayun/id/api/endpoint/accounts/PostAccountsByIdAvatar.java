package dev.bayun.id.api.endpoint.accounts;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.modal.AccountUpdateToken;
import dev.bayun.id.core.service.AccountService;
import dev.bayun.id.core.service.AvatarService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class PostAccountsByIdAvatar {

    @Setter
    private AccountService accountService;

    @Setter
    private AvatarService avatarService;

    @PostMapping(path = "/api/accounts/{id}/avatar",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> handle(@RequestParam("avatar") MultipartFile file, @PathVariable("id") String accountId,
                                       Authentication authentication) throws IOException {
        Account account;
        try {
            if (accountId.equalsIgnoreCase("me")) {
                String username = authentication.getName();
                account = accountService.loadUserByUsername(username);
            } else {
                account = accountService.loadUserById(UUID.fromString(accountId));
            }
        } catch (AccountNotFoundException | UsernameNotFoundException | IllegalArgumentException exception) {
            throw new AccountNotFoundException(Errors.ACCOUNT_NOT_FOUND_CODE);
        }

        Avatar avatar = avatarService.save(file.getBytes());

        AccountUpdateToken token = new AccountUpdateToken();
        token.setAvatarId(avatar.getId());

        accountService.update(account.getId(), token);

        return ResponseEntity.ok(null);
    }


}
