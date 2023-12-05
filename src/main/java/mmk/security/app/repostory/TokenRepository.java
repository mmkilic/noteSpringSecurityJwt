package mmk.security.app.repostory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mmk.security.app.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

	  @Query(value = """
	      select t from Token t inner join UserJwt u\s
	      on t.userJwt.id = u.id\s
	      where u.id = :id and (t.expired = false or t.revoked = false)\s
	      """)
	  List<Token> findAllValidTokenByUser(Integer id);

	  Optional<Token> findByToken(String token);
	}
