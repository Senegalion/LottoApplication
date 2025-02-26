package org.example.domain.numberreceiver;

import java.util.UUID;

public class IdGenerator implements IdGenerable {
    @Override
    public String getId() {
        return UUID.randomUUID().toString();
    }
}
