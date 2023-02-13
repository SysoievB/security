package com.security.service;

import com.security.entity.User;
import com.security.entity.dto.UserRequestDto;
import com.security.entity.security.AccountStatus;
import com.security.mapper.UserMapper;
import com.security.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public User updateUser(Long id, User user) {
        var userForUpdate = repository.findById(id).orElseThrow(NoSuchElementException::new);

        userForUpdate.setFirstName(user.getFirstName());
        userForUpdate.setLastName(user.getLastName());
        userForUpdate.setAge(user.getAge());
        userForUpdate.setUsername(user.getUsername());

        if (userForUpdate.getCustomer() == null
                || !userForUpdate.getCustomer().equals(user.getCustomer())) {
            userForUpdate.setCustomer(user.getCustomer());
        }

        return userForUpdate;
    }

    public User deleteById(Long id) {
        var user = repository.findById(id).
                orElseThrow(
                        () -> new UsernameNotFoundException("User with id " + id + " was not found")
                );

        user.setAccountStatus(AccountStatus.DELETED);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username " + username + " was not found")
                );
    }

    @Transactional
    public Optional<User> register(UserRequestDto userRequestDto) {
        var user = mapper.userRequestDtoToUser(userRequestDto);

        if (repository.existsUserByUsername(user.getUsername())) {
            return Optional.empty();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountStatus(AccountStatus.ACTIVE);

        return Optional.of(repository.save(user));
    }
}
