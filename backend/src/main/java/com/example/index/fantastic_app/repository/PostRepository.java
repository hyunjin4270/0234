package com.example.index.fantastic_app.repository;

import com.example.index.fantastic_app.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
