package com.lwohvye.sysadaptor.service.fallback;

import com.lwohvye.api.modules.system.domain.User;
import com.lwohvye.api.modules.system.domain.vo.UserBaseVo;
import com.lwohvye.api.modules.system.domain.vo.UserPassVo;
import com.lwohvye.api.modules.system.service.dto.UserInnerDto;
import com.lwohvye.api.modules.system.service.dto.UserQueryCriteria;
import com.lwohvye.sysadaptor.service.ISysUserFeignClientService;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@Component
public class SysUserFallbackFactory implements FallbackFactory<ISysUserFeignClientService> {
    @Override
    public ISysUserFeignClientService create(Throwable cause) {
        return new ISysUserFeignClientService() {
            @Override
            public ResponseEntity<ResultInfo<Map<String, Object>>> query(UserQueryCriteria criteria, Pageable pageable) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<ResultInfo<String>> create(User resources) {
                return new ResponseEntity<>(ResultInfo.failed("添加失败，系统繁忙"), HttpStatus.CREATED);
            }

            @Override
            public ResponseEntity<ResultInfo<String>> update(User resources) throws Exception {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，系统繁忙"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<ResultInfo<String>> updateStatus(UserBaseVo userVo) {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，请稍后重试"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<ResultInfo<String>> center(User resources) {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，系统繁忙"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<ResultInfo<String>> delete(Set<Long> ids) {
                return new ResponseEntity<>(ResultInfo.failed("删除失败，系统繁忙"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<ResultInfo<String>> updatePass(UserPassVo passVo) throws Exception {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> createWithHeader(User resources) {
                return new ResponseEntity<>(ResultInfo.failed("添加失败，系统繁忙"), HttpStatus.CREATED);
            }

            @Override
            public ResponseEntity<Object> createWithHeader(User resources, String token) {
                return new ResponseEntity<>(ResultInfo.failed("添加失败，系统繁忙"), HttpStatus.CREATED);
            }

            @Override
            public ResponseEntity<Map<String, String>> updateAvatar(MultipartFile avatar) {
                return null;
            }

            @Override
            public ResponseEntity<ResultInfo<String>> updateEmail(String code, User user) throws Exception {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<ResultInfo<UserInnerDto>> queryByName(String username) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }
        };
    }
}
