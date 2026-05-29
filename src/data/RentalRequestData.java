package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.RentalRequest;

public final class RentalRequestData {
    private static final List<RentalRequest> REQUESTS = new ArrayList<>();

    private RentalRequestData() {
    }

    public static void clear() {
        REQUESTS.clear();
    }

    public static void addRequest(RentalRequest request) {
        if (request != null) {
            REQUESTS.add(request);
        }
    }

    public static List<RentalRequest> getRequests() {
        return Collections.unmodifiableList(REQUESTS);
    }

    public static List<RentalRequest> getPendingRequests() {
        List<RentalRequest> pending = new ArrayList<>();
        for (RentalRequest request : REQUESTS) {
            if ("Pending".equalsIgnoreCase(request.getStatus())) {
                pending.add(request);
            }
        }
        return pending;
    }

    public static RentalRequest findById(String requestId) {
        if (requestId == null) {
            return null;
        }
        for (RentalRequest request : REQUESTS) {
            if (requestId.equalsIgnoreCase(request.getRequestId())) {
                return request;
            }
        }
        return null;
    }

    public static boolean markApproved(String requestId) {
        RentalRequest request = findById(requestId);
        if (request == null) {
            return false;
        }
        request.setStatus("Approved");
        return true;
    }

    public static boolean markRejected(String requestId) {
        RentalRequest request = findById(requestId);
        if (request == null) {
            return false;
        }
        request.setStatus("Rejected");
        return true;
    }
}
