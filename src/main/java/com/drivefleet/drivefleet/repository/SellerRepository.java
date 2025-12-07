package com.drivefleet.drivefleet.repository;

import com.drivefleet.drivefleet.domain.entities.Seller;
import com.drivefleet.drivefleet.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Optional<Seller> findByRegistrationNumber(Long registrationNumber);
    boolean existsByRegistrationNumber(Long registrationNumber);

    @Query("SELECT s FROM Seller s WHERE s.user.status = :status")
    List<Seller> findAllActive(@Param("status") UserStatus status);
}
