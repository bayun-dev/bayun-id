package dev.bayun.id.api.schema.response;

import com.fasterxml.jackson.annotation.JsonValue;
import dev.bayun.id.core.entity.account.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GetAccountsByIdResponse extends AbstractBaseResponse {

    @JsonValue
    private Account account;

    public GetAccountsByIdResponse(Account account) {
        this.account = account;
    }

}
