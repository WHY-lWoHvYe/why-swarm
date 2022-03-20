package com.lwohvye.sysadaptor.service.fallback;

import com.lwohvye.modules.system.domain.User;
import com.lwohvye.modules.system.domain.vo.UserBaseVo;
import com.lwohvye.modules.system.domain.vo.UserPassVo;
import com.lwohvye.modules.system.service.dto.UserQueryCriteria;
import com.lwohvye.sysadaptor.service.ISysUserFeignClientService;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class SysUserFallbackFactory implements FallbackFactory<ISysUserFeignClientService> {
    @Override
    public ISysUserFeignClientService create(Throwable cause) {
        return new ISysUserFeignClientService() {
            @Override
            public ResponseEntity<Object> query(UserQueryCriteria criteria, Pageable pageable) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> create(User resources) {
                return new ResponseEntity<>(ResultInfo.failed("添加失败，系统繁忙"), HttpStatus.CREATED);
            }

            @Override
            public ResponseEntity<Object> update(User resources) throws Exception {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，系统繁忙"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<Object> updateStatus(UserBaseVo userVo) {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，请稍后重试"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<Object> center(User resources) {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，系统繁忙"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<Object> delete(Set<Long> ids) {
                return new ResponseEntity<>(ResultInfo.failed("删除失败，系统繁忙"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> updatePass(UserPassVo passVo) throws Exception {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> updateAvatar(MultipartFile avatar) {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> updateEmail(String code, User user) throws Exception {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> queryByName(String username) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }
        };
    }
}
