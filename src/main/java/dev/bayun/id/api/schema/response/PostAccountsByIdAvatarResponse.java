package dev.bayun.id.api.schema.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostAccountsByIdAvatarResponse extends AbstractResponse {

    private String avatarId;

}
