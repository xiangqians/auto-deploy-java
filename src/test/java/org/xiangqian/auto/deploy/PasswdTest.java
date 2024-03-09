package org.xiangqian.auto.deploy;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.xiangqian.auto.deploy.util.List;

import java.util.ArrayList;

/**
 * @author xiangqian
 * @date 19:42 2024/02/29
 */
public class PasswdTest {

    @Test
    public void test() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        CharSequence rawPassword = "admin";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println(encodedPassword);

        List<Integer> list = new List<>(5, 10);

        int capacity = 100;
        java.util.List<Integer> data = new ArrayList<>(capacity);
        while (capacity-- > 0) {
            data.add(capacity);
        }
        list.setData(data);

        System.out.println(list.getOffsets());
    }

}
