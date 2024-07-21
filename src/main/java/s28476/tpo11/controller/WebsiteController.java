package s28476.tpo11.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import s28476.tpo11.model.Link;
import s28476.tpo11.model.LinkDTO;
import s28476.tpo11.service.LinkDtoMapper;

import java.util.Locale;

@Controller
public class WebsiteController {

    private final LinkDtoMapper linkDtoMapper;
    private WebClient webClient;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.servlet.context-path}")
    private String serverContext;

    public WebsiteController(LinkDtoMapper linkDtoMapper) {
        this.linkDtoMapper = linkDtoMapper;
    }

    @PostConstruct
    public void init() {
        String apiUrl = "http://" + serverAddress + ":" + serverPort + serverContext;
        this.webClient = WebClient.builder().baseUrl(apiUrl).build();
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("link", new LinkDTO());
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("link", new LinkDTO());
        return "create";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("link", new LinkDTO());
        return "edit";
    }

    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("link", new LinkDTO());
        return "view";
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        model.addAttribute("link", new LinkDTO());
        return "delete";
    }

    @PostMapping("/create")
    public String createLink(@ModelAttribute("link") LinkDTO linkDTO, Model model, BindingResult bindingResult) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            WebClient.ResponseSpec responseSpec = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/api/links").queryParam("lang", locale).build())
                    .bodyValue(linkDTO)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new));

            Link responseLink = responseSpec.bodyToMono(Link.class).block();

            if (responseLink != null) {
                LinkDTO responseLinkDto = linkDtoMapper.mapToDto(responseLink);
                model.addAttribute("link", responseLinkDto);
                model.addAttribute("message", "Link created successfully!");
            } else {
                model.addAttribute("error", "Error creating link: Unknown error");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error creating link: " + e.getMessage());
        }

        return "create";
    }

    @PostMapping("/view")
    public String viewLink(@ModelAttribute("link") LinkDTO linkDTO, Model model, BindingResult bindingResult) {
        Locale locale = LocaleContextHolder.getLocale();
        String linkId = linkDTO.getId();
        try {
            WebClient.ResponseSpec responseSpec = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/links/{id}").queryParam("lang", locale).build(linkId))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new));

            Link responseLink = responseSpec.bodyToMono(Link.class).block();

            if (responseLink != null) {
                LinkDTO responseLinkDto = linkDtoMapper.mapToDto(responseLink);
                model.addAttribute("link", responseLinkDto);
                model.addAttribute("message", "Link retrieved successfully!");
            } else {
                model.addAttribute("error", "Error retrieving link: Unknown error");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error retrieving link: " + e.getMessage());
        }

        return "view";
    }

    @PostMapping("/delete")
    public String deleteLink(@ModelAttribute("link") LinkDTO linkDTO, Model model, BindingResult bindingResult) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            WebClient.ResponseSpec responseSpec = webClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("/api/links/{id}").queryParam("pass", linkDTO.getPassword()).queryParam("lang", locale).build(linkDTO.getId()))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new));

            responseSpec.toBodilessEntity().block();

            model.addAttribute("message", "Link deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting link: " + e.getMessage());
        }

        return "delete";
    }

    @PostMapping("/edit")
    public String editLink(@ModelAttribute("link") LinkDTO linkDTO, BindingResult bindingResult, Model model) {
        try {
            Locale locale = LocaleContextHolder.getLocale();

            WebClient.ResponseSpec responseSpec = webClient.patch()
                    .uri(uriBuilder -> uriBuilder.path("/api/links/{id}").queryParam("lang", locale).build(linkDTO.getId()))
                    .bodyValue(linkDTO)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new));

            ResponseEntity<Void> response = responseSpec.toBodilessEntity().block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "Link updated successfully!");
            } else {
                model.addAttribute("error", "Error updating link: " + (response != null ? response.getStatusCode() : "Unknown error"));
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error updating link: " + e.getMessage());
        }

        return "edit";
    }
}
