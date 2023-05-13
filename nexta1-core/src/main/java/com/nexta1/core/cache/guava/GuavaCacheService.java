package com.nexta1.core.cache.guava;


import com.nexta1.orm.system.entity.SysDeptEntity;
import com.nexta1.orm.system.service.ISysConfigService;
import com.nexta1.orm.system.service.ISysDeptService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author valarchie
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class GuavaCacheService {

    @NonNull
    private ISysConfigService configService;

    @NonNull
    private ISysDeptService deptService;


    public final AbstractGuavaCacheDao<String> configCache = new AbstractGuavaCacheDao<>() {
        public String getObjectFromDb(Object id) {
            return configService.getConfigValueByKey(id.toString());
        }
    };

    public final AbstractGuavaCacheDao<SysDeptEntity> deptCache = new AbstractGuavaCacheDao<SysDeptEntity>() {
        @Override
        public SysDeptEntity getObjectFromDb(Object id) {
            return deptService.getById(id.toString());
        }
    };


}
