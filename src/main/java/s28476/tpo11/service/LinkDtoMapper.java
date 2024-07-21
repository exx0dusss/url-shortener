package s28476.tpo11.service;

import org.springframework.stereotype.Service;
import s28476.tpo11.model.*;

@Service
public class LinkDtoMapper {

    public Link map(LinkDTO linkDTO) {
        Link link = new Link();
        String generatedId = LinkGenerator.getRandomSequence();
        link.setId(generatedId);
        link.setName(linkDTO.getName());
        link.setTargetUrl(linkDTO.getTargetUrl());
        link.setVisits(0);
        link.setPassword(linkDTO.getPassword());
        return link;
    }

    public LinkDTO mapToDto(Link link) {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(link.getId());
        linkDTO.setName(link.getName());
        linkDTO.setTargetUrl(link.getTargetUrl());
        linkDTO.setRedirectUrl(link.getRedirectUrl());
        linkDTO.setVisits(link.getVisits());
        return linkDTO;
    }
}
