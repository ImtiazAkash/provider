package com.service.provider.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.service.provider.dto.RoleDto;
import com.service.provider.dto.UserDto;
import com.service.provider.model.Role;
import com.service.provider.model.User;
import com.service.provider.repository.RoleRepositoy;
import com.service.provider.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepositoy roleRepositoy;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private ModelMapper modelMapper = ModelMapperST.getInstance();

    @Override
    public UserDto getUserById(long id) {
        User user = this.userRepository.findById(id).orElseThrow();
        return convertToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = this.userRepository.findAll();
        List<UserDto> userDtoList =
                userList.stream().map(this::convertToUserDto).collect(Collectors.toList());
        return userDtoList;
    }



    private UserDto convertToUserDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);


        return userDto;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username);
    }

    @Override
    @Transactional
    public String createUser(UserDto userdto) {
        try {
            // Fetch the role by name
            Set<Role> roles = new HashSet<>();
            for (RoleDto roleDto : userdto.getRoles()) {
                Role role = roleRepositoy.findByRoleName(roleDto.getRoleName());
                if (role == null) {
                    role = new Role();
                    role.setRoleName(roleDto.getRoleName());
                    roleRepositoy.save(role);
                }
                roles.add(role);
            }

            User user = new User();
            user.setName(userdto.getName());
            user.setEmail(userdto.getEmail());
            user.setPassword(passwordEncoder.encode(userdto.getPassword()));
            user.setAbout(userdto.getAbout());
            user.setRoles(roles);

            // Save the user to the database
            userRepository.save(user);
            return "User created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "User creation failed";

        }
    }

    @Override
    public UserDto getUserByUserName(String userName) {
        User user = userRepository.findByEmail(userName);
        return convertToUserDto(user);
    }

}
