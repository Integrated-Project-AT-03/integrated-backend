package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.task.FormTaskDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.FullTaskDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.RequestRemoveFilesDto;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.TaskDtoV3;
import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import com.example.itbangmodkradankanbanapi.entities.V3.TaskAttachment;
import com.example.itbangmodkradankanbanapi.entities.V3.TasksV3;
import com.example.itbangmodkradankanbanapi.exceptions.ConflictException;
import com.example.itbangmodkradankanbanapi.exceptions.InvalidFieldInputException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.NotAllowedException;
import com.example.itbangmodkradankanbanapi.repositories.V3.BoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.StatusRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.TaskAttachmentRepository;
import com.example.itbangmodkradankanbanapi.repositories.V3.TaskRepositoryV3;
import com.example.itbangmodkradankanbanapi.services.V2.SettingService;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskServiceV3 {
    @Autowired
    private TaskRepositoryV3 repository;
    @Autowired
    private StatusRepositoryV3 statusRepository;

    @Autowired
    private BoardRepositoryV3 boardRepository;

    @Autowired
    private TaskAttachmentRepository taskAttachmentRepository;
//
//    @Autowired
//    private SettingService settingService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${value.max-file-per-request}")
    private Integer MAX_FILES;

    @Value("${value.server.storage.address}")
    private String localCloudServer ;

    public FullTaskDtoV3 getTask(Integer id){
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Task "+ id + " dose not exist !!!!")),FullTaskDtoV3.class);
    }


    @Transactional
    public List<TaskAttachment> uploadAttachment(List<MultipartFile> multipartFiles, int taskId) throws IOException {
        if(multipartFiles.size() > MAX_FILES) throw new InvalidFieldInputException("multipartFiles","You can only upload up to " + MAX_FILES + " files at once.");
        TasksV3 tasks =  repository.findById(taskId).orElseThrow(() -> new ItemNotFoundException("Task "+ taskId + " dose not exist !!!!"));
        int countTasks = taskAttachmentRepository.countByTask(tasks);
        if(countTasks >= 10) throw new InvalidFieldInputException("files","Each task can have at most "+ MAX_FILES + " files.");

        List<MultipartFile> filterMultipartFiles;
        if(multipartFiles.size() + countTasks > MAX_FILES )
        filterMultipartFiles = multipartFiles.subList(0,MAX_FILES-countTasks);
        else filterMultipartFiles = multipartFiles;
        String url = localCloudServer+"/task-attachment/" + taskId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile file : filterMultipartFiles) {
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("files", resource);
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<TaskAttachment[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    TaskAttachment[].class
            );
            return response.getBody() != null ? List.of(response.getBody()) : List.of();
        } catch (HttpClientErrorException.Conflict e) {
            throw new ConflictException("File with the same filename cannot be added or updated to the attachments. Please delete the attachment and add again to update the file.");
        }catch (HttpClientErrorException.NotFound e) {
            throw new ItemNotFoundException("Task " + taskId + " does not exist !!!!");
        }
    }

    @Transactional
    public ResponseEntity<Resource> getAttachment(Integer taskId, String fileId) {
        TasksV3 task = repository.findById(taskId)
                .orElseThrow(() -> new ItemNotFoundException("Not Found"));
        TaskAttachment taskAttachment = taskAttachmentRepository.findByIdAndTask(fileId, task);
        if (taskAttachment == null) throw new ItemNotFoundException("Not Found file id: " + fileId + " of task id:" + taskId);

        String filename;
        if (taskAttachment.getType() != null)
        filename = taskAttachment.getId() + '-' + taskAttachment.getTask().getIdTask() + '-' + taskAttachment.getName() + '.' + taskAttachment.getType();
        else filename = taskAttachment.getId() + '-' + taskAttachment.getTask().getIdTask() + '-' + taskAttachment.getName();
        String url = localCloudServer + "/task-attachment/" + filename;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<Resource> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    Resource.class
            );

            // ดึง Content-Disposition header เพื่อเก็บชื่อไฟล์ต้นฉบับ
            String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
            String originalFilename = filename; // ค่า default เผื่อไม่เจอใน Content-Disposition
            if (contentDisposition != null && contentDisposition.contains("filename=\"")) {
                if(taskAttachment.getType() != null)
                originalFilename = taskAttachment.getName()+"."+taskAttachment.getType();
                else  originalFilename = taskAttachment.getName();
            }

            // ส่ง Response กลับพร้อม Content-Disposition header
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFilename + "\"")
                    .contentType(response.getHeaders().getContentType())
                    .body(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            throw new ItemNotFoundException("File not found id " + fileId);
        }
    }



    @Transactional
    public List<TaskAttachment> deleteAttachments(RequestRemoveFilesDto requestRemoveFilesDto) {
        String url = localCloudServer+"/task-attachment";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RequestRemoveFilesDto> requestEntity = new HttpEntity<>(requestRemoveFilesDto, headers);
        try {
            ResponseEntity<List<TaskAttachment>> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    requestEntity,
                    new ParameterizedTypeReference<List<TaskAttachment>>() {}
            );
            return response.getBody() != null ? response.getBody() : null;
        }catch (HttpClientErrorException.NotFound e) {
            throw new ItemNotFoundException("File not found ");
        }
    }







    @Transactional
    public TaskDtoV3 deleteTask(Integer id){

      TasksV3 task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
      taskAttachmentRepository.deleteAll(task.getTasksAttachment());
          repository.delete(task);
        return modelMapper.map(task,TaskDtoV3.class);
    }


    public List<TaskDtoV3> getAllTaskByStatusIdIn(String[] filterStatuses, String[] sortBy,String[] direction) {

        List<Sort.Order> orders = new ArrayList<>();
        if((sortBy.length != 0 && !sortBy[0].equals("status.name"))|| sortBy.length > 1 ) throw new NotAllowedException("invalid filter parameter");
        else if(sortBy.length !=0)
            for (int i = 0; i < sortBy.length; i++) {
                orders.add(new Sort.Order((direction[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC), sortBy[i]));
            }
        else orders.add(new Sort.Order(Sort.Direction.ASC ,"createdOn"));
        if(filterStatuses.length == 0) return  listMapper.mapList(repository.findAll(Sort.by(orders)),TaskDtoV3.class);

        List<StatusV3> statuses = Arrays.stream(filterStatuses).map((filterStatus) -> statusRepository.findByName(filterStatus.replace("_"," "))).toList();
        return listMapper.mapList(repository.findAllByStatusIn(statuses,Sort.by(orders)),TaskDtoV3.class);
    }



    public TaskDtoV3 updateTask(Integer id, FormTaskDtoV3 formTask){

        TasksV3 updateTask = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found"));
        StatusV3 oldStatus = updateTask.getStatus();
        updateTask.setTitle(formTask.getTitle());
        updateTask.setAssignees(formTask.getAssignees());
        updateTask.setDescription(formTask.getDescription());
        StatusV3 status = statusRepository.findById(formTask.getStatusId()).orElseThrow(() -> new InvalidFieldInputException("status","does not exist"));
        Board board = updateTask.getBoard();

        if(status.getCenterStatus() != null) {
            if (!oldStatus.equals(status) && status.getCenterStatus().getEnableConfig() && board.getEnableLimitsTask() && status.getTasks().size() >= board.getLimitsTask())
                throw new InvalidFieldInputException("status", "status cannot be over the limit ");
        }else {
            if (!oldStatus.equals(status) && board.getEnableLimitsTask() && status.getTasks().size() >= board.getLimitsTask())
                throw new InvalidFieldInputException("status", "status cannot be over the limit ");
        }
        updateTask.setStatus(status);

      return  modelMapper.map( repository.save(updateTask),TaskDtoV3.class);
    }

    @Transactional
    public TaskDtoV3 addTask(FormTaskDtoV3 formTask,String nanoId){
        TasksV3 newTask = new TasksV3();
        newTask.setTitle(formTask.getTitle());
        newTask.setAssignees(formTask.getAssignees());
        newTask.setDescription(formTask.getDescription());
        Board board = boardRepository.findById(nanoId).orElseThrow(() -> new InvalidFieldInputException("boardNanoId","does not exist"));
        newTask.setBoard(board);
        StatusV3 status = statusRepository.findById(formTask.getStatusId()).orElseThrow(() -> new InvalidFieldInputException("status","does not exist"));
        if(status.getCenterStatus() != null) {
            if (status.getCenterStatus().getEnableConfig() && board.getEnableLimitsTask() && status.getTasks().size() >= board.getLimitsTask())
                throw new InvalidFieldInputException("status", "status cannot be over the limit");
        }else {
            if (board.getEnableLimitsTask() && status.getTasks().size() >= board.getLimitsTask())
                throw new InvalidFieldInputException("status", "status cannot be over the limit");
        }
        newTask.setStatus(status);



        return modelMapper.map( repository.save(newTask),TaskDtoV3.class);
    }
}
