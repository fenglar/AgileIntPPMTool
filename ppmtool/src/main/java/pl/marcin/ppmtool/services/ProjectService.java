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

}