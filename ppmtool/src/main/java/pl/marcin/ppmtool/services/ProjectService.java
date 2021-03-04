package pl.marcin.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marcin.ppmtool.domain.Project;
import pl.marcin.ppmtool.exceptions.ProjectidException;
import pl.marcin.ppmtool.repositories.ProjectRepository;

import java.util.Locale;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
try{
    project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
    return projectRepository.save(project);
}
catch(Exception e) {
    throw new ProjectidException("Project ID "+project.getProjectIdentifier().toUpperCase()+" already exists");
}

    }

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
if (project == null){
    throw new ProjectidException("Project ID '"+projectId+"does not exist");
}
return project;
    }
public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
}
public void deleteProjectByIdentifier(String projectid){
        Project project = projectRepository.findByProjectIdentifier(projectid);
        if(projectid==null){
            throw new ProjectidException("Cannot Project with ID"+projectid+". This project does not exist");
        }
        projectRepository.delete(project);
}
}