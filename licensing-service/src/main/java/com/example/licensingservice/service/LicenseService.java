package com.example.licensingservice.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.licensingservice.configuration.ServiceConfig;
import com.example.licensingservice.entity.License;
import com.example.licensingservice.repository.LicenseRepository;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final MessageSource messages;
    private final LicenseRepository licenseRepository;
    private final ServiceConfig serviceConfig;

    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository
                .findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                messages.getMessage("license.search.error.message",
                                        new String[] { licenseId, organizationId },
                                        null)));
        return license.withComment(serviceConfig.getProperty());
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(serviceConfig.getProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);
        return license.withComment(serviceConfig.getProperty());

    }

    public String deleteLicense(String licenseId) {
        String responseMessage = null;
        License license = License.builder().licenseId(licenseId).build();
        licenseRepository.delete(license);
        responseMessage = String.format(messages.getMessage("license.delete.message", null, null), licenseId);
        return responseMessage;
    }
}