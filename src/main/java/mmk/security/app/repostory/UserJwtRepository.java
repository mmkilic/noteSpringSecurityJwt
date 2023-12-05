package mmk.security.app.repostory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mmk.security.app.entity.UserJwt;

@Repository
public interface UserJwtRepository extends JpaRepository<UserJwt, Integer>{
	@Query("SELECT u from UserJwt u where u.email=(:email)")
	Optional<UserJwt> findByEmail(@Param("email") String email);
}
