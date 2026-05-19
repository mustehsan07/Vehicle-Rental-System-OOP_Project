import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RentalHistoryTableModel extends AbstractTableModel {
    private final String[] columns = {
            "Rental ID", "Customer", "Vehicle", "Rent Date", "Due Date",
            "Return Date", "Days", "Daily Rate", "Total Cost", "Status"
    };

    private final ArrayList<RentalHistoryRecord> records;
    private final DateTimeFormatter formatter;

    public RentalHistoryTableModel() {
        records = new ArrayList<RentalHistoryRecord>();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public void setRecords(ArrayList<RentalHistoryRecord> newRecords) {
        records.clear();
        records.addAll(newRecords);
        fireTableDataChanged();
    }

    public RentalHistoryRecord getRecord(int row) {
        return records.get(row);
    }

    public int getRowCount() {
        return records.size();
    }

    public int getColumnCount() {
        return columns.length;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public Object getValueAt(int row, int column) {
        RentalHistoryRecord record = records.get(row);

        if (column == 0) return record.getRentalId();
        if (column == 1) return record.getCustomerName();
        if (column == 2) return record.getVehicleName();
        if (column == 3) return formatter.format(record.getRentDate());
        if (column == 4) return formatter.format(record.getDueDate());

        if (column == 5) {
            if (record.getReturnDate() == null) return "Not Returned";
            return formatter.format(record.getReturnDate());
        }

        if (column == 6) return record.getTotalDays();
        if (column == 7) return "Rs. " + String.format("%.0f", record.getDailyRate());
        if (column == 8) return "Rs. " + String.format("%.0f", record.getTotalCost());
        if (column == 9) return record.getDisplayStatus();

        return "";
    }
}