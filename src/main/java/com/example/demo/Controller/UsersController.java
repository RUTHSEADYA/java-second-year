package com.example.demo.Controller;

import com.example.demo.model.Users;
import com.example.demo.services.UsersRepository;
import org.apache.catalina.User;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/users")
@RestController
@CrossOrigin
public class UsersController {

private UsersRepository usersRepository;
public UsersController(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
}

@PostMapping("/signUp")
public ResponseEntity<Users> signUp(@RequestBody Users user) {
    if (user.getUsername() == null || user.getUsername().isEmpty() ||
            user.getEmail()==null || user.getEmail().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty()) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // שדות ריקים - החזר
    }
    List<Users> users = usersRepository.findAll();
    for (Users u : users) {
        if (u.getUsername().equals(user.getUsername()))
            return new ResponseEntity<>(u, HttpStatus.CONFLICT);
    }
    System.out.println("User Type Received: " + user.getUserType());

        Users newUser = usersRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }

    @PostMapping("/Log_in")
    public ResponseEntity <Users> Log_in(@RequestBody Users  newuser){
        if (newuser.getUsername() == null || newuser.getUsername().isEmpty() ||
                newuser.getPassword() == null || newuser.getPassword().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // שדות ריקים - החזר 409
        }

        List<Users> useresList=usersRepository.findAll();

        for(Users u:useresList){
            if (u.getUsername() != null && u.getUsername().equals(newuser.getUsername())) {
                if (u.getPassword().equals(newuser.getPassword())){
                    return new ResponseEntity<>(u, HttpStatus.OK);
                }
                if(u.getUsername() != null &&u.getPassword()!=null&&u.getUsername().equals(newuser.getUsername())){
                    if (!(u.getPassword().equals(newuser.getPassword()))){
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}

            }
            }
        }

        return new ResponseEntity<>(new Users(), HttpStatus.NOT_FOUND);
    }
    //Get----מקבלת קוד מחזירה אובייקט
    @GetMapping( "/getUsers/{id}")
    public ResponseEntity<Users> getUsers(@PathVariable Long id) {
        Users s = usersRepository.getById(id);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }
    @GetMapping("/getUser/{id}")
    public ResponseEntity<Users> getUser(@PathVariable Long id) {
        Users s = usersRepository.findById(id).orElse(null);
        if (s == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(s, HttpStatus.OK);
    }
    @GetMapping("/getUserByName/{name}")
    public ResponseEntity<Users> getUserByName(@PathVariable String name) {
        List<Users> list = usersRepository.findAll();
        for (Users u : list) {
            if (u.getUsername().equals(name))
                return new ResponseEntity<>(u,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //Get--מחזירה רשימה של אובייקטים
    @GetMapping("/getUsers")
    public ResponseEntity<List<Users>> getUsers() {
        return new ResponseEntity<>(usersRepository.findAll(), HttpStatus.OK);
    }

        @PutMapping("/updateUser/{id}")
    public ResponseEntity<Users> updateUser(@RequestBody Users users, @PathVariable Long id) {
        Users existingUser = usersRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
       existingUser.setUsername(users.getUsername());
       existingUser.setPassword(users.getPassword());
            existingUser.setEmail(users.getEmail());
            usersRepository.save(existingUser);
            return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    //delete--מחיקת אובייקט מהטבלה
    @DeleteMapping("/deletUser/{id}")
    public ResponseEntity<Void> deletUser(@PathVariable Long id){
        usersRepository.deleteById(id);
        return ResponseEntity.noContent().build();  // מחזיר 204
    }

    //     Post--הוספה של אובייקט לטבלה
    @PostMapping("/addUser")
    public ResponseEntity<Users> addUser(@RequestBody Users users) {
        List<Users> useresList = usersRepository.findAll();
        for (Users u : useresList) {
            if (u.getUsername()!=null&& u.getPassword()!=null && u.getUsername() .equals(users.getUsername()) && u.getPassword() .equals(users.getPassword())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }
        Users newUsers = usersRepository.save(users);
        return new ResponseEntity<>(newUsers, HttpStatus.CREATED);

    }

}
