package com.a601.refesta.login.repository;

import com.a601.refesta.login.data.GoogleAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleAccessTokenRepository extends CrudRepository<GoogleAccessToken, String> {

    Optional<GoogleAccessToken> findById(String id);
}
