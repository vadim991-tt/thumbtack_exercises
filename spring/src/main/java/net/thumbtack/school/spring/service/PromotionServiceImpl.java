package net.thumbtack.school.spring.service;

import net.thumbtack.school.spring.model.Recording;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service("service1")
public class PromotionServiceImpl implements PromotionService {
    @Override
    public void createCampaign(Recording recording, ZonedDateTime campaignCreateDate) {
        LOGGER.info("{} campaign was planned on {}", recording.getName(), campaignCreateDate);
    }

    @Override
    public void stopCampaign(int campaignID) {
        LOGGER.info("Campaign with {} ID was stopped successfully at {}", campaignID, ZonedDateTime.now());
    }

    @Override
    public void deleteCampaign(int campaignID) {
        LOGGER.info("Campaign with {} ID was deleted successfully at {}", campaignID, ZonedDateTime.now());
    }


}
