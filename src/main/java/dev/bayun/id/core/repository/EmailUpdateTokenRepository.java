package dev.bayun.id.core.repository;

import dev.bayun.id.core.entity.account.EmailUpdateToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailUpdateTokenRepository extends CrudRepository<EmailUpdateToken, UUID> {

    void deleteAllByAccountId(UUID accountId);

}
