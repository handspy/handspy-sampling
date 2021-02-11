package pt.up.hs.sampling.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.client.project.ProjectMicroService;
import pt.up.hs.sampling.domain.*;

import java.io.Serializable;
import java.util.Optional;

import static pt.up.hs.sampling.config.Constants.INTERNAL_CLIENT_ID;

@Component
public class ProjectPermissionEvaluator implements PermissionEvaluator {
    private static final String TARGET_TYPE_PROJECT = "Project";

    private final Logger log = LoggerFactory.getLogger(ProjectPermissionEvaluator.class);

    private final ProjectMicroService projectMicroService;

    @Autowired
    public ProjectPermissionEvaluator(
        ProjectMicroService projectMicroService
    ) {
        this.projectMicroService = projectMicroService;
    }

    @Override
    public boolean hasPermission(
        Authentication auth, Object targetDomainObject, Object permission
    ) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }

        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();

        if (!userLogin.isPresent()) {
            return false;
        }

        if (userLogin.get().equals(INTERNAL_CLIENT_ID)) {
            return true;
        }

        Long projectId = null; // permission check is only available for projects
        if (targetDomainObject instanceof Annotation) {
            projectId = ((Annotation) targetDomainObject).getText().getProjectId();
        } else if (targetDomainObject instanceof AnnotationType) {
            projectId = ((AnnotationType) targetDomainObject).getProjectId();
        } else if (targetDomainObject instanceof Note) {
            projectId = ((Note) targetDomainObject).getProjectId();
        } else if (targetDomainObject instanceof Protocol) {
            projectId = ((Protocol) targetDomainObject).getProjectId();
        } else if (targetDomainObject instanceof Text) {
            projectId = ((Text) targetDomainObject).getProjectId();
        }

        if (projectId == null) {
            return false;
        }

        // permission check is only available for projects
        return projectMicroService.permissions(userLogin.get(), projectId)
            .parallelStream()
            .anyMatch(projectPermission ->
                projectPermission.equals(permission.toString())
            );
    }

    @Override
    public boolean hasPermission(
        Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();

        if (!userLogin.isPresent()) {
            return false;
        }

        if (userLogin.get().equals(INTERNAL_CLIENT_ID)) {
            return true;
        }

        if (targetType.equals(TARGET_TYPE_PROJECT)) {
            // permission check is only available for projects
            return projectMicroService.permissions(userLogin.get(), (Long) targetId)
                .parallelStream()
                .anyMatch(projectPermission ->
                    projectPermission.equals(permission.toString())
                );
        }

        return false;
    }
}
