package com.fc.sns.repository;

import com.fc.sns.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
}
