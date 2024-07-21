package s28476.tpo11.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import s28476.tpo11.model.Link;
import s28476.tpo11.model.LinkDTO;
import s28476.tpo11.repository.LinkRepository;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final LinkDtoMapper linkDtoMapper;
    private final LinkGenerator linkGenerator;
    private final MessageSource messageSource;

    @Autowired
    public LinkService(LinkRepository linkRepository, LinkDtoMapper linkDtoMapper, LinkGenerator linkGenerator, MessageSource messageSource) {
        this.linkRepository = linkRepository;
        this.linkDtoMapper = linkDtoMapper;
        this.linkGenerator = linkGenerator;
        this.messageSource = messageSource;
    }


    public Link saveLink(LinkDTO linkDTO) {
        if (linkRepository.findByName(linkDTO.getName()).isPresent()) {
            throw new IllegalArgumentException(getMessage("error.link.name.exists"));
        }
        Link link = linkDtoMapper.map(linkDTO);
        String linkId = link.getId();
        String redirectUrl = linkGenerator.getLink("red", linkId);
        link.setRedirectUrl(redirectUrl);
        return linkRepository.save(link);

    }

    public Optional<Link> getLinkById(String id) {
        return linkRepository.findById(id);
    }

    public Optional<Link> getLinkByName(String name) {
        return linkRepository.findByName(name);
    }

    public void incrementVisits(Link link) {
        link.setVisits(link.getVisits() + 1);
        linkRepository.save(link);
    }

    public void updateLink(Link link) {
        linkRepository.save(link);

    }

    public void deleteLink(String id, String password) {
        Optional<Link> existingLinkOpt = linkRepository.findById(id);
        if (existingLinkOpt.isPresent()) {
            Link existingLink = existingLinkOpt.get();
            if (!existingLink.getPassword().equals(password)) {
                throw new IllegalArgumentException(getMessage("error.wrong.password"));
            }
            linkRepository.delete(existingLink);
        }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
