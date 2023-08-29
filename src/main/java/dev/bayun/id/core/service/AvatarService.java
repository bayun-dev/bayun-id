package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.repository.AvatarRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AvatarService {

    @Setter
    private AvatarRepository avatarRepository;

    @PostConstruct
    public void checkDefaultAvatar() throws IOException {
        Avatar avatar = avatarRepository.get("default");
        if (avatar == null) {
            ClassPathResource defaultAvatarResource = new ClassPathResource("avatar/default.png");
            saveWithId(defaultAvatarResource.getContentAsByteArray(), "default");
        }
    }

    public Avatar get(String id) {
        return avatarRepository.get(id);
    }

    public Avatar save(byte[] origin) {
        return saveWithId(origin, UUID.randomUUID().toString());
    }

    private Avatar saveWithId(byte[] origin, String id) {
        Avatar avatar = new Avatar();
        avatar.setId(id);
        try {
            BufferedImage originImage = ImageIO.read(new ByteArrayInputStream(origin));
            BufferedImage croppedOriginImage = cropToSquare(originImage);
            avatar.setSmall(toBytes(scale(croppedOriginImage, 64, 64)));
            avatar.setMedium(toBytes(scale(croppedOriginImage, 128, 128)));
            avatar.setLarge(toBytes(scale(croppedOriginImage, 256, 256)));
            avatarRepository.save(avatar);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return avatar;
    }

    private BufferedImage cropToSquare(BufferedImage origin) {
        if (origin.getWidth() == origin.getHeight()) {
            return origin;
        }

        BufferedImage cropped;
        if (origin.getHeight() < origin.getWidth()) { // horizontal
            int side = origin.getHeight();
            cropped = origin.getSubimage((origin.getWidth() - side) / 2,0,side,side);
        } else { // vertical
            int side = origin.getWidth();
            cropped = origin.getSubimage(0,(origin.getHeight() - side) / 2,side,side);
        }

        return cropped;
    }

    private BufferedImage scale(BufferedImage origin, int width, int height) {
        Image resultingImage = origin.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private byte[] toBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();
    }
}
