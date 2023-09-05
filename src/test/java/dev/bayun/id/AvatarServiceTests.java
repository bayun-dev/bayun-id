package dev.bayun.id;

import dev.bayun.id.core.repository.AvatarRepository;
import dev.bayun.id.core.service.AvatarService;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void test_crop() throws IOException {
        ClassPathResource resource = new ClassPathResource("avatar.jpg");
        assertNotNull(resource);

        BufferedImage image = ImageIO.read(resource.getInputStream());
        if (image.getWidth() == image.getHeight()) {
            return;
        }

        BufferedImage cropped;
        if (image.getHeight() < image.getWidth()) { // horizontal
            int side = image.getHeight();
            cropped = image.getSubimage((image.getWidth() - side) / 2,0,side,side);
        } else { // vertical
            int side = image.getWidth();
            cropped = image.getSubimage(0,(image.getHeight() - side) / 2,side,side);
        }
        Path path = Path.of("D:\\bayun\\projects\\bayun-id\\avatars");
        path = path.resolve(UUID.randomUUID().toString() + ".png");
        Files.createFile(path);
        ImageIO.write(cropped, "png", Files.newOutputStream(path));
    }

}
