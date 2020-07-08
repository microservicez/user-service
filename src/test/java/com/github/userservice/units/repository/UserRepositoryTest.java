package com.github.userservice.units.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.userservice.dto.User;
import com.github.userservice.repository.UserRepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {
	private final UserRepository repository;
	private final TestEntityManager entityManager;
	
	@Autowired
	UserRepositoryTest(UserRepository repository, TestEntityManager entityManager) {
		this.repository = repository;
		this.entityManager = entityManager;
	}
	
	@Test
	public void getUser_returnsUser() {
		User user = new User();
		user.setName("Jane");
		User persistedUser = entityManager.persistFlushFind(user);
		User userLookUp = repository.findById(persistedUser.getId()).get();
		
		assertThat(userLookUp.getId()).isEqualTo(persistedUser.getId());
		assertThat(userLookUp.getName()).isEqualTo(persistedUser.getName());
	}
	
	@Test
	public void addUser_persistUser() {
		User user = new User();
		user.setName("John");
		User persistedUser = repository.save(user);

		assertThat(persistedUser.getName()).isEqualTo(user.getName());
	}

}
