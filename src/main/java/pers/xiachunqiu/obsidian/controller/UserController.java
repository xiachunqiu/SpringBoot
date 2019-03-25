package pers.xiachunqiu.obsidian.controller;

import com.google.common.collect.Lists;
import pers.xiachunqiu.obsidian.global.Constants;
import pers.xiachunqiu.obsidian.dto.PagerDTO;
import pers.xiachunqiu.obsidian.dto.UserDTO;
import pers.xiachunqiu.obsidian.entity.User;
import pers.xiachunqiu.obsidian.dto.PagerRtnDTO;
import pers.xiachunqiu.obsidian.dto.ResponseDataDTO;
import pers.xiachunqiu.obsidian.util.DateUtil;
import pers.xiachunqiu.obsidian.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@Log4j2
public class UserController extends BaseController {
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
                        .setGenderDescription(Constants.MALE_KEY.equals(gender) ? "male" : Constants.FEMALE_KEY.equals(gender) ? "female" : "secret");
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
            Assert.isTrue(StringUtils.isNotNull(userDTO.getUserName()), "User name cannot be empty");
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
            Assert.isTrue(StringUtils.isNotNull(userDTO.getGender()), "Gender cannot be empty");
            Assert.isTrue(StringUtils.isNotNull(userDTO.getGender()), "Gender cannot be empty");
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