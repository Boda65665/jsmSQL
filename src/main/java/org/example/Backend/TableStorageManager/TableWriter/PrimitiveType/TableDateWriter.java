package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.DataToPrimitiveSerializer.AbstractFactoryDataSerializer;
import org.example.Backend.TableStorageManager.DataToPrimitiveSerializer.DataSerializer;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import java.util.Date;

public class TableDateWriter extends TableWriter<Date> {
    private final TableWriter<Long> tableLongWriter;
    private final AbstractFactoryDataSerializer abstractFactoryDataSerializer = new AbstractFactoryDataSerializer();

    public TableDateWriter(TablePathProvider tablePathProvider, TableWriter<Long> tableLongWriter) {
        super(tablePathProvider);
        this.tableLongWriter = tableLongWriter;
    }

    @Override
    public void write(String tableName, Date data, int offset) {
        final DataSerializer<Date, Long> serializer = (DataSerializer<Date, Long>) abstractFactoryDataSerializer.getDataSerializer(TypeData.DATE);

        Long time = serializer.serialize(data);
        tableLongWriter.write(tableName, time, offset);
    }
}
