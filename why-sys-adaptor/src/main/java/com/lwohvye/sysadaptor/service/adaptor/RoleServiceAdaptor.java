package com.lwohvye.sysadaptor.service.adaptor;

import com.lwohvye.api.modules.system.domain.Dept;
import com.lwohvye.api.modules.system.domain.Role;
import com.lwohvye.api.modules.system.service.IRoleService;
import com.lwohvye.api.modules.system.service.dto.RoleDto;
import com.lwohvye.api.modules.system.service.dto.RoleQueryCriteria;
import com.lwohvye.api.modules.system.service.dto.RoleSmallDto;
import com.lwohvye.sysadaptor.service.ISysRoleFeignClientService;
import com.lwohvye.utils.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@ConditionalOnMissingBean(IRoleService.class)
public class RoleServiceAdaptor implements IRoleService {

    @Autowired
    private ISysRoleFeignClientService roleFeignClientService;

    @Override
    public List<RoleDto> queryAll() {
        return null;
    }

    @Override
    public RoleDto findById(long id) {
        return null;
    }

    @Override
    public void create(Role resources) {

    }

    @Override
    public void update(Role resources) {

    }

    @Override
    public void delete(Set<Long> ids) {

    }

    @Override
    public List<RoleSmallDto> findByUserId(Long userId) {
        var roleEntity = roleFeignClientService.queryByUid(userId);
        return ResultUtil.getListFromResp(roleEntity);
    }

    @Override
    public Integer findByRoles(Set<Role> roles) {
        return null;
    }

    @Override
    public void updateMenu(Role resources, RoleDto roleDTO) {

    }

    @Override
    public void untiedMenu(Long id) {

    }

    @Override
    public Map<String, Object> queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public List<RoleDto> queryAll(RoleQueryCriteria criteria) {
        return null;
    }

    @Override
    public void download(List<RoleDto> queryAll, HttpServletResponse response) {

    }

    @Override
    public List<GrantedAuthority> grantedAuthorityGenHandler(Long userId, Boolean isAdmin) {
        return null;
    }

    @Override
    public void verification(Set<Long> ids) {

    }

    @Override
    public List<Role> findInMenuId(List<Long> menuIds) {
        return null;
    }

    @Override
    public Boolean hasDepts(List<Dept> depts) {
        return null;
    }
}
