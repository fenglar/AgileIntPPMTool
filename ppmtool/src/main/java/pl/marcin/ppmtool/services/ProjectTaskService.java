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

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
  private  ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

    //PTs to be added to a specific project, project != null, BL exists
    Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);

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
    if ( projectTask.getPriority() == null || projectTask.getPriority()==0) {
        projectTask.setPriority(3);
    }
    //initial status when status is null
    if (projectTask.getStatus() == "" || projectTask.getStatus() == null) { // in the future we need projectTask.getPriority()==0 to handle the form
        projectTask.setStatus("TO_DO");
    }

    return projectTaskRepository.save(projectTask);


        }

    public Iterable<ProjectTask>findBacklogById(String id, String username){

projectService.findProjectByIdentifier(id,username);

return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {

        projectService.findProjectByIdentifier(backlog_id,username);

        ProjectTask projectTask=projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask==null){
            throw new ProjectNotFoundException("ProjectTask with ID: '"+pt_id+"' not found");
        }
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: "+backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSeqence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        ProjectTask projectTask=findPTByProjectSequence(backlog_id, pt_id, username);

        projectTask=updatedTask;

        return projectTaskRepository.save(projectTask);
    }



    public void deletePTByProjectSequence (String backlog_id, String pt_id, String username){
        ProjectTask projectTask=findPTByProjectSequence(backlog_id, pt_id, username);
          projectTaskRepository.delete(projectTask);
    }



}
