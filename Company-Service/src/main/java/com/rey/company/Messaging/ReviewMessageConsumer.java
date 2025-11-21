package com.rey.company.Messaging;

import com.rey.company.ServiceImpl.CompanyServiceImpl;
import com.rey.company.DTO.ReviewMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageConsumer {

    private final CompanyServiceImpl companyService;

    public ReviewMessageConsumer(CompanyServiceImpl companyService) {
        this.companyService = companyService;
    }

    @RabbitListener(queues = "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage){
        companyService.updateCompanyRating(reviewMessage);
    }
}
