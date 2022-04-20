package com.lwohvye.sysadaptor.service.fallback;

import com.lwohvye.api.modules.system.domain.Role;
import com.lwohvye.api.modules.system.service.dto.RoleQueryCriteria;
import com.lwohvye.sysadaptor.service.ISysRoleFeignClientService;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SysRoleFallbackFactory implements FallbackFactory<ISysRoleFeignClientService> {
    @Override
    public ISysRoleFeignClientService create(Throwable cause) {
        return new ISysRoleFeignClientService() {
            @Override
            public ResponseEntity<Object> query(Long id) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> query() {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> query(RoleQueryCriteria criteria, Pageable pageable) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> getLevel() {
                return null;
            }

            @Override
            public ResponseEntity<Object> create(Role resources) {
                return new ResponseEntity<>(ResultInfo.failed("添加失败，系统繁忙"), HttpStatus.CREATED);
            }

            @Override
            public ResponseEntity<Object> update(Role resources) {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，系统繁忙"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<Object> updateMenu(Role resources) {
                return null;
            }

            @Override
            public ResponseEntity<Object> delete(Set<Long> ids) {
                return new ResponseEntity<>(ResultInfo.failed("删除失败，系统繁忙"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> queryByUid(Long userId) {
                return null;
            }
        };
    }
}
