package dev.bayun.id.ui;

import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.service.AvatarService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class Endpoints {

    @Setter
    private AvatarService avatarService;

    @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String me() {
        return "index";
    }

    @GetMapping(path = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return "login";
    }

    @GetMapping(path = "/reset-password", produces = MediaType.TEXT_HTML_VALUE)
    public String resetPassword() {
        return "reset-password";
    }

    @GetMapping(path = "/signup", produces = MediaType.TEXT_HTML_VALUE)
    public String signup() {
        return "signup";
    }

    @GetMapping(path = "/widget", produces = MediaType.TEXT_HTML_VALUE)
    public String widget() {

        return "widget";
    }

    @ResponseBody
    @GetMapping(path = "/avatar/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] avatarById(@PathVariable("id") String avatarId) {
        Avatar avatar = Objects.requireNonNullElse(avatarService.get(avatarId), avatarService.getDefaultAvatar());
        return avatar.getBlob();
    }

}
