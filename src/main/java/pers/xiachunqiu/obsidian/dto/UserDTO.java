package pers.xiachunqiu.obsidian.dto;

import pers.xiachunqiu.obsidian.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserDTO implements Serializable {
    private Long id;
    private String userName;
    private String passwordOriginal;
    private String gender;
    private String genderDescription;
    private String address;
    private String createdTimeString;

    public User convert() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public static UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}