package com.security.service;

import com.security.entity.User;
import com.security.entity.dto.UserRequestDto;
import com.security.entity.security.AccountStatus;
import com.security.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public User saveUser(User user) {
        user.setAccountStatus(AccountStatus.ACTIVE);
        return repository.save(user);
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

    @Transactional
    public boolean deleteById(Long id) {
        return repository.deleteUserById(id) == 1;
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
        return Optional.empty();
    }
}
/*@Transactional
    public Optional<UserDto> register(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            return Optional.empty();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return Optional.of(modelMapper.map(userRepository.save(user), UserDto.class));
    }*/