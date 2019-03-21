package com.x.y.repository;

import com.x.y.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select t from User t where t.userName=:name")
    User findUserByName(@Param("name") String name);
}