package com.lwohvye.sysadaptor.service;

import com.lwohvye.modules.system.api.SysDeptAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "sys-service")
public interface ISysDeptFeignClientService extends SysDeptAPI {
}
