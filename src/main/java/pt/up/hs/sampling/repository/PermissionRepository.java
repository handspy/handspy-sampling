package pt.up.hs.sampling.repository;

import pt.up.hs.sampling.domain.Permission;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Spring Data  repository for the Permission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findAllByUserAndProjectId(@NotNull String user, Long projectId);

    List<Permission> findAllByUser(@NotNull String user);

    List<Permission> findAllByProjectId(Long project_id);

    void deleteAllByUserAndProjectId(@NotNull String user, Long projectId);

    void deleteAllByProjectId(Long projectId);

    void deleteAllByUserAndProjectIdAndPermissionIn(
        @NotNull String user, Long project_id, @NotNull List<String> permissions);
}
