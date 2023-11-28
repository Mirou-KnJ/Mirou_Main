package com.knj.mirou.base.security.token.repository;

import com.knj.mirou.base.security.token.entity.RememberMeToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RememberMeTokenRepository  extends JpaRepository<RememberMeToken, String> {

    Optional<RememberMeToken> findBySeries(final String series);

    List<RememberMeToken> findByUsername(final String username);
}
