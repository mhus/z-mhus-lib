/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.logging.MLogUtil;

public class Table implements Serializable, Externalizable {

    private static final long serialVersionUID = 1L;

    private String name;
    LinkedList<TableColumn> columns = new LinkedList<>();
    HashMap<String, Integer> columnsIndex = new HashMap<>();
    LinkedList<TableRow> rows = new LinkedList<>();

    public Table() {}

    public Table(ResultSet res) throws SQLException {
        ResultSetMetaData meta = res.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 0; i < count; i++) {
            addHeader(meta.getColumnName(i + 1), meta.getColumnTypeName(i + 1));
        }

        while (res.next()) {
            TableRow row = new TableRow();
            row.setTable(this);
            for (int i = 0; i < count; i++) {
                try {
                    row.appendData(res.getObject(i + 1));
                } catch (Throwable t) {
                    MLogUtil.log().t(t);
                    row.appendData((String) null);
                }
            }
            getRows().add(row);
        }
        res.close();
    }

    public List<TableColumn> getColumns() {
        return columns;
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public TableColumn addHeader(String name, Class<?> type) {
        return addHeader(name, type.getCanonicalName());
    }

    public TableColumn addHeader(String name, String type) {
        TableColumn col = new TableColumn();
        col.setName(name);
        col.setType(type);
        columnsIndex.put(name, columns.size());
        columns.add(col);
        return col;
    }

    public TableRow addRow(Object... data) {
        TableRow row = new TableRow();
        row.setTable(this);
        row.setData(data);
        rows.add(row);
        return row;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {

        if (name == null) name = "";
        out.writeUTF(name);

        out.writeInt(columns.size());
        for (TableColumn col : columns) out.writeObject(col);

        out.writeInt(rows.size());
        for (TableRow row : rows) out.writeObject(row);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        name = in.readUTF();
        {
            int size = in.readInt();
            // columns.clear();
            columns = new LinkedList<>();
            // columnsIndex.clear();
            columnsIndex = new HashMap<>();
            for (int i = 0; i < size; i++) {
                TableColumn col = (TableColumn) in.readObject();
                columnsIndex.put(col.getName(), columns.size());
                columns.add(col);
            }
        }
        {
            int size = in.readInt();
            // rows.clear();
            rows = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                TableRow row = (TableRow) in.readObject();
                row.setTable(this);
                rows.add(row);
            }
        }
    }
    /*
    private void readObjectNoData()
        throws ObjectStreamException {

    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColumnIndex(String name) {
        Integer ret = columnsIndex.get(name);
        return ret == null ? -1 : ret;
    }

    @Override
    public String toString() {
        return columns.toString() + rows.toString();
    }

    public int getColumnSize() {
        return columns.size();
    }

    public int getRowSize() {
        return rows.size();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        if (name == null) name = "";
        out.writeUTF(name);

        out.writeInt(columns.size());
        for (TableColumn col : columns) out.writeObject(col);

        out.writeInt(rows.size());
        for (TableRow row : rows) out.writeObject(row);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = in.readUTF();
        {
            int size = in.readInt();
            // columns.clear();
            columns = new LinkedList<>();
            // columnsIndex.clear();
            columnsIndex = new HashMap<>();
            for (int i = 0; i < size; i++) {
                TableColumn col = (TableColumn) in.readObject();
                columnsIndex.put(col.getName(), columns.size());
                columns.add(col);
            }
        }
        {
            int size = in.readInt();
            // rows.clear();
            rows = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                TableRow row = (TableRow) in.readObject();
                row.setTable(this);
                rows.add(row);
            }
        }
    }
}
