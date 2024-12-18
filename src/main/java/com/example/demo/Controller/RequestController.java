package com.example.demo.Controller;

import com.example.demo.model.Request;
import com.example.demo.services.RequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController()
@CrossOrigin
@RequestMapping("api/requests")
public class RequestController {

    private RequestRepository requestRepository;
    public RequestController(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;

    }
    @GetMapping("/getAllReqeusts")
    public ResponseEntity<List<Request>>getAllRequests() {
return new ResponseEntity<>(requestRepository.findAll(), HttpStatus.OK);  }
    @GetMapping("/getRequestById/{id}")
    public ResponseEntity<Request>findRequestById(@PathVariable Long id) {
        Request request=requestRepository.findById(id).orElse(null);
        if(request==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }
    @PostMapping("/addRequest")
    public ResponseEntity<Request>addRequest(@RequestBody  Request request) {
        Request newRequest=requestRepository.save(request);
        return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
    }
    @PutMapping("/updateReqeust/{id}")
    public ResponseEntity<Request>updateReqeust(@RequestBody Request request,@PathVariable Long id){
        Request existingReqeust=requestRepository.findById(id).orElse(null);
        if(existingReqeust==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingReqeust.setRequest(request.getRequest());
        existingReqeust.setDate(request.getDate());
        requestRepository.save(existingReqeust);
        return new ResponseEntity<>(existingReqeust,HttpStatus.OK);
    }
    @DeleteMapping("deleteRequest/{id}")
   public ResponseEntity<Void>deleteRequest(@PathVariable Long id){
        if(requestRepository.existsById(id)){
            requestRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);}
     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }
}
