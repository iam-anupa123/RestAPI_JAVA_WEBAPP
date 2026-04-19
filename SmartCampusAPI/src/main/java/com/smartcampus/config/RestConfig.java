package com.smartcampus.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 * @author Anupa Vitharana
 */
@ApplicationPath("/api/v1")
public class RestConfig extends Application {
    
}
