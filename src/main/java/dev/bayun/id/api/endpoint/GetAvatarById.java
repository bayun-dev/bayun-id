package dev.bayun.id.api.endpoint;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.response.ErrorResponse;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.exception.AvatarNotFoundException;
import dev.bayun.id.core.modal.AccountUpdateToken;
import dev.bayun.id.core.service.AccountService;
import dev.bayun.id.core.service.AvatarService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class GetAvatarById {

    @Setter
    private AvatarService avatarService;

    @GetMapping(path = "/avatar/{id}/{size}",
                produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] handle(@PathVariable("id") String avatarId, @PathVariable String size) {
        try {
            Avatar avatar = avatarService.get(avatarId);
            if (avatar == null) {
                throw new AvatarNotFoundException("with id=" +avatarId);
            }

            byte[] body = switch (size.toLowerCase()) {
                case "small" -> avatar.getSmall();
                case "medium" -> avatar.getMedium();
                case "large" -> avatar.getLarge();
                default -> null;
            };

            if (body == null) {
                throw new AvatarNotFoundException("with id=" +avatarId+" and with size="+size);
            }

            return body;
        } catch (Exception e) {
            throw new AvatarNotFoundException("with id=" + Objects.requireNonNullElse(avatarId, "null")
                    +" and with size="+Objects.requireNonNullElse(size, "null"), e);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AvatarNotFoundException.class)
    public void avatarNotFoundExceptionHandle(AvatarNotFoundException e) {
        log.warn(e.getMessage(), e);
    }
}
