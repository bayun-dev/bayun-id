package dev.bayun.id.api.endpoint;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.modal.AccountUpdateToken;
import dev.bayun.id.core.service.AccountService;
import dev.bayun.id.core.service.AvatarService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class GetAvatarById {

    @Setter
    private AccountService accountService;

    @Setter
    private AvatarService avatarService;

    @GetMapping(path = "/avatar/{id}/{size}")
    public ResponseEntity<byte[]> handle(@PathVariable("id") String avatarId, @PathVariable String size)
            throws IOException {
        Avatar avatar = avatarService.get(UUID.fromString(avatarId));
        if (avatar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] body = switch (size.toLowerCase()) {
            case "small" -> avatar.getSmall();
            case "medium" -> avatar.getMedium();
            case "large" -> avatar.getLarge();
            default -> null;
        };

        if (body == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(body);
    }


}
