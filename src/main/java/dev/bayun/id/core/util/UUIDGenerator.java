package dev.bayun.id.core.util;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;

import java.util.UUID;

public class UUIDGenerator {

    private static final TimeBasedEpochGenerator v7Generator = Generators.timeBasedEpochGenerator();

    public static UUID getV4() {
        return UUID.randomUUID();
    }

    public static UUID getV7() {
        return v7Generator.generate();
    }

}
