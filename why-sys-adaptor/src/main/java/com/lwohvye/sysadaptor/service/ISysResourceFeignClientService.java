package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.api.modules.system.api.SysResourceAPI;
import com.lwohvye.api.modules.system.domain.Resource;
import com.lwohvye.sysadaptor.service.fallback.SysResourceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

@FeignClient(value = "sys-service", fallbackFactory = SysResourceFallbackFactory.class)
public interface ISysResourceFeignClientService extends SysResourceAPI {

    @Log("新增资源")
    @Override
    ResponseEntity<Object> create(Resource resources);

    @Log("修改资源")
    @Override
    ResponseEntity<Object> update(Resource resources);

    @Log("删除资源")
    @Override
    ResponseEntity<Object> delete(Long[] ids);
}
