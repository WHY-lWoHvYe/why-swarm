package com.lwohvye.sysadaptor.service.adaptor;

import com.lwohvye.api.modules.system.domain.Dept;
import com.lwohvye.api.modules.system.domain.User;
import com.lwohvye.api.modules.system.service.IUserService;
import com.lwohvye.api.modules.system.service.dto.UserDto;
import com.lwohvye.api.modules.system.service.dto.UserInnerDto;
import com.lwohvye.api.modules.system.service.dto.UserQueryCriteria;
import com.lwohvye.sysadaptor.service.ISysUserFeignClientService;
import com.lwohvye.utils.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@ConditionalOnMissingBean(IUserService.class)
public class UserServiceAdaptor implements IUserService {

    @Autowired
    private ISysUserFeignClientService userFeignClientService;

    @Override
    public UserDto findById(long id) {
        return null;
    }

    @Override
    public void create(User resources) {

    }

    @Override
    public void update(User resources) {

    }

    @Override
    public void delete(Set<Long> ids) {

    }

    @Override
    public UserDto findByName(String userName) {
        return null;
    }

    @Override
    public UserInnerDto findInnerUserByName(String userName) {
        var userEntity = userFeignClientService.queryByName(userName);
        return ResultUtil.getEntityFromResp(userEntity);
    }

    @Override
    public void updatePass(String username, String encryptPassword) {

    }

    @Override
    public Map<String, String> updateAvatar(MultipartFile file) {
        return null;
    }

    @Override
    public void updateEmail(String username, String email) {

    }

    @Override
    public void updateEnabled(String username, Boolean enabled) {

    }

    @Override
    public Map<String, Object> queryAll(UserQueryCriteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public List<UserDto> queryAll(UserQueryCriteria criteria) {
        return null;
    }

    @Override
    public List<User> queryAll(User expUser, Pageable pageable) {
        return null;
    }

    @Override
    public void download(List<UserDto> queryAll, HttpServletResponse response) {

    }

    @Override
    public void updateCenter(User resources) {

    }

    @Override
    public int countByRoles(Collection<Long> rids) {
        return 0;
    }

    @Override
    public int countByJobs(Collection<Long> jids) {
        return 0;
    }

    @Override
    public Boolean hasDepts(List<Dept> depts) {
        return null;
    }
}
