package net.thumbtack.school.spring.service;

import net.thumbtack.school.spring.model.Recording;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

public interface PromotionService {

    Logger LOGGER = LoggerFactory.getLogger(PromotionService.class);

    void createCampaign(Recording recording, ZonedDateTime campaignCreateDate);

    void stopCampaign(int campaignID);

    void deleteCampaign(int campaignID);
}
