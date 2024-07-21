package s28476.tpo11.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import s28476.tpo11.model.Link;
import s28476.tpo11.model.LinkDTO;
import s28476.tpo11.service.LinkDtoMapper;
import s28476.tpo11.service.LinkService;

import java.net.URI;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/links",
        produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE})
public class ApiController {
    private final LinkService linkService;
    private final Validator validator;
    private final MessageSource messageSource;

    public ApiController(LinkService linkService, Validator validator, MessageSource messageSource) {
        this.linkService = linkService;
        this.validator = validator;
        this.messageSource = messageSource;
    }

    @Tag(name = "POST", description = "Add new link")
    @PostMapping
    public ResponseEntity<?> saveLink(@Valid @RequestBody LinkDTO linkDTO, @RequestHeader(value = "Accept-Language", required = false) String lang) {
        setLocale(lang);
        try {
            Link savedLink = linkService.saveLink(linkDTO);
            URI savedLinkLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedLink.getId())
                    .toUri();
            return ResponseEntity.created(savedLinkLocation).body(savedLink);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Tag(name = "GET", description = "Get link information")
    @GetMapping("/{id}")
    public ResponseEntity<?> getLink(@PathVariable String id, @RequestHeader(value = "Accept-Language", required = false) String lang) {
        setLocale(lang);
        try {
            Optional<Link> link = linkService.getLinkById(id);
            if (link.isPresent()) {
                return ResponseEntity.ok(link.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessage("error.link.not.found"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Tag(name = "PATCH", description = "Update an existing link")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateLink(@PathVariable String id, @RequestBody LinkDTO linkDTO, @RequestHeader(value = "Accept-Language", required = false) String lang) {
        setLocale(lang);
        try {
            Optional<Link> existingLinkOpt = linkService.getLinkById(id);
            if (existingLinkOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessage("error.link.not.found"));
            }
            Link existingLink = existingLinkOpt.get();
            String dtoPass = linkDTO.getPassword();
            String existingPass = existingLink.getPassword();
            if (dtoPass != null && !dtoPass.equals(existingPass)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(getMessage("error.wrong.password"));
            }

            if (!Objects.equals(linkDTO.getTargetUrl(), "")) {
                existingLink.setTargetUrl(linkDTO.getTargetUrl());
            }
            if (!Objects.equals(linkDTO.getName(), "")) {
                existingLink.setName(linkDTO.getName());
            }

            validator.validate(existingLink);
            linkService.updateLink(existingLink);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessage("error.link.not.found"));
        }
    }

    @Tag(name = "DELETE", description = "Delete an existing link")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLink(@PathVariable String id, @RequestParam String pass, @RequestHeader(value = "Accept-Language", required = false) String lang) {
        setLocale(lang);
        try {
            linkService.deleteLink(id, pass);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessage("error.link.not.found"));
        }
    }

    private void setLocale(String lang) {
        if (lang != null && !lang.isEmpty()) {
            Locale locale = new Locale(lang);
            LocaleContextHolder.setLocale(locale);
        }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
