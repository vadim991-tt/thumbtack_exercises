package net.thumbtack.school.notes.endpoint;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dto.request.section.CreateSectionDtoRequest;
import net.thumbtack.school.notes.dto.request.section.RenameSectionDtoRequest;
import net.thumbtack.school.notes.dto.response.section.CreateSectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.SectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.RenameSectionDtoResponse;
import net.thumbtack.school.notes.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionsEndpoint {

    private final SectionService sectionService;

    @Autowired
    public SectionsEndpoint(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public CreateSectionDtoResponse createSection(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                                  @RequestBody @Valid CreateSectionDtoRequest req) throws ServerException {
        return sectionService.createSection(req, javaSessionId);
    }


    @PutMapping(value = "/{id}")
    public RenameSectionDtoResponse renameSection(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                                  @RequestBody @Valid RenameSectionDtoRequest req,
                                                  @PathVariable("id") int id) throws ServerException {
        return sectionService.renameSection(req, javaSessionId, id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteSection(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                       @PathVariable("id") int id) throws ServerException {
        sectionService.deleteSection(javaSessionId, id);
    }

    @GetMapping(value = "/{id}")
    public SectionDtoResponse getSectionInfo(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                             @PathVariable("id") int id) throws ServerException {
        return sectionService.getSectionInfo(javaSessionId, id);
    }

    @GetMapping
    public List<SectionDtoResponse> getSections(@CookieValue(value = "JAVASESSIONID") String javaSessionId) throws ServerException {
        return sectionService.getSections(javaSessionId);
    }

}
