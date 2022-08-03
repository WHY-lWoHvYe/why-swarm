package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.api.modules.system.api.SysDeptAPI;
import com.lwohvye.api.modules.system.domain.Dept;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@FeignClient(value = "sys-service")
public interface ISysDeptFeignClientService extends SysDeptAPI {

    @Log("新增部门")
    @Override
    ResponseEntity<ResultInfo<String>> create(Dept resources);

    @Log("修改部门")
    @Override
    ResponseEntity<ResultInfo<String>> update(Dept resources);

    @Log("删除部门")
    @Override
    ResponseEntity<ResultInfo<String>> delete(Set<Long> ids);
}
