package net.thumbtack.school.spring.endpoint;


import net.thumbtack.school.spring.model.DtoResponse;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.service.PromotionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignEndpoint {
    private final PromotionServiceImpl promotionService;

    @Autowired
    public CampaignEndpoint(PromotionServiceImpl promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DtoResponse startCampaign(@RequestBody @Valid Recording recording) {
        promotionService.createCampaign(recording, ZonedDateTime.now());
        return new DtoResponse(String.format("Campaign for %s was started successfully", recording.getName()));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/status/stop")
    public DtoResponse stopCampaign(@PathVariable("id") int campaignId) {
        return new DtoResponse(String.format("Campaign with %s ID was stopped successfully", campaignId));

    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public DtoResponse deleteCampaign(@PathVariable("id") int campaignId
                                      ) {
        promotionService.deleteCampaign(campaignId);
        return new DtoResponse(String.format("Campaign with %s ID was deleted successfully", campaignId));
    }


}
