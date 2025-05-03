package com.psnrwanda.api.config;

import com.psnrwanda.api.model.Service;
import com.psnrwanda.api.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Initializer for populating services data
 */
@Component
@Order(2) // Run after other initializers
@RequiredArgsConstructor
@Slf4j
public class ServiceDataInitializer implements CommandLineRunner {

    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public void run(String... args) {
        initializeServices();
    }

    /**
     * Initialize services if none exist
     */
    private void initializeServices() {
        if (serviceRepository.count() > 0) {
            log.info("Services already exist in the database, skipping initialization");
            return;
        }

        log.info("Initializing services data");

        // Create and save services
        createNotaryService();
        createContractDraftingService();
        createLegalConsultancyService();
        createRealEstateService();
        createAccountingService();
        createLeasingService();

        log.info("Services initialization completed");
    }

    private void createNotaryService() {
        Service service = Service.builder()
                .title("Notary Services")
                .description("Professional notary services for document authentication and legal validation")
                .active(true)
                .imageUrl("notary.jpg")
                .bulletPoints(Arrays.asList(
                        "Professional notary services for",
                        "for document authentication and",
                        "Professional notary services for",
                        "Fast turnaround"
                ))
                .turnaroundTime("24-48 hours")
                .ctaText("Book This Service")
                .priceInfo("Starting from RWF 25,000")
                .additionalInfo("Our notary services include document authentication, contract review, and legal validation.")
                .build();

        serviceRepository.save(service);
    }
    
    private void createContractDraftingService() {
        Service service = Service.builder()
                .title("Contract Drafting and Review")
                .description("Professional contract drafting and comprehensive legal document review services")
                .active(true)
                .imageUrl("contract.jpg")
                .bulletPoints(Arrays.asList(
                        "Expert contract drafting services",
                        "Comprehensive legal document review",
                        "Custom clauses and provisions",
                        "Detailed feedback and recommendations"
                ))
                .turnaroundTime("3-5 business days")
                .ctaText("Book This Service")
                .priceInfo("Starting from RWF 35,000")
                .additionalInfo("Our contract services include drafting new agreements, reviewing existing contracts, and providing legal recommendations to protect your interests.")
                .build();

        serviceRepository.save(service);
    }

    private void createLegalConsultancyService() {
        Service service = Service.builder()
                .title("Legal Consultancy")
                .description("Expert legal advice and consultancy for individuals and businesses")
                .active(true)
                .imageUrl("legal.jpg")
                .bulletPoints(Arrays.asList(
                        "Expert legal advice and",
                        "and consultancy for individuals",
                        "Expert legal advice and",
                        "Fast turnaround"
                ))
                .turnaroundTime("1-3 days")
                .ctaText("Book This Service")
                .priceInfo("Starting from RWF 30,000")
                .additionalInfo("Our legal consultancy services include contract review, legal advice, and business compliance.")
                .build();

        serviceRepository.save(service);
    }

    private void createRealEstateService() {
        Service service = Service.builder()
                .title("Real Estate Services")
                .description("Professional real estate brokerage and advisory services")
                .active(true)
                .imageUrl("realestate.jpg")
                .bulletPoints(Arrays.asList(
                        "Professional real estate brokerage",
                        "brokerage and advisory services",
                        "estate brokerage and advisory",
                        "Fast turnaround"
                ))
                .turnaroundTime("Variable")
                .ctaText("Book This Service")
                .priceInfo("Commission-based")
                .additionalInfo("Our real estate services include property valuation, brokerage, and investment advisory.")
                .build();

        serviceRepository.save(service);
    }

    private void createAccountingService() {
        Service service = Service.builder()
                .title("Accounting Services")
                .description("Financial accounting and bookkeeping services for businesses")
                .active(true)
                .imageUrl("accounting.jpg")
                .bulletPoints(Arrays.asList(
                        "Financial accounting and bookkeeping",
                        "bookkeeping services for businesses",
                        "and bookkeeping services for",
                        "Fast turnaround"
                ))
                .turnaroundTime("3-5 days")
                .ctaText("Book This Service")
                .priceInfo("Starting from RWF 50,000/month")
                .additionalInfo("Our accounting services include bookkeeping, financial statements, and tax preparation.")
                .build();

        serviceRepository.save(service);
    }

    private void createLeasingService() {
        Service service = Service.builder()
                .title("Leasing Services")
                .description("Professional leasing and property management services")
                .active(true)
                .imageUrl("leasing.jpg")
                .bulletPoints(Arrays.asList(
                        "Professional leasing and property",
                        "Professional leasing and property",
                        "Tailored solutions",
                        "Fast turnaround"
                ))
                .turnaroundTime("Variable")
                .ctaText("Book This Service")
                .priceInfo("Commission-based")
                .additionalInfo("Our leasing services include property management, tenant screening, and lease administration.")
                .build();

        serviceRepository.save(service);
    }
} 