package dev.bayun.id.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionToken {

    private String ip;
    private long lastAccessedTime;
    private boolean isExpired;

}
