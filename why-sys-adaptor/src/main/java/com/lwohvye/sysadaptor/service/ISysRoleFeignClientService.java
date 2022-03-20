package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.modules.system.api.SysRoleAPI;
import com.lwohvye.modules.system.domain.Role;
import com.lwohvye.sysadaptor.service.fallback.SysRoleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@FeignClient(value = "sys-service", fallbackFactory = SysRoleFallbackFactory.class)
public interface ISysRoleFeignClientService extends SysRoleAPI {

    @Log("新增角色")
    @Override
    ResponseEntity<Object> create(Role resources);

    @Log("修改角色")
    @Override
    ResponseEntity<Object> update(Role resources);

    @Log("修改角色菜单")
    @Override
    ResponseEntity<Object> updateMenu(Role resources);

    @Log("删除角色")
    @Override
    ResponseEntity<Object> delete(Set<Long> ids);
}
