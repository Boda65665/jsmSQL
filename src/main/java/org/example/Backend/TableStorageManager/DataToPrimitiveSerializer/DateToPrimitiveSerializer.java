package org.example.Backend.TableStorageManager.DataToPrimitiveSerializer;

import java.util.Date;

public class DateToPrimitiveSerializer implements DataSerializer<Date, Long> {
    @Override
    public Long serialize(Date data) {
        return data.getTime();
    }

    @Override
    public Date deserialize(Long serializeData) {
        return new Date(serializeData);
    }
}
