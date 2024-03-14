package com.meracok.resource.mongodb;

import com.meracok.domain.Usuario;
import com.meracok.infrastructure.repository.UsuarioRepository;
import com.meracok.resource.Constants;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.bson.types.ObjectId;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class MongoDbTestResource implements QuarkusTestResourceLifecycleManager {

    private MongoDBContainer mongoContainer;

    @Override
    public Map<String, String> start() {
        mongoContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
                .withExposedPorts(27017);

        mongoContainer.start();

        return Collections.singletonMap("quarkus.mongodb.connection-string", mongoContainer.getReplicaSetUrl());
    }

    @Override
    public void stop() {
        System.out.println("FINALIZANDO MONGO CONTAINER");
        if (mongoContainer != null) {
            mongoContainer.stop();
        }
    }

}
