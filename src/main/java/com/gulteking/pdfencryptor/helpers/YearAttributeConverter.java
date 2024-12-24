package com.gulteking.pdfencryptor.helpers;

import java.time.Year;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class YearAttributeConverter implements AttributeConverter<Year, Short> {

  @Override
  public Short convertToDatabaseColumn(Year attribute) {
    if (attribute != null) {
      return (short) attribute.getValue();
    }
    return null;
  }

  @Override
  public Year convertToEntityAttribute(Short dbData) {
    if (dbData != null) {
      return Year.of(dbData);
    }
    return null;
  }
}
