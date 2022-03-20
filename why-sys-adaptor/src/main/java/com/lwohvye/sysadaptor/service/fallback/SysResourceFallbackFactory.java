package com.lwohvye.sysadaptor.service.fallback;

import com.lwohvye.modules.system.domain.Resource;
import com.lwohvye.modules.system.service.dto.ResourceQueryCriteria;
import com.lwohvye.sysadaptor.service.ISysResourceFeignClientService;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SysResourceFallbackFactory implements FallbackFactory<ISysResourceFeignClientService> {
    @Override
    public ISysResourceFeignClientService create(Throwable cause) {
        return new ISysResourceFeignClientService() {
            @Override
            public ResponseEntity<Object> query(ResourceQueryCriteria criteria, Pageable pageable) {
                return new ResponseEntity<>(ResultInfo.failed("请稍后重试"), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Object> create(Resource resources) {
                return new ResponseEntity<>(ResultInfo.failed("添加失败，系统繁忙"), HttpStatus.CREATED);
            }

            @Override
            public ResponseEntity<Object> update(Resource resources) {
                return new ResponseEntity<>(ResultInfo.failed("提交失败，系统繁忙"), HttpStatus.NO_CONTENT);
            }

            @Override
            public ResponseEntity<Object> delete(Long[] ids) {
                return new ResponseEntity<>(ResultInfo.failed("删除失败，系统繁忙"), HttpStatus.OK);
            }
        };
    }
}
