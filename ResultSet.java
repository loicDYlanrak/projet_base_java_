package jdbc;

import java.util.ArrayList;
import java.util.List;

public class ResultSet {
    private String tableName;
    private String[] columns;
    private List<String[]> rows;
    private int currentIndex = -1;

    public ResultSet(String response) {
        rows = new ArrayList<>();
        parseResponse(response);
    }

    private void parseResponse(String response) {
        String[] lines = response.split("\n");
        if (lines.length == 0) return;
    
        // Extraire le nom de la table
        for (String line : lines) {
            if (line.startsWith("Table:")) {
                tableName = line.substring(6).trim();
            }
        }
    
        // Extraire les colonnes
        for (String line : lines) {
            if (line.startsWith("Colonne:")) {
                String columnLine = line.substring(8).trim();
                columns = columnLine.split(" ");
            }
        }
    
        // Extraire les valeurs
        boolean inValuesSection = false;
        for (String line : lines) {
            //System.out.println("line: "+line);
            if (line.startsWith("Valeurs:")) {
                inValuesSection = true;
                continue;
            }
    
            if (inValuesSection && !line.trim().isEmpty()) {
                // Nettoyer et ajouter les lignes de valeurs
                String cleanedLine = line.replace("| ", "").trim(); // Enlever les "|" et les espaces inutiles
                //System.out.println("cleanedLine: "+cleanedLine);
                String[] rawValues = cleanedLine.split("\\  "); // Diviser par "|" pour recuperer chaque ligne
                //System.out.println("rawValues.length: "+rawValues.length);
                for (String valueLine : rawValues) {
                    if (!valueLine.trim().isEmpty()) {
                        String[] row = valueLine.trim().split("\\s+"); // Diviser par les espaces multiples
                        rows.add(row);
                    }
                }
            }
        }
    }
    

    public boolean next() {
        //System.out.println("currentIndex: "+currentIndex);
        //System.out.println("rows.size(): "+rows.size());
        if (currentIndex + 1 < rows.size()) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public Object getObject(String columnLabel) throws Exception {
        int columnIndex = getColumnIndex(columnLabel);
        return getObject(columnIndex);
    }

    public Object getObject(int columnIndex) throws Exception {
        if (currentIndex == -1 || currentIndex >= rows.size()) {
            throw new IllegalStateException("ResultSet is not pointing to a valid row.");
        }
        if (columnIndex < 0 || columnIndex >= rows.get(currentIndex).length) {
            throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
        return rows.get(currentIndex)[columnIndex];
    }

    private int getColumnIndex(String columnLabel) throws Exception {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equalsIgnoreCase(columnLabel)) {
                return i;
            }
        }
        throw new Exception("Column not found: " + columnLabel);
    }

    public void close() {
        rows.clear();
        currentIndex = -1;
    }

    public String getTableName() {
        return tableName;
    }
}
