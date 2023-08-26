package dev.bayun.id.core.entity.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.io.Serializable;
import java.util.stream.Stream;

@Data
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person implements Serializable {

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public enum Gender {
        MALE,
        FEMALE;

        @JsonCreator
        public static Person.Gender fromValue(String value) {
            return Stream.of(Person.Gender.values())
                    .filter(enumValue -> enumValue.name().equalsIgnoreCase(value))
                    .findAny()
                    .orElseThrow(IllegalArgumentException::new);
        }

    }

}
