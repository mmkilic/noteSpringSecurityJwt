package mmk.security.app.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserJwtRequestChangePassword{
	
	private String currentPassword;
	private String newPassword;
	private String confirmationPassword;
	
}