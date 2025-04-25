package org.example.Backend.DataToBytesConverters.factory;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.TableParts.BytesMetaDataConverters;
import org.example.Backend.DataToBytesConverters.TableParts.BytesTabularDataConverters;
import org.example.Backend.Models.TablePartType;

public class TablePartTypeBytesConverterFactory {

    public static TablePartTypeConverter getTablePartTypeConverter(TablePartType tablePartType) {
        return switch(tablePartType){
            case TABULAR_DATA -> new BytesTabularDataConverters();
            case TABLE_METADATA -> new BytesMetaDataConverters();
        };
    }
}
