package dev.bayun.id.core.repository;

import dev.bayun.id.core.entity.account.Email;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailRepository extends CrudRepository<Email, UUID> {
}
