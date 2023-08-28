package dev.bayun.id;

import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.repository.AvatarRepository;
import dev.bayun.id.core.service.AvatarService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.UUID;

public class AvatarServiceTests {

    private final AvatarRepository avatarRepository = new AvatarRepository("D:\\bayun\\projects\\bayun-id\\avatars");
    private final AvatarService avatarService = new AvatarService(avatarRepository);
//    {
//        avatarRepository.setRoot();
//    }

    @Test
    public void test_save() throws IOException {
        ClassPathResource resource = new ClassPathResource("avatar.jpg");
        byte[] origin = resource.getInputStream().readAllBytes();

        avatarService.save(origin);


//        Avatar avatar = new Avatar();
//        avatar.setId(UUID.randomUUID());
//        avatar.setSmall(origin);
//        avatar.setMedium(origin);
//        avatar.setLarge(origin);
//        avatarRepository.save(avatar);
    }

}
