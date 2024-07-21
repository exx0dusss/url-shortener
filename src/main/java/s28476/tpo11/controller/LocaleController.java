package s28476.tpo11.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@Controller
public class LocaleController {

    @GetMapping("/locale")
    public String changeLocale(HttpServletRequest request, HttpServletResponse response, @RequestParam("lang") String lang) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            localeResolver.setLocale(request, response, Locale.forLanguageTag(lang));
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
