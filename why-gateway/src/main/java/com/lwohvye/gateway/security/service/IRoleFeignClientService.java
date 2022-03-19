/*
 *    Copyright (c) 2022.  lWoHvYe(Hongyan Wang)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.lwohvye.gateway.security.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.base.BaseEntity;
import com.lwohvye.modules.system.domain.Role;
import com.lwohvye.modules.system.service.dto.RoleQueryCriteria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@FeignClient(value = "el-system")
@RequestMapping("/api/sys/roles")
public interface IRoleFeignClientService {

    @GetMapping(value = "/{id}")
    ResponseEntity<Object> query(@PathVariable Long id);

    @GetMapping(value = "/download")
    void download(HttpServletResponse response, RoleQueryCriteria criteria);

    @GetMapping(value = "/all")
    ResponseEntity<Object> query();

    @GetMapping
    ResponseEntity<Object> query(RoleQueryCriteria criteria, Pageable pageable);

    @GetMapping(value = "/level")
    ResponseEntity<Object> getLevel();

    @Log("新增角色")
    @PostMapping
    ResponseEntity<Object> create(@Validated @RequestBody Role resources);

    @Log("修改角色")
    @PutMapping
    ResponseEntity<Object> update(@Validated(BaseEntity.Update.class) @RequestBody Role resources);

    @Log("修改角色菜单")
    @PutMapping(value = "/menu")
    ResponseEntity<Object> updateMenu(@RequestBody Role resources);

    @Log("删除角色")
    @DeleteMapping
    ResponseEntity<Object> delete(@RequestBody Set<Long> ids);

    @GetMapping("/uid/{userId}")
    ResponseEntity<Object> queryByUid(@PathVariable Long userId);
}
