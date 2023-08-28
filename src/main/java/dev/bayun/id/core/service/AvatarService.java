package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.repository.AvatarRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AvatarService {

    @Setter
    private AvatarRepository avatarRepository;

    public Avatar get(UUID id) {
        return avatarRepository.get(id);
    }

    public Avatar save(byte[] origin) {
        Avatar avatar = new Avatar();
        avatar.setId(UUID.randomUUID());
        try {
            BufferedImage originImage = ImageIO.read(new ByteArrayInputStream(origin));
            avatar.setSmall(toBytes(scale(originImage, 64, 64)));
            avatar.setMedium(toBytes(scale(originImage, 128, 128)));
            avatar.setLarge(toBytes(scale(originImage, 256, 256)));
            avatarRepository.save(avatar);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return avatar;
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
