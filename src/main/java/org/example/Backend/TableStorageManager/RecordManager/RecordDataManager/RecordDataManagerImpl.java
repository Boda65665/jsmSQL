package org.example.Backend.TableStorageManager.RecordManager.RecordDataManager;

import org.example.Backend.Exception.ValidationException;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.Record;

import java.util.*;

public class RecordDataManagerImpl implements RecordDataManager {

    @Override
    public void validData(List<ColumnStruct> columnStructList, Record record) throws ValidationException {
        if (record.getColumns().size() > columnStructList.size()) throw new ValidationException("columns size more then in struct");
        for (Column column : record.getColumns()) {
            if (!validStruct(columnStructList, column)){
                throw new ValidationException("Struct column '" + column.getName() + "' is not valid");
            }
        }
    }

    private boolean validStruct(List<ColumnStruct> columnStructList, Column column) {
        for (ColumnStruct columnStruct : columnStructList) {
            if (column.getName().equals(columnStruct.getColumnName())) {
                if (column.getType().equals(columnStruct.getType())){
                 return true;
                }
            }
        }
        return false;
    }

    @Override
    public Record editDataForSave(List<ColumnStruct> columnStructList, Record record) {
        if (!isNeedEditDataForSave(columnStructList, record)) return record;

        ArrayList<Column> columns = record.getColumns();
        int oldSize = columns.size();
        HashMap<String, Column> columnMap = new HashMap();
        for (Column column : columns) {
            columnMap.put(column.getName(), column);
        }

        fillMissingColumnsWithNulls(columnStructList, columnMap, columns);
        for (int i = oldSize-1; i < columns.size(); i++) {
            Column column = columns.get(i);
            columnMap.put(column.getName(), column);
        }
        columns = rearrangeInOrder(columnStructList, columnMap);

        record.setColumns(columns);
        return record;
    }

    private void fillMissingColumnsWithNulls(List<ColumnStruct> columnStructList, HashMap<String, Column> columnMap, ArrayList<Column> columns) {
        for (ColumnStruct columnStruct : columnStructList) {
            if (!columnMap.containsKey(columnStruct.getColumnName())) {
                columns.add(new Column(null, columnStruct.getType(), columnStruct.getColumnName()));
            }
        }
    }

    private ArrayList<Column> rearrangeInOrder(List<ColumnStruct> columnStructList, HashMap<String, Column> columnMap) {
        ArrayList<Column> rearrangedColumns = new ArrayList<>();
        for (ColumnStruct columnStruct : columnStructList) {
            rearrangedColumns.add(columnMap.get(columnStruct.getColumnName()));
        }
        return rearrangedColumns;
    }

    private boolean isNeedEditDataForSave(List<ColumnStruct> columnStructList, Record record) {
        return columnStructList.size() != record.getColumns().size();
    }
}
