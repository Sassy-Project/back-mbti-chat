package com.projectsassy.sassy.user.repository;

import com.projectsassy.sassy.user.domain.Email;
import com.projectsassy.sassy.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(Email email);

    Optional<User> findByEmailAndLoginId(Email email, String loginId);
}
