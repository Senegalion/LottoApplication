package org.example.domain.drawdateretriever;

import java.time.LocalDateTime;

@FunctionalInterface
interface DrawDateGenerable {
    LocalDateTime getNextDrawDate();
}
