package com.x.y.controller;

import com.google.common.collect.Lists;
import com.x.y.common.Constants;
import com.x.y.dto.PagerDTO;
import com.x.y.dto.UserDTO;
import com.x.y.entity.User;
import com.x.y.dto.PagerRtnDTO;
import com.x.y.dto.ResponseDataDTO;
import com.x.y.util.DateUtil;
import com.x.y.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hello")
@Log4j2
public class HelloController extends BaseController {
    @RequestMapping("getUserList")
    public ResponseDataDTO getUserList(UserDTO userDTO, @RequestParam(defaultValue = "1", value = "pageNo") Integer pageNo) {
        try {
            User user = userDTO.convert();
            int count = super.getCommonService().getEntityCountForSearch(user);
            PagerDTO pagerDTO = new PagerDTO(count, Constants.PAGE_SIZE, pageNo);
            List<User> userList = super.getCommonService().getEntityListForSearch(user, pagerDTO);
            List<UserDTO> userDTOList = Lists.newArrayList();
            userList.forEach(u -> {
                UserDTO userOutputDTO = UserDTO.convert(u);
                String gender = u.getGender();
                userOutputDTO.setCreatedTimeString(DateUtil.format(u.getCreatedTime()))
                        .setGenderDescription(Constants.MALE_KEY.equals(gender) ? "男" : Constants.FEMALE_KEY.equals(gender) ? "女" : "不详");
                userDTOList.add(userOutputDTO);
            });
            PagerRtnDTO<UserDTO> pagerRtnDTO = super.getPagerRtn(userDTOList, pagerDTO);
            return ResponseDataDTO.success(pagerRtnDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataDTO.fail();
        }
    }

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public ResponseDataDTO addUser(UserDTO userDTO) {
        try {
            Assert.isTrue(StringUtil.isNotNull(userDTO.getUserName()), "User name cannot be empty");
            User user = userDTO.convert();
            Date now = new Date();
            user.setCreatedTime(now).setModifiedTime(now).setPassword(getPassword(null));
            super.getCommonService().insert(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataDTO.fail(e.getMessage());
        }
        return ResponseDataDTO.success();
    }

    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public ResponseDataDTO updateUser(UserDTO userDTO) {
        try {
            Assert.isTrue(userDTO.getId() != null, "ID cannot be empty");
            Assert.isTrue(StringUtil.isNotNull(userDTO.getGender()), "Gender cannot be empty");
            Assert.isTrue(StringUtil.isNotNull(userDTO.getGender()), "Gender cannot be empty");
            User dbUser = super.getCommonService().getEntityById(userDTO.getId(), User.class);
            Assert.isTrue(dbUser != null, "User is not exist");
            User user = userDTO.convert();
            dbUser.setModifiedTime(new Date()).setAddress(user.getAddress()).setGender(user.getGender());
            super.getCommonService().update(dbUser);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataDTO.fail(e.getMessage());
        }
        return ResponseDataDTO.success();
    }
}