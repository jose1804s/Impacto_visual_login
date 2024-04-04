package com.impacto.visual.User;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; 

    @Transactional
    public UserResponse updateUser(UserRequest userRequest) {
       
        User user = User.builder()
        .id(userRequest.id)
        .persontype(userRequest.getPersontype())
        .documenttype(userRequest.getDocumenttype())
        .documentnumber(userRequest.getDocumentnumber())
        .firstname(userRequest.getFirstname())
        .lastname(userRequest.lastname)
        .address(userRequest.address)
        .email(userRequest.email)
        .phone(userRequest.phone)
        .role(Role.USER)
        .build();
        
        userRepository.updateUser(user.id, user.persontype, user.documenttype, user.documentnumber, user.firstname, user.lastname, user.address, user.email, user.phone);

        return new UserResponse("El usuario se registró satisfactoriamente");
    }

    public UserDTO getUser(Integer id) {
        User user= userRepository.findById(id).orElse(null);
       
        if (user!=null)
        {
            UserDTO userDTO = UserDTO.builder()
            .id(user.id)
            .username(user.username)
            .persontype(user.persontype)
            .documenttype(user.documenttype)
            .documentnumber(user.documentnumber)
            .firstname(user.firstname)
            .lastname(user.lastname)
            .address(user.address)
            .email(user.email)
            .phone(user.phone)

            
            .build();
            return userDTO;
        }
        return null;
    }
}