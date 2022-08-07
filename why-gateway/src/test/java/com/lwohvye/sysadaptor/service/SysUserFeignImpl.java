package com.lwohvye.sysadaptor.service;

import com.lwohvye.api.modules.system.domain.User;
import com.lwohvye.api.modules.system.domain.vo.UserBaseVo;
import com.lwohvye.api.modules.system.domain.vo.UserPassVo;
import com.lwohvye.api.modules.system.service.dto.UserInnerDto;
import com.lwohvye.api.modules.system.service.dto.UserQueryCriteria;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

public class SysUserFeignImpl implements ISysUserFeignClientService{
    @Override
    public ResponseEntity<ResultInfo<Map<String, Object>>> query(UserQueryCriteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<ResultInfo<String>> create(User resources) {
        return null;
    }

    @Override
    public ResponseEntity<ResultInfo<String>> update(User resources) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<ResultInfo<String>> updateStatus(UserBaseVo userVo) {
        return null;
    }

    @Override
    public ResponseEntity<ResultInfo<String>> center(User resources) {
        return null;
    }

    @Override
    public ResponseEntity<ResultInfo<String>> delete(Set<Long> ids) {
        return null;
    }

    @Override
    public ResponseEntity<ResultInfo<String>> updatePass(UserPassVo passVo) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, String>> updateAvatar(MultipartFile avatar) {
        return null;
    }

    @Override
    public ResponseEntity<ResultInfo<String>> updateEmail(String code, User user) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<ResultInfo<UserInnerDto>> queryByName(String username) {
        return null;
    }

    @Override
    public ResponseEntity<Object> createWithHeader(User resources) {
        return null;
    }

    @Override
    public ResponseEntity<Object> createWithHeader(User resources, String token) {
        return null;
    }
}
