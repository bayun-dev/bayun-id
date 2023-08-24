package dev.bayun.id.core.util.converter;

import dev.bayun.id.core.entity.account.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class SetAuthorityToStringConverter implements AttributeConverter<Set<Authority>, String> {

    private static final String SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(Set<Authority> attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.size() == 0) {
            return "";
        }

        return String.join(SEPARATOR, attribute.stream()
                .map(Authority::getAuthority).collect(Collectors.toSet()));
    }

    @Override
    public Set<Authority> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        if (dbData.length() == 0) {
            return Collections.emptySet();
        }

        return Arrays.stream(dbData.split(SEPARATOR))
                .map(Authority::valueOf)
                .collect(Collectors.toSet());
    }

}
