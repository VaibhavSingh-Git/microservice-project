package com.user.repositories;

import com.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositorie extends JpaRepository<User,String> {
}
