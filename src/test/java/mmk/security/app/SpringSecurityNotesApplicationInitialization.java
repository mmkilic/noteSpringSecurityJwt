package mmk.security.app;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import mmk.security.app.entity.UserJwt;
import mmk.security.app.service.UserJwtService;

@SpringBootTest
class SpringSecurityNotesApplicationInitialization {
	
	private ObjectMapper mapper;
	
	@Autowired
	private UserJwtService userService;
	
	@Test
	void contextLoads() {
		mapper = JsonMapper.builder()
				.addModule(new ParameterNamesModule())
				.addModule(new Jdk8Module())
				.addModule(new JavaTimeModule())
				.build();

		prjUserInitialization("/json/user_jwt.json");
	}
	
	void prjUserInitialization(String jsonFile) {
		TypeReference<List<UserJwt>> typeReference = new TypeReference<List<UserJwt>>() {};
		InputStream inputStream = TypeReference.class.getResourceAsStream(jsonFile);
		try {
			List<UserJwt> users = mapper.readValue(inputStream, typeReference);
			for (UserJwt user : users) {
				userService.save(user);
				System.out.println("All users have been saved.");
			}
		}catch (Exception e) {
			System.out.println("There was an error while recording!");
		}
	}

}
