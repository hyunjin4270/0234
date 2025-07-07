package com.example.index.fantastic_app.repository;

import com.example.index.fantastic_app.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
//JPA로 DB접근
public interface PostRepository extends JpaRepository<Post, Integer> {
}
