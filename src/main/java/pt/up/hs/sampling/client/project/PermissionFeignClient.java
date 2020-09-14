package pt.up.hs.sampling.client.project;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.up.hs.sampling.client.AuthorizedUserFeignClient;
import pt.up.hs.sampling.client.project.dto.ProjectPermissionsDTO;

@AuthorizedUserFeignClient(name = "project")
public interface PermissionFeignClient {

    @RequestMapping(value = "/api/projects/{projectId}/permissions/{user}", method = RequestMethod.GET)
    ProjectPermissionsDTO getUserPermissionsInProject(
        @PathVariable("projectId") Long projectId,
        @PathVariable("user") String user
    );
}
