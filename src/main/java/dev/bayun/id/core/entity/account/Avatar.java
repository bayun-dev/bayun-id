package dev.bayun.id.core.entity.account;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.UUID;

@Data
public class Avatar {

    private UUID id;

    private byte[] small; // 42x42

    private byte[] medium; // 56x56

    private byte[] large; // 84x84

}
