package dev.bayun.id.core.repository;

import dev.bayun.id.core.entity.account.Avatar;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Repository
public class AvatarRepository {

    @Setter
    private Path root;

    public AvatarRepository(@Value("${bayun.id.account.avatar.path}") String path) {
        this.root = Path.of(path);
    }

    public Avatar get(UUID id) {
        Avatar avatar = new Avatar();
        avatar.setId(id);

        try {
            Path accountPath = this.root.resolve(id.toString());
            if (Files.notExists(accountPath)) {
                return null;
            }

            avatar.setSmall(get(accountPath, "small.png"));
            avatar.setMedium(get(accountPath, "medium.png"));
            avatar.setLarge(get(accountPath, "large.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return avatar;
    }

    private byte[] get(Path accountPath, String file) throws IOException {
        Path filePath = accountPath.resolve(file);
        if (Files.notExists(filePath)) {
            return null;
        }

        return Files.readAllBytes(filePath);
    }

    public void save(Avatar avatar) {
        try {
            Path avatarPath = this.root.resolve(avatar.getId().toString());
            save(avatarPath, "small.png", avatar.getSmall());
            save(avatarPath, "medium.png", avatar.getMedium());
            save(avatarPath, "large.png", avatar.getLarge());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void save(Path root, String fileName, byte[] bytes) throws IOException {
        if (Files.notExists(root)) {
            Files.createDirectories(root);
        }

        Path filePath = root.resolve(fileName);
        Files.createFile(filePath);
        Files.copy(new ByteArrayInputStream(bytes), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

}
