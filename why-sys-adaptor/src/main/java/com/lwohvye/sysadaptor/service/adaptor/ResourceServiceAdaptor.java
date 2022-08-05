package com.lwohvye.sysadaptor.service.adaptor;

import com.lwohvye.api.modules.system.domain.Resource;
import com.lwohvye.api.modules.system.service.IResourceService;
import com.lwohvye.api.modules.system.service.dto.ResourceDto;
import com.lwohvye.api.modules.system.service.dto.ResourceQueryCriteria;
import com.lwohvye.sysadaptor.service.ISysResourceFeignClientService;
import com.lwohvye.utils.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Service
@ConditionalOnMissingBean(IResourceService.class)
public class ResourceServiceAdaptor implements IResourceService {

    @Autowired
    private ISysResourceFeignClientService resourceFeignClientService;

    @Override
    public Map<String, Object> queryAll(ResourceQueryCriteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public List<ResourceDto> queryAll(ResourceQueryCriteria criteria) {
        return null;
    }

    @Override
    public List<ResourceDto> queryAllRes() {
        var resourcesEntity = resourceFeignClientService.queryAllRes();
        return ResultUtil.getListFromResp(resourcesEntity);
    }

    @Override
    public ResourceDto findById(Long resourceId) {
        return null;
    }

    @Override
    public ResourceDto create(Resource resources) {
        return null;
    }

    @Override
    public void update(Resource resources) {

    }

    @Override
    public void deleteAll(Long[] ids) {

    }

    @Override
    public void download(List<ResourceDto> all, HttpServletResponse response) {

    }
}
