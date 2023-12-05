package mmk.security.app.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
	public UserJwt findByEmail(String email) {
		return userRepo.findByEmail(email).get();
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
                .role(request.getRole())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .build();

        return userRepo.save(newUser);
    }
	/*public void changePassword(UserJwt request, Principal connectedUser) {

        var user = (UserJwt) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(encoder.encode(request.getPassword()));
        userRepo.save(user);
    }*/
	
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserJwt> user = userRepo.findByEmail(email);
        return user.orElseThrow(EntityNotFoundException::new);
    }
}