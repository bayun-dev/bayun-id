package dev.bayun.id.api.schema.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Data
@JsonSerialize
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeleteAccountsByIdResponse extends AbstractBaseResponse {

}
