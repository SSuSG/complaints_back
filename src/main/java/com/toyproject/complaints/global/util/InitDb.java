package com.toyproject.complaints.global.util;

import com.toyproject.complaints.domain.user.entity.Role;
import com.toyproject.complaints.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

//    @PostConstruct
//    public void init() {
//        initService.dbInit1();
//    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void dbInit1() {

            User superAdmin = User.builder()
                    .userEmail("admin@qwe.com")
                    .userPw(passwordEncoder.encode("1234"))
                    .name("슈퍼운영자")
                    .phoneNumber("01099509587")
                    .employeeIdentificationNum("202312345")
                    .role(Role.ADMIN)
                    //.roles(Collections.singletonList("ROLE_ADMIN"))
                    .active(true)
                    .build();
            em.persist(superAdmin);

//            User admin = User.builder()
//                    .userEmail("admin@qwe.com")
//                    .userPw(passwordEncoder.encode("qwerasdf1234"))
//                    .name("운영자")
//                    .updateUser(superAdmin)
//                    .registerUser(superAdmin)
//                    .phoneNumber("01099509587")
//                    .employeeIdentificationNum("1234")
//                    .roles(Collections.singletonList("ROLE_ADMIN"))
//                    .regDate(LocalDateTime.now())
//                    .updateDate(LocalDateTime.now())
//                    .department("테스트")
//                    .active(true)
//                    .build();

            /*
            User me = User.builder()
                    .userEmail("psg980331@naver.com")
                    .userPw(passwordEncoder.encode("1234"))
                    .name("박시균")
                    .registerUser(superAdmin)
                    .updateUser(superAdmin)
                    .employeeIdentificationNum("1234")
                    .phoneNumber("01099509587")
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .regDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .department("테스트")
                    .active(true)
                    .build();

             */

//            User user = User.builder()
//                    .userEmail("user@qwe.com")
//                    .userPw(passwordEncoder.encode("1234"))
//                    .name("회원")
//                    .registerUser(superAdmin)
//                    .updateUser(superAdmin)
//                    .employeeIdentificationNum("1234")
//                    .phoneNumber("01099509587")
//                    .roles(Collections.singletonList("ROLE_USER"))
//                    .regDate(LocalDateTime.now())
//                    .updateDate(LocalDateTime.now())
//                    .department("테스트")
//                    .active(true)
//                    .build();
//
//            User half = User.builder()
//                    .userEmail("half@qwe.com")
//                    .userPw(passwordEncoder.encode("1234"))
//                    .name("준회원")
//                    .registerUser(superAdmin)
//                    .updateUser(superAdmin)
//                    .employeeIdentificationNum("1234")
//                    .phoneNumber("01099509587")
//                    .roles(Collections.singletonList("ROLE_HALF"))
//                    .regDate(LocalDateTime.now())
//                    .updateDate(LocalDateTime.now())
//                    .department("테스트")
//                    .active(true)
//                    .build();


            em.persist(superAdmin);
            //em.persist(user);
            //em.persist(half);
            //em.persist(me);

        }
    }
}
