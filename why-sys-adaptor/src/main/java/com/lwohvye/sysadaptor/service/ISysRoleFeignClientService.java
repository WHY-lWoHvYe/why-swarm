package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.api.modules.system.api.SysRoleAPI;
import com.lwohvye.api.modules.system.domain.Role;
import com.lwohvye.api.modules.system.service.dto.RoleQueryCriteria;
import com.lwohvye.sysadaptor.service.fallback.SysRoleFallbackFactory;
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
import java.util.Set;

@FeignClient(value = "why-system", contextId = "roleValentine", fallbackFactory = SysRoleFallbackFactory.class)
public interface ISysRoleFeignClientService extends SysRoleAPI {

    @GetMapping("/api/sys/roles")
    @Override
    ResponseEntity<ResultInfo<Map<String, Object>>> query(@SpringQueryMap RoleQueryCriteria criteria, Pageable pageable);

    @Log("新增角色")
    @PostMapping("/api/sys/roles")
    @Override
    ResponseEntity<ResultInfo<String>> create(Role resources);

    @Log("修改角色")
    @PutMapping("/api/sys/roles")
    @Override
    ResponseEntity<ResultInfo<String>> update(Role resources);

    @Log("修改角色菜单")
    @PutMapping("/api/sys/roles/menu")
    @Override
    ResponseEntity<ResultInfo<String>> updateMenu(Role resources);

    @Log("删除角色")
    @DeleteMapping("/api/sys/roles")
    @Override
    ResponseEntity<ResultInfo<String>> delete(Set<Long> ids);
}
