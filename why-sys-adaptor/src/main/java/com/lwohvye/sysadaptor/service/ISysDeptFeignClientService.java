package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.api.modules.system.api.SysDeptAPI;
import com.lwohvye.api.modules.system.domain.Dept;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Set;

@FeignClient(value = "why-system", contextId = "deptValentine")
public interface ISysDeptFeignClientService extends SysDeptAPI {

    @Log("新增部门")
    @PostMapping("/api/sys/dept")
    @Override
    ResponseEntity<ResultInfo<String>> create(Dept resources);

    @Log("修改部门")
    @PutMapping("/api/sys/dept")
    @Override
    ResponseEntity<ResultInfo<String>> update(Dept resources);

    @Log("删除部门")
    @DeleteMapping("/api/sys/dept")
    @Override
    ResponseEntity<ResultInfo<String>> delete(Set<Long> ids);
}
