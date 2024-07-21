package s28476.tpo11.controller;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import s28476.tpo11.model.Link;
import s28476.tpo11.service.LinkService;
import java.net.URI;
import java.util.Optional;

@RestController
public class RedirectController {
    private final LinkService linkService;

    public RedirectController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/red/{id}")
    public ResponseEntity<Void> redirectLink(@PathVariable String id) {
        Optional<Link> link = linkService.getLinkById(id);
        if (link.isPresent()) {
            Link targetLink = link.get();
            linkService.incrementVisits(targetLink);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(targetLink.getTargetUrl()));
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
