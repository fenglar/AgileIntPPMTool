package pl.marcin.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marcin.ppmtool.domain.Backlog;
import pl.marcin.ppmtool.domain.Project;
import pl.marcin.ppmtool.domain.ProjectTask;
import pl.marcin.ppmtool.exceptions.ProjectNotFoundException;
import pl.marcin.ppmtool.repositories.BacklogRepository;
import pl.marcin.ppmtool.repositories.ProjectRepository;
import pl.marcin.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
try {
    //PTs to be added to a specific project, project != null, BL exists
    Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

    //set the bl to pl
    projectTask.setBacklog(backlog);
    //we want our poject sequence to be like this: IDPRO-1 IDPRO-2
    Integer BacklogSequence = backlog.getPTSequence();
    //Update the BL sequence
    BacklogSequence++;

    backlog.setPTSequence(BacklogSequence);
    //add sequence to project task
    projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
    projectTask.setProjectIdentifier(projectIdentifier);
    //initial priority when priority null
    if (projectTask.getPriority() == null) {
        projectTask.setPriority(3);
    }
    //initial status when status is null
    if (projectTask.getStatus() == "" || projectTask.getStatus() == null) { // in the future we need projectTask.getPriority()==0 to handle the form
        projectTask.setStatus("TO_DO");
    }

    return projectTaskRepository.save(projectTask);
} catch(Exception e){
    throw new ProjectNotFoundException("Project Not Found");
}

        }

    public Iterable<ProjectTask>findBacklogById(String id){

Project project = projectRepository.findByProjectIdentifier(id);
if(project==null){
    throw new ProjectNotFoundException("Project with ID: "+id+" does not exist");
}

return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
        }
        ProjectTask projectTask=projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask==null){
            throw new ProjectNotFoundException("ProjectTask with ID: '"+pt_id+"' not found");
        }
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: "+backlog_id);
        }
        return projectTask;
    }

}
