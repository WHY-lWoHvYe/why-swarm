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
import com.lwohvye.modules.system.domain.Dept;
import com.lwohvye.modules.system.service.dto.DeptQueryCriteria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(value = "el-system")
@RequestMapping("/api/sys/dept")
public interface IDateFeignClientService {

    @GetMapping
    ResponseEntity<Object> query(DeptQueryCriteria criteria);

    @PostMapping("/superior")
    ResponseEntity<Object> getSuperior(@RequestBody List<Long> ids);

    @Log("新增部门")
    @PostMapping
    ResponseEntity<Object> create(@Validated @RequestBody Dept resources);

    @Log("修改部门")
    @PutMapping
    ResponseEntity<Object> update(@Validated(BaseEntity.Update.class) @RequestBody Dept resources);

    @Log("删除部门")
    @DeleteMapping
    ResponseEntity<Object> delete(@RequestBody Set<Long> ids);

    @GetMapping("/enabled/{userId}/{deptId}")
    ResponseEntity<Object> queryEnabledDeptIds(@PathVariable Long userId, @PathVariable Long deptId);
}
