package com.mahesh.repository;

import com.mahesh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    class UserCacheRepository {
    }
}
