package dev.bayun.id.core.repository;

import dev.bayun.id.core.entity.account.Avatar;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Repository
public class AvatarRepository {

    @Setter
    private Path root;

    public AvatarRepository(@Value("${bayun.id.account.avatar.path}") String path) {
        this.root = Path.of(path);
    }

    public Avatar get(String id) {
        Avatar avatar = new Avatar();
        avatar.setId(id);

        try {
            Path avatarPath = this.root.resolve(id + ".png");
            if (Files.notExists(avatarPath)) {
                return null;
            }

            avatar.setBlob(Files.readAllBytes(avatarPath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return avatar;
    }

    public void save(Avatar avatar) {
        try {
            Path avatarPath = this.root.resolve(avatar.getId() + ".png");
            if (Files.exists(avatarPath)) {
                throw new RuntimeException("avatar with id=" + avatar.getId() + " already exists");
            }

            Files.createFile(avatarPath);
            Files.copy(new ByteArrayInputStream(avatar.getBlob()), avatarPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
