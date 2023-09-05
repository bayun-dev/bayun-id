package dev.bayun.id.core.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.session.Session;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ActivityHistoryService {

    @Setter
    private JdbcIndexedSessionRepository sessionRepository;

    public Collection<? extends Session> getByPrincipalName(String username) {
        return sessionRepository.findByPrincipalName(username).values();
    }

}
