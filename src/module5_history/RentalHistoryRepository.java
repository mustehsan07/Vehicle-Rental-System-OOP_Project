import java.time.LocalDate;
import java.util.ArrayList;

public class RentalHistoryRepository {
    private final ArrayList<RentalHistoryRecord> records;

    public RentalHistoryRepository() {
        records = new ArrayList<RentalHistoryRecord>();
        addSampleData();
    }

    private void addSampleData() {
        records.add(new RentalHistoryRecord("R-101", "Ali Khan", "Toyota Corolla 2022",
                LocalDate.now().minusDays(8), LocalDate.now().minusDays(4),
                LocalDate.now().minusDays(4), 6500, RentalStatus.RETURNED));

        records.add(new RentalHistoryRecord("R-102", "Ayesha Malik", "Honda Civic 2021",
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(1),
                null, 8000, RentalStatus.ACTIVE));

        records.add(new RentalHistoryRecord("R-103", "Hamza Ahmed", "Suzuki Alto 2023",
                LocalDate.now().minusDays(2), LocalDate.now().plusDays(3),
                null, 4200, RentalStatus.ACTIVE));
    }

    public ArrayList<RentalHistoryRecord> getAllRecords() {
        return new ArrayList<RentalHistoryRecord>(records);
    }

    public void addRecord(RentalHistoryRecord record) {
        records.add(record);
    }

    public void updateRecord(int index, RentalHistoryRecord record) {
        records.set(index, record);
    }

    public void deleteRecord(int index) {
        records.remove(index);
    }

    public ArrayList<RentalHistoryRecord> searchRecords(String keyword, String statusFilter) {
        ArrayList<RentalHistoryRecord> result = new ArrayList<RentalHistoryRecord>();
        String lowerKeyword = keyword.toLowerCase();

        for (int i = 0; i < records.size(); i++) {
            RentalHistoryRecord record = records.get(i);

            boolean keywordMatched =
                    record.getRentalId().toLowerCase().contains(lowerKeyword)
                            || record.getCustomerName().toLowerCase().contains(lowerKeyword)
                            || record.getVehicleName().toLowerCase().contains(lowerKeyword);

            boolean statusMatched =
                    statusFilter.equals("All")
                            || record.getDisplayStatus().equals(statusFilter);

            if (keywordMatched && statusMatched) {
                result.add(record);
            }
        }

        return result;
    }
}