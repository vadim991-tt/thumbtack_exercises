package net.thumbtack.school.spring.operations;

import net.thumbtack.school.spring.data.DataStorage;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.music.PublishingChannel;
import net.thumbtack.school.spring.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class StartCampaignTwoWeeks implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartCampaignTwoWeeks.class);
    private final DataStorage dataStorage;
    private final PublishingChannel iTunes;
    private final PublishingChannel yandex;
    private final PublishingChannel youtube;
    private final PromotionService service;

    @Autowired
    public StartCampaignTwoWeeks(@Qualifier("audioStorage") DataStorage dataStorage, @Qualifier("iTunes") PublishingChannel iTunes, @Qualifier("Yandex") PublishingChannel yandex, @Qualifier("YouTube") PublishingChannel youtube, @Qualifier("service1") PromotionService service) {
        this.dataStorage = dataStorage;
        this.iTunes = iTunes;
        this.yandex = yandex;
        this.youtube = youtube;
        this.service = service;
    }

    @Override
    public void run(String... args) {
        LOGGER.info("Runner was started");
        LOGGER.info("Publish {} and create campaign after 2 weeks", "SongName");


        Recording recording = dataStorage.get(0);
        ZonedDateTime publishAvailableDate = ZonedDateTime.now();
        ZonedDateTime campaignCreateDate = publishAvailableDate.plusWeeks(2);

        iTunes.publish(recording, publishAvailableDate);
        yandex.publish(recording, publishAvailableDate);
        youtube.publish(recording, publishAvailableDate);
        service.createCampaign(recording, campaignCreateDate);

        LOGGER.info("Runner was stopped");
    }

}