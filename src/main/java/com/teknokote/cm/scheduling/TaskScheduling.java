package com.teknokote.cm.scheduling;

import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.service.CardService;
import com.teknokote.cm.dto.CardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@Component
@Slf4j
public class TaskScheduling {
    @Autowired
    private CardService cardService;
    @Scheduled(cron = "0/30 * * * * *")
    @Transactional
    public void checkAndFreeStaleCards() {
        LocalDateTime now = LocalDateTime.now();
        List<CardDto> cardDtoList = cardService.findAll();
        for (CardDto cardDto : cardDtoList){
            Duration range = Duration.between(cardDto.getDateStatusChange(),now);
            if (!cardDto.getStatus().equals(EnumCardStatus.FREE) && !cardDto.getStatus().equals(EnumCardStatus.IN_USE)){
                if (range.toMinutes() >= 3){
                    cardService.updateCardStatus(cardDto.getId(),null,null,EnumCardStatus.FREE);
                    log.info("Card {} has been freed after timeout", cardDto.getCardId());
                }
            }
        }
    }
}
