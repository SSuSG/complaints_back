package com.toyproject.complaints.domain.user.repository;



import com.toyproject.complaints.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByUserEmail(String email);
    public boolean existsByRefreshToken(String refreshToken);
    public Optional<User> findByUserEmail(String userEmail);
    public boolean deleteByUserEmail(String userEmail);
    public Optional<User> findByUserEmailAndName(String userEmail , String name);
    public List<User> findByActive(boolean active);
}
