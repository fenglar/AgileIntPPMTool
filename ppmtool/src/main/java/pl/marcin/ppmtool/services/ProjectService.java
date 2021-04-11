package pl.marcin.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marcin.ppmtool.domain.Backlog;
import pl.marcin.ppmtool.domain.Project;
import pl.marcin.ppmtool.domain.User;
import pl.marcin.ppmtool.exceptions.ProjectNotFoundException;
import pl.marcin.ppmtool.exceptions.ProjectidException;
import pl.marcin.ppmtool.repositories.BacklogRepository;
import pl.marcin.ppmtool.repositories.ProjectRepository;
import pl.marcin.ppmtool.repositories.UserRepository;

import java.util.Locale;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;


    public Project saveOrUpdateProject(Project project, String username) {
        try {

            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
    backlog.setProject(project);
    backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if (project.getId()!=null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);

        } catch (Exception e) {
            throw new ProjectidException("Project ID " + project.getProjectIdentifier().toUpperCase() + " already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) {
            throw new ProjectidException("Project ID '" + projectId + "does not exist");
        }
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectid, String username) {

        projectRepository.delete(findProjectByIdentifier(projectid, username));
    }
}