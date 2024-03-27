package com.pi.farmease.services;

import com.pi.farmease.entities.Application;
import com.pi.farmease.entities.Post;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    List<Application> getAllApplications();

    Optional<Application> getApplicationById(long id);

    void addApplication(Application requestBody, Principal connected,long mortid);

    Application updateApplication(long id, Application application);

    void deleteApplication(long id);
}
