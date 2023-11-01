package com.example.licensingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.licensingservice.entity.License;
import com.example.licensingservice.service.LicenseService;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
@RequiredArgsConstructor
public class LicenseController {

        private final LicenseService licenseService;

        @GetMapping(value = "/{licenseId}")
        public ResponseEntity<License> getLicense(
                        @PathVariable("organizationId") String organizationId,
                        @PathVariable("licenseId") String licenseId) {

                License license = licenseService
                                .getLicense(licenseId, organizationId);
                license.add(linkTo(methodOn(LicenseController.class)
                                .getLicense(organizationId, license.getLicenseId()))
                                .withSelfRel(),
                                linkTo(methodOn(LicenseController.class)
                                                .createLicense(license))
                                                .withRel("createLicense"),
                                linkTo(methodOn(LicenseController.class)
                                                .updateLicense(organizationId, license))
                                                .withRel("updateLicense"),
                                linkTo(methodOn(LicenseController.class)
                                                .deleteLicense(license.getLicenseId()))
                                                .withRel("deleteLicense"));
                return ResponseEntity.ok(license);
        }

        @PutMapping
        public ResponseEntity<License> updateLicense(
                        @PathVariable("organizationId") String organizationId,
                        @RequestBody License request) {
                return ResponseEntity.ok(licenseService.updateLicense(request));
        }

        @PostMapping
        public ResponseEntity<License> createLicense(@RequestBody License request) {
                return ResponseEntity.ok(licenseService.createLicense(request));
        }

        @DeleteMapping(value = "/{licenseId}")
        public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
                return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
        }
}
