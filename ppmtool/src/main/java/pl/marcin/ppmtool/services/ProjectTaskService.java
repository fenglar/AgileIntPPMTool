package pl.marcin.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marcin.ppmtool.domain.Backlog;
import pl.marcin.ppmtool.domain.ProjectTask;
import pl.marcin.ppmtool.repositories.BacklogRepository;
import pl.marcin.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        //PTs to be added to a specific project, project != null, BL exists
        Backlog backlog= backlogRepository.findByProjectIdentifier(projectIdentifier);
        //set the bl to pl
        projectTask.setBacklog(backlog);
        //we want our poject sequence to be like this: IDPRO-1 IDPRO-2
        Integer BacklogSequence = backlog.getPTSequence();
        //Update the BL sequence
        BacklogSequence++;

        backlog.setPTSequence(BacklogSequence);
        //add sequence to project task
        projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);
        //initial priority when priority null
        if(projectTask.getPriority()==null){
             projectTask.setPriority(3);
        }
        //initial status when status is null
        if (projectTask.getStatus()==""||projectTask.getStatus()==null){ // in the future we need projectTask.getPriority()==0 to handle the form
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);

    }
    public Iterable<ProjectTask>findBacklogById(String id){
return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

}
