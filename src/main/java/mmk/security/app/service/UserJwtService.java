package mmk.security.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mmk.security.app.entity.UserJwt;
import mmk.security.app.repostory.UserJwtRepository;

@Service
public class UserJwtService implements UserDetailsService {
	
	@Autowired
	private UserJwtRepository userRepo;
	@Autowired
	private PasswordEncoder encoder;
	
	
	public List<UserJwt> findAll(){
		return userRepo.findAll();
	}
	public UserJwt findById(int id) {
		return userRepo.findById(id).get();
	}
	public Optional<UserJwt> findByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	public UserJwt save(UserJwt user) {
		return userRepo.save(user);
	}
	public void delete(int prjId) {
		userRepo.deleteById(prjId);
	}
	
	public UserJwt createUser(UserJwt request) {

		UserJwt newUser = UserJwt.builder()
				                .firstName(request.getFirstName())
				                .lastName(request.getLastName())
				                .email(request.getEmail())
				                .password(encoder.encode(request.getPassword()))
				                .authorities(request.getAuthorities())
				                .accountNonExpired(true)
				                .credentialsNonExpired(true)
				                .enabled(true)
				                .accountNonLocked(true)
				                .build();
		
        return userRepo.save(newUser);
    }
	
	
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepo.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }
}