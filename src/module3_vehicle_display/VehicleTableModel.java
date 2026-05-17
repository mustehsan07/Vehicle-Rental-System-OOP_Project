import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VehicleTableModel extends AbstractTableModel {
    private final String[] columns = {"Select", "ID", "Vehicle", "Brand", "Type", "Seats", "Transmission", "Daily Rate"};
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-PK"));
    private List<Vehicle> vehicles = new ArrayList<>();
    private String selectedVehicleId;

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = new ArrayList<>(vehicles);
        if (getSelectedVehicle() == null) {
            selectedVehicleId = null;
        }
        fireTableDataChanged();
    }

    public Vehicle getSelectedVehicle() {
        if (selectedVehicleId == null) {
            return null;
        }

        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(selectedVehicleId)) {
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return vehicles.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        }
        if (columnIndex == 5) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Vehicle vehicle = vehicles.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return vehicle.getVehicleId().equals(selectedVehicleId);
            case 1:
                return vehicle.getVehicleId();
            case 2:
                return vehicle.getName();
            case 3:
                return vehicle.getBrand();
            case 4:
                return vehicle.getType();
            case 5:
                return vehicle.getSeats();
            case 6:
                return vehicle.getTransmission();
            case 7:
                return currencyFormat.format(vehicle.getDailyRate());
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex != 0 || rowIndex < 0 || rowIndex >= vehicles.size()) {
            return;
        }

        Vehicle vehicle = vehicles.get(rowIndex);
        boolean checked = Boolean.TRUE.equals(value);
        selectedVehicleId = checked ? vehicle.getVehicleId() : null;
        fireTableDataChanged();
    }
}
