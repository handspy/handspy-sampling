package pt.up.hs.sampling.client.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.client.project.dto.ProjectPermissionsDTO;
import pt.up.hs.sampling.security.ProjectPermissionEvaluator;

import java.util.List;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProjectMicroService {

    private final Logger log = LoggerFactory.getLogger(ProjectPermissionEvaluator.class);

    private final PermissionFeignClient permissionFeignClient;

    @Autowired
    public ProjectMicroService(
        PermissionFeignClient permissionFeignClient
    ) {
        this.permissionFeignClient = permissionFeignClient;
    }

    @Cacheable(value = "project.permissions", key = "#login.concat('-').concat(#projectId)", sync = true)
    public List<String> permissions(String login, Long projectId) {
        log.info(login + " " + projectId);

        ProjectPermissionsDTO projectPermissions = permissionFeignClient
            .getUserPermissionsInProject(projectId, login);

        return projectPermissions.getPermissions();
    }
}
