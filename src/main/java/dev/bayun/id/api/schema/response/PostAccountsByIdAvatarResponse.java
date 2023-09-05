package dev.bayun.id.api.schema.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostAccountsByIdAvatarResponse extends AbstractBaseResponse {

    private String avatarId;

}
