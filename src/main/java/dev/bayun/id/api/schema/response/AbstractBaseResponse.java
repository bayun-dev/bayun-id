package dev.bayun.id.api.schema.response;

import dev.bayun.id.api.schema.BaseResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractBaseResponse implements BaseResponse {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private boolean ok;

    protected AbstractBaseResponse() {
        this(true);
    }

    protected AbstractBaseResponse(boolean ok) {
        setOk(ok);
    }




}
