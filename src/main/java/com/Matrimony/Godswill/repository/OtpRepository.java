package com.Matrimony.Godswill.repository;

import com.Matrimony.Godswill.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<Otp, String> {

    Optional<Otp> findByEmailAndPurpose(String email, String purpose);

    Optional<Otp> findByPhoneAndPurpose(String phone, String purpose);

    Optional<Otp> findByEmailAndOtpCode(String email, String otpCode);

    Optional<Otp> findByPhoneAndOtpCode(String phone, String otpCode);

    void deleteByEmailAndPurpose(String email, String purpose);

    void deleteByPhoneAndPurpose(String phone, String purpose);
}