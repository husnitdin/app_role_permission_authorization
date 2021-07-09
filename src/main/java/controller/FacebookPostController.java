package controller;
import entity.FacebookPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repository.FacebookPostRepository;
import java.util.Optional;

@RestController
@RequestMapping("/api/facebookpost")
public class FacebookPostController {

    @Autowired
    FacebookPostRepository facebookPostRepository;

    @PreAuthorize("hasAnyRole('USER','OWNER', 'ADMIN')")
    @GetMapping
    public HttpEntity<?> getFacebookPost(){
        return ResponseEntity.ok( facebookPostRepository.findAll() );
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public HttpEntity<?> addFacebookPost(@RequestBody FacebookPost facebookPost){
        return ResponseEntity.ok( facebookPostRepository.save(facebookPost) );
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{id}")
    public HttpEntity<?> editFacebookPost(@PathVariable Integer id, @RequestBody FacebookPost facebookPost){
        Optional<FacebookPost> optionalFacebookPost = facebookPostRepository.findById(id);
        if(optionalFacebookPost.isPresent()){
            FacebookPost editingFacebookPost = optionalFacebookPost.get();
            editingFacebookPost.setName(facebookPost.getName());
            editingFacebookPost.setPostDate(facebookPost.getPostDate());

            facebookPostRepository.save(editingFacebookPost);
            return ResponseEntity.ok(editingFacebookPost);
        }
        return ResponseEntity.status(409).build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteFacebookPost(@PathVariable Integer id){
        facebookPostRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/{id}")
    public HttpEntity<?> getFacebookPost(@PathVariable Integer id){
        Optional<FacebookPost> optionalFacebookPost = facebookPostRepository.findById(id);
        if(optionalFacebookPost.isPresent()){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

}
