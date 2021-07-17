package br.com.zupacademy.rayllanderson.proposta.travel.creators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TravelNotifyRequestCreator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static MockTravelRequest createTraveNotifyRequestToBeSaved(){
        LocalDate fiveDaysLater = LocalDate.now().plusDays(5);
        return new MockTravelRequest(createDateFrom(fiveDaysLater), "Tokyo");
    }

    public static MockTravelRequest createTraveNotifyRequestWithEndDateInPast(){
        LocalDate fiveDaysBefore = LocalDate.now().minusDays(5);
        return new MockTravelRequest(createDateFrom(fiveDaysBefore), "Tokyo");
    }

    public static MockTravelRequest createTraveNotifyRequestWithDestinationEmpty(){
        LocalDate fiveDaysLater = LocalDate.now().plusDays(5);
        return new MockTravelRequest(createDateFrom(fiveDaysLater), "");
    }

    private static String createDateFrom(LocalDate localDate){
        return formatter.format(localDate);
    }

    private static class MockTravelRequest {
        private final String endDate;
        private final String destination;

        public MockTravelRequest(String endDate, String destination) {
            this.endDate = endDate;
            this.destination = destination;
        }
    }
}
