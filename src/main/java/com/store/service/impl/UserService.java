package com.store.service.impl;

import com.store.entity.UserEntity;
import com.store.exception.UserAlreadyExistException;
import com.store.models.UserDto;
import com.store.repository.UserRepository;
import com.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.store.utils.ApplicationConstants.USER_ALREADY_EXIST_ERROR;
import static com.store.utils.ApplicationConstants.USER_NOT_FOUND_ERROR;


@Service("userService")
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserEntity registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException(String.format(USER_ALREADY_EXIST_ERROR, userDto.getEmail()));
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(userDto.getPassword());
        userEntity.setEmail(userDto.getEmail());
        encodePassword(userEntity, userDto);

        return userRepository.save(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_ERROR, email)));
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private void encodePassword(UserEntity userEntity, UserDto userDto){
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
}
