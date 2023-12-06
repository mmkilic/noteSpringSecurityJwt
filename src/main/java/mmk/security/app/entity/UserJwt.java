package mmk.security.app.entity;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mmk.security.app.enums.Authority;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJwt implements UserDetails{
	
	private static final long serialVersionUID = -9070019381762409016L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique=true)
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	
	@Enumerated(EnumType.STRING)
	private Set<Authority> authorities;
	
	@JsonIgnore
	@OneToMany(mappedBy = "userJwt")
	private List<Token> tokens;

	@Override
	public String getUsername() {
		return email;
	}
	
}