package com.lwohvye.sysadaptor.service.adaptor;

import com.lwohvye.api.modules.system.service.IDataService;
import com.lwohvye.sysadaptor.service.ISysDeptFeignClientService;
import com.lwohvye.utils.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnMissingBean(IDataService.class)
public class DataServiceAdaptor implements IDataService {

    @Autowired
    private ISysDeptFeignClientService deptFeignClientService;

    @Override
    public List<Long> getDeptIds(Long userId, Long deptId) {
        var deptEntity = deptFeignClientService.queryEnabledDeptIds(userId, deptId);
        return ResultUtil.getListFromResp(deptEntity);

    }
}
