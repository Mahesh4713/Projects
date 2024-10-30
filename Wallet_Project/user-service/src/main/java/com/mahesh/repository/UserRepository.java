package com.mahesh.repository;

import com.mahesh.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPhone(String searchKey);

    User findByEmail(String searchValue);

    User findByName(String searchValue);

    class UserCacheRepository {
    }
}
