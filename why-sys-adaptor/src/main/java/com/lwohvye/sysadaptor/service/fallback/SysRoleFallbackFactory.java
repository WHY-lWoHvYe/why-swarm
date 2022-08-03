package com.lwohvye.sysadaptor.service.fallback;

import cn.hutool.core.lang.Dict;
import com.lwohvye.api.modules.system.domain.Role;
import com.lwohvye.api.modules.system.service.dto.RoleDto;
import com.lwohvye.api.modules.system.service.dto.RoleQueryCriteria;
import com.lwohvye.api.modules.system.service.dto.RoleSmallDto;
import com.lwohvye.sysadaptor.service.ISysRoleFeignClientService;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class SysRoleFallbackFactory implements FallbackFactory<ISysRoleFeignClientService> {
    @Override
    public ISysRoleFeignClientService create(Throwable cause) {
        return new ISysRoleFeignClientService() {
            @Override
            public ResponseEntity<ResultInfo<RoleDto>> query(Long id) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<ResultInfo<RoleDto>> query() {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<ResultInfo<Map<String, Object>>> query(RoleQueryCriteria criteria, Pageable pageable) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<ResultInfo<Dict>> getLevel() {
                return null;
            }

            @Override
            public ResponseEntity<ResultInfo<String>> create(Role resources) {
                return new ResponseEntity<>(ResultInfo.failed("添加失败，系统繁忙"), HttpStatus.CREATED);
            }

            @Override
            public ResponseEntity<ResultInfo<String>> update(Role resources) {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，系统繁忙"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<ResultInfo<String>> updateMenu(Role resources) {
                return null;
            }

            @Override
            public ResponseEntity<ResultInfo<String>> delete(Set<Long> ids) {
                return new ResponseEntity<>(ResultInfo.failed("删除失败，系统繁忙"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<ResultInfo<RoleSmallDto>> queryByUid(Long userId) {
                return null;
            }
        };
    }
}
