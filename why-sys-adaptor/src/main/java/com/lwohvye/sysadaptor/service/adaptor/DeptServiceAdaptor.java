package com.lwohvye.sysadaptor.service.adaptor;

import com.lwohvye.api.modules.system.domain.Dept;
import com.lwohvye.api.modules.system.service.IDeptService;
import com.lwohvye.api.modules.system.service.dto.DeptDto;
import com.lwohvye.api.modules.system.service.dto.DeptQueryCriteria;
import com.lwohvye.sysadaptor.service.ISysDeptFeignClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@ConditionalOnMissingBean(IDeptService.class)
public class DeptServiceAdaptor implements IDeptService{

    @Autowired
    private ISysDeptFeignClientService deptFeignClientService;

    @Override
    public List<DeptDto> queryAll(Long currentUserId, DeptQueryCriteria criteria, Boolean isQuery) throws Exception {
        return null;
    }

    @Override
    public DeptDto findById(Long id) {
        return null;
    }

    @Override
    public void create(Dept resources) {

    }

    @Override
    public void update(Dept resources) {

    }

    @Override
    public void delete(Set<DeptDto> deptDtos) {

    }

    @Override
    public List<Dept> findByPid(long pid) {
        return null;
    }

    @Override
    public Set<Dept> findByRoleId(Long id) {
        return null;
    }

    @Override
    public void download(List<DeptDto> queryAll, HttpServletResponse response) throws IOException {

    }

    @Override
    public Set<DeptDto> getDeleteDepts(List<Dept> deptList, Set<DeptDto> deptDtos) {
        return null;
    }

    @Override
    public List<DeptDto> getSuperior(DeptDto deptDto, List<Dept> depts) {
        return null;
    }

    @Override
    public Map<String, Object> buildTree(List<DeptDto> deptDtos) {
        return null;
    }

    @Override
    public List<Long> getDeptChildren(List<Dept> deptList) {
        return null;
    }

    @Override
    public void verification(Set<DeptDto> deptDtos) {

    }
}
