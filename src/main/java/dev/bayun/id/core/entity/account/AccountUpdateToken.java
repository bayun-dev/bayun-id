package dev.bayun.id.core.entity.account;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class AccountUpdateToken {

    private String firstName;

    private String lastName;

    private UUID emailId;

    private String password;

    private String avatarId;

    private boolean dropAvatar;

}
