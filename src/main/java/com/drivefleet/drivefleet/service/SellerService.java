package com.drivefleet.drivefleet.service;

import com.drivefleet.drivefleet.domain.dto.seller.SellerRequest;
import com.drivefleet.drivefleet.domain.dto.seller.SellerResponse;
import com.drivefleet.drivefleet.domain.entities.Seller;
import com.drivefleet.drivefleet.domain.entities.User;
import com.drivefleet.drivefleet.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserService userService;
    private final SalesOrderService salesOrderService;

    private SellerResponse convertToResponse(Seller seller) {
        return SellerResponse.builder()
                .id(seller.getId())
                .registrationNumber(seller.getRegistrationNumber())
                .user(userService.convertToResponse(seller.getUser()))
                .sales(seller.getSales()
                        .stream()
                        .map(salesOrderService::convertToResponse)
                        .toList()
                )
                .build();
    }

    private Long generateRegistrationNumber() {
        Long number;
        do {
            number = ThreadLocalRandom.current()
                    .nextLong(10_000_000L, 99_999_999L);
        } while (sellerRepository.existsByRegistrationNumber(number));
        return number;
    }

    public SellerResponse create(SellerRequest request) {
        User user = userService.create(request.user());
        Seller seller = Seller.builder()
                .registrationNumber(generateRegistrationNumber())
                .user(user)
                .sales(new ArrayList<>())
                .build();
        sellerRepository.save(seller);
        return convertToResponse(seller);
    }
}
