package com.example.p2k.user;

import com.example.p2k._core.exception.Exception400;
import com.example.p2k._core.exception.Exception404;
import com.example.p2k.courseuser.CourseUserRepository;
import com.example.p2k.vm.VmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CourseUserRepository courseUserRepository;
    private final VmRepository vmRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void save(UserRequest.JoinDTO requestDTO) {
        if(requestDTO.getEmailAvailability() == null){
            throw new Exception400("이메일이 확인되지 않았습니다.");
        } else if(!requestDTO.getEmailAvailability()){
            throw new Exception400("중복된 이메일입니다.");
        }

        String enPassword = bCryptPasswordEncoder.encode(requestDTO.getPasswordConf()); // 비밀번호 암호화

        User user = User.builder()
                .email(requestDTO.getEmail())
                .name(requestDTO.getName())
                .password(enPassword)
                .role(requestDTO.getRole())
                .build();
        user.updatePending(requestDTO.getRole() == Role.ROLE_INSTRUCTOR);

        userRepository.save(user);
    }

    public Boolean checkEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    public UserResponse.FindByIdDTO findById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 사용자를 찾을 수 없습니다.")
        );
        return new UserResponse.FindByIdDTO(user);
    }

    @Transactional
    public void update(Long id, UserRequest.UpdateDTO requestDTO){
        userRepository.update(id, requestDTO.getEmail(), requestDTO.getName());
    }

    @Transactional
    public void resetPassword(UserRequest.ResetDTO requestDTO){
        String email = requestDTO.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new Exception404("해당 사용자를 찾을 수 없습니다.")
        );

        String password = requestDTO.getPassword();
        String passwordConf = requestDTO.getPasswordConf();
        if(password != passwordConf){
            throw new Exception400("비밀번호가 일치하지 않습니다.");
        }
        String enPassword = bCryptPasswordEncoder.encode(requestDTO.getPassword());
        userRepository.resetPassword(user.getId(), enPassword);
    }

    @Transactional
    public void delete(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 사용자를 찾을 수 없습니다.")
        );
        courseUserRepository.deleteByUserId(id);
        vmRepository.deleteByUserId(id);
        userRepository.delete(user);
    }
}