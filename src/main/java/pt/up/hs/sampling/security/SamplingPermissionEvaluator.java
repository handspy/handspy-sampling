package pt.up.hs.sampling.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.repository.PermissionRepository;
import pt.up.hs.sampling.repository.SampleRepository;
import pt.up.hs.sampling.repository.TextRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Component
public class SamplingPermissionEvaluator implements PermissionEvaluator {

    private final SampleRepository sampleRepository;
    private final TextRepository textRepository;

    private final PermissionRepository permissionRepository;

    @Autowired
    public SamplingPermissionEvaluator(
        SampleRepository sampleRepository,
        TextRepository textRepository,
        PermissionRepository permissionRepository
    ) {
        this.sampleRepository = sampleRepository;
        this.textRepository = textRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean hasPermission(
        Authentication auth, Object targetDomainObject, Object requiredPermission
    ) {
        if ((auth == null) || (targetDomainObject == null) || !(requiredPermission instanceof String)){
            return false;
        }

        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (!userLogin.isPresent()) {
            return false;
        }

        Long projectId = null; // permission check is only available for projects
        if (targetDomainObject instanceof Annotation) {
            projectId = ((Annotation) targetDomainObject).getText().getProjectId();
        } else if (targetDomainObject instanceof AnnotationType) {
            projectId = ((AnnotationType) targetDomainObject).getProjectId();
        } else if (targetDomainObject instanceof Dot) {
            projectId = ((Dot) targetDomainObject).getStroke().getProtocol().getProjectId();
        } else if (targetDomainObject instanceof Stroke) {
            projectId = ((Stroke) targetDomainObject).getProtocol().getProjectId();
        } else if (targetDomainObject instanceof Note) {
            projectId = ((Note) targetDomainObject).getSample().getProjectId();
        } else if (targetDomainObject instanceof Protocol) {
            projectId = ((Protocol) targetDomainObject).getProjectId();
        } else if (targetDomainObject instanceof Sample) {
            projectId = ((Sample) targetDomainObject).getProjectId();
        } else if (targetDomainObject instanceof Text) {
            projectId = ((Text) targetDomainObject).getProjectId();
        }

        if (projectId == null) {
            return false;
        }

        List<Permission> permissions = permissionRepository
            .findAllByUserAndProjectId(
                userLogin.get(),
                projectId
            );

        return permissions.parallelStream()
            .anyMatch(permission ->
                permission.getPermission().equals(requiredPermission.toString())
            );
    }

    @Override
    public boolean hasPermission(
        Authentication auth, Serializable targetId, String targetType, Object requiredPermission) {
        /*if ((auth == null) || (targetType == null) || !(requiredPermission instanceof String)) {
            return false;
        }

        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (!userLogin.isPresent()) {
            return false;
        }

        if (
                targetType.equals(Annotation.class.getCanonicalName()) ||
                targetType.equals(AnnotationType.class.getCanonicalName()) ||
                targetType.equals(Dot.class.getCanonicalName()) ||
                targetType.equals(Layout.class.getCanonicalName()) ||
                targetType.equals(Note.class.getCanonicalName()) ||
                targetType.equals(Protocol.class.getCanonicalName()) ||
                targetType.equals(Sample.class.getCanonicalName()) ||
                targetType.equals(Text.class.getCanonicalName())
        ) {
            // permission check is only available for projects
            List<Permission> permissions = permissionRepository
                .findAllByUserAndProjectId(
                    userLogin.get(),
                    (Long) targetId
                );

            return permissions.parallelStream()
                .anyMatch(permission ->
                    permission.getPermission()
                        .equals(requiredPermission.toString())
                );
        }*/

        return true;
    }

    private Long getProjectIdFromSample(Long id) {
        Optional<Sample> sampleOptional = sampleRepository.findById(id);
        return sampleOptional.map(Sample::getProjectId).orElse(null);
    }

    private Long getProjectIdFromText(Long id) {
        Optional<Text> textOptional = textRepository.findById(id);
        return textOptional
            .map(text -> text.getSample().getProjectId())
            .orElse(null);
    }
}
