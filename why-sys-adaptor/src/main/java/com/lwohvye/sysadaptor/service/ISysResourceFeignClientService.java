package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.api.modules.system.api.SysResourceAPI;
import com.lwohvye.api.modules.system.domain.Resource;
import com.lwohvye.api.modules.system.service.dto.ResourceQueryCriteria;
import com.lwohvye.sysadaptor.service.fallback.SysResourceFallbackFactory;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;

@FeignClient(value = "why-system", contextId = "resourceValentine", fallbackFactory = SysResourceFallbackFactory.class)
public interface ISysResourceFeignClientService extends SysResourceAPI {

    @GetMapping("/api/sys/resources")
    @Override
    ResponseEntity<ResultInfo<Map<String, Object>>> query(@SpringQueryMap ResourceQueryCriteria criteria, Pageable pageable);

    @Log("新增资源")
    @PostMapping("/api/sys/resources")
    @Override
    ResponseEntity<ResultInfo<String>> create(Resource resources);

    @Log("修改资源")
    @PutMapping("/api/sys/resources")
    @Override
    ResponseEntity<ResultInfo<String>> update(Resource resources);

    @Log("删除资源")
    @DeleteMapping("/api/sys/resources")
    @Override
    ResponseEntity<ResultInfo<String>> delete(Long[] ids);
}
