package vehicle_display;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

public class VehicleTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Vehicle", "Brand", "Type", "Seats", "Transmission", "Daily Rate"};
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-PK"));
    private List<Vehicle> vehicles = new ArrayList<>();

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = new ArrayList<>(vehicles);
        fireTableDataChanged();
    }

    public Vehicle getVehicleAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= vehicles.size()) {
            return null;
        }
        return vehicles.get(rowIndex);
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
        if (columnIndex == 4) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Vehicle vehicle = vehicles.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> vehicle.getVehicleId();
            case 1 -> vehicle.getName();
            case 2 -> vehicle.getBrand();
            case 3 -> vehicle.getType();
            case 4 -> vehicle.getSeats();
            case 5 -> vehicle.getTransmission();
            case 6 -> currencyFormat.format(vehicle.getDailyRate());
            default -> "";
        };
    }
}
