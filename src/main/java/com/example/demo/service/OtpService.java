package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStorage.put(email, new OtpEntry(otp, LocalDateTime.now().plusMinutes(5))); 
        return otp;
    }

    public boolean validateOtp(String email, String userOtp) {
        OtpEntry otpEntry = otpStorage.get(email);

        if (otpEntry == null || otpEntry.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false; 
        }

        return otpEntry.getOtp().equals(userOtp);  
    }

    public boolean resetPassword(String email, String userOtp, String newPassword) {
        OtpEntry otpEntry = otpStorage.get(email);

        if (otpEntry == null || !otpEntry.getOtp().equals(userOtp)) {
            return false;  
        }

        boolean passwordUpdated = updateUserPassword(email, newPassword);
        if (passwordUpdated) {
            otpStorage.remove(email); 
            return true;
        }

        return false;
    }

    private boolean updateUserPassword(String email, String newPassword) {
        System.out.println("Password updated for: " + email);
        return true;
    }

    private static class OtpEntry {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpEntry(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}
