package com.blinov.kafka.repo;

import com.blinov.kafka.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {


}
