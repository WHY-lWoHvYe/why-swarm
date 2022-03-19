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
import com.lwohvye.modules.system.domain.Resource;
import com.lwohvye.modules.system.service.dto.ResourceQueryCriteria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "el-system")
@RequestMapping("/api/sys/resources")
public interface IResourceFeignClientService {

    @GetMapping
    ResponseEntity<Object> query(ResourceQueryCriteria criteria, Pageable pageable);

    @Log("新增资源")
    @PostMapping
    ResponseEntity<Object> create(@Validated @RequestBody Resource resources);

    @Log("修改资源")
    @PutMapping
    ResponseEntity<Object> update(@Validated @RequestBody Resource resources);

    @Log("删除资源")
    @DeleteMapping
    ResponseEntity<Object> delete(@RequestBody Long[] ids);

    @GetMapping("/queryAllRes")
    ResponseEntity<Object> queryAllRes();
}
