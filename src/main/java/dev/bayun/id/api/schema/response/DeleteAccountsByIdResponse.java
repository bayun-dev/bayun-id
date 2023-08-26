package dev.bayun.id.api.schema.response;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.bayun.id.core.entity.account.Account;
import lombok.*;

@Data
@JsonSerialize
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeleteAccountsByIdResponse extends AbstractResponse {

}
