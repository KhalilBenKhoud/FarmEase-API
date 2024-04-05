package com.pi.farmease.services;

import com.pi.farmease.entities.Application;
import com.pi.farmease.entities.Post;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    List<Application> getAllApplications();

    Optional<Application> getApplicationById(long id);
    void exportApplicationToPdf(Application application, String filePath) throws IOException;
    void addApplication(Application requestBody, Principal connected,long mortid);

    void updateApplication(Application application, long id);
    void deleteApplication(long id);
}
