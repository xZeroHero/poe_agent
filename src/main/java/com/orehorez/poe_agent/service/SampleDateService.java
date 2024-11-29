package com.orehorez.poe_agent.service;


import com.orehorez.poe_agent.Classes.SampleDate;
import com.orehorez.poe_agent.repository.SampleDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class SampleDateService {

    private final SampleDateRepository sampleDateRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");


    @Autowired
    public SampleDateService(SampleDateRepository sampleDateRepository) {
        this.sampleDateRepository = sampleDateRepository;
    }

    public SampleDate findSampleDateByDateString(String localDateString) {

        LocalDateTime localDateTime = LocalDateTime.parse(localDateString, formatter);

        return sampleDateRepository
                .findSampleDateByDate(localDateTime)
                .orElseThrow(() -> new IllegalStateException("Sample Date with date " + localDateTime + " does not exist"));
    }

    public SampleDate findSampleDateByDate(LocalDateTime localDateTime) {
        return sampleDateRepository
                .findSampleDateByDate(localDateTime)
                .orElseThrow(() -> new IllegalStateException("Sample Date with date " + localDateTime + " does not exist"));
    }


    public SampleDate addNewSampleDate(SampleDate sampleDate) {
        Optional<SampleDate> sampleDateOptional = sampleDateRepository
                .findSampleDateByDate(sampleDate.getDate());

        if (sampleDateOptional.isPresent()) {
            throw new IllegalStateException("Sample Date already present");
        }

        return sampleDateRepository.save(sampleDate);
    }

}
