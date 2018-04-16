package io.pivotal.pal.tracker.backlog;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }
    private Map<Long, ProjectInfo > projectMap = new ConcurrentHashMap<>();
    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo myProject = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        projectMap.put(projectId, myProject);
        return myProject;
    }
    public ProjectInfo getProjectFromCache(long projectId) {
        return projectMap.getOrDefault(projectId,null);
    }
}
