import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.time.LocalTime;
import java.util.stream.Collectors;
import java.util.Optional;

public class Exercise{

    private static final List<String> workingDaysPeriod = Arrays.asList("Monday,Tuesday,Wednesday,Thusday".split(","));
    private static final List<String> weekendPeriod = Arrays.asList("Friday,Saturday,Sunday".split(","));
    private static final List<String> shortWeekendPeriod = Arrays.asList("Friday,Saturday".split(","));
    private static List<Restaurant> listRestaurant = new ArrayList<>();

    public static void main(String[] args){
        initData();
        //listRestaurant.forEach(res -> res.reservablePeriod.forEach(reser -> System.out.print("res"+res.name + " days: "+reser.dayOfWeeks)));
        search("Sunday", LocalTime.of(22,0), 45);
        search("Monday", LocalTime.of(11,0), 30);
        search("Sunday", LocalTime.of(18,0), 30);
    }

    public static void search(String date, LocalTime startTime, int period){
        List<Restaurant> listRes = listRestaurant.stream()
        .filter(res ->
        {
            // if(res.reservablePeriod.stream().filter(peri -> date.equalsIgnoreCase(peri.dayOfWeeks)).collect(Collectors.toList()).size() > 0){
                List<ReservablePeriod> listSer = res.reservablePeriod.stream()
                                                    .filter(peri -> date.equalsIgnoreCase(peri.dayOfWeeks))
                                                    .collect(Collectors.toList());
                if(listSer.size() > 0){
                    Optional<ReservablePeriod> ped = listSer.stream()
                            .filter(ser -> {
                                return (ser.periodTime.stream().filter(pedtime -> (pedtime.startTime.isBefore(startTime) || pedtime.startTime.equals(startTime))
                                                            && (pedtime.endTime.isAfter(startTime.plusMinutes(period)) || pedtime.endTime.equals(startTime.plusMinutes(period)) ))
                                                            .collect(Collectors.toList()).size() > 0);
                            })
                            .findAny();
                return ped.isPresent();
              }
              return false;
            }
        ).collect(Collectors.toList());
        listRes.forEach(res -> System.out.print(" Restaurant: "+res.name+" openTime: "+date+", "+startTime+"\n"));

    }

    private static void initData(){
        listRestaurant.add(initSGNData());
        listRestaurant.add(initRBData());
        listRestaurant.add(initHYData());
    }

    private static Restaurant initSGNData(){
        List<PeriodTime> listWorkingDaysPeriod = new ArrayList<>();
        List<PeriodTime> listWeekendDaysPeriod = new ArrayList<>();
        List<ReservablePeriod> listReservablePeriod = new ArrayList<>();
        listWorkingDaysPeriod.add(new PeriodTime(LocalTime.of(9,0), LocalTime.of(11,30)));
        listWorkingDaysPeriod.add(new PeriodTime(LocalTime.of(17,0), LocalTime.of(21,30)));
        listWeekendDaysPeriod.add(new PeriodTime(LocalTime.of(11,30), LocalTime.of(21,30)));


        workingDaysPeriod.forEach(days -> listReservablePeriod.add(new ReservablePeriod(days, listWorkingDaysPeriod)));
        shortWeekendPeriod.forEach(days -> listReservablePeriod.add(new ReservablePeriod(days, listWeekendDaysPeriod)));
        return new Restaurant("Sài Gòn New",listReservablePeriod);
    }

    private static Restaurant initRBData(){
        List<PeriodTime> listWorkingDaysPeriod = new ArrayList<>();
        List<PeriodTime> listWeekendDaysPeriod = new ArrayList<>();
        List<ReservablePeriod> listReservablePeriod = new ArrayList<>();
        listWorkingDaysPeriod.add(new PeriodTime(LocalTime.of(9,0), LocalTime.of(21,0)));
        listWeekendDaysPeriod.add(new PeriodTime(LocalTime.of(17,0), LocalTime.of(23,0)));


        workingDaysPeriod.forEach(days -> listReservablePeriod.add(new ReservablePeriod(days, listWorkingDaysPeriod)));
        weekendPeriod.forEach(days -> listReservablePeriod.add(new ReservablePeriod(days, listWeekendDaysPeriod)));
        return new Restaurant("Rạn Biển",listReservablePeriod);
    }

    private static Restaurant initHYData(){
        List<PeriodTime> listWeekendDaysPeriod = new ArrayList<>();
        List<ReservablePeriod> listReservablePeriod = new ArrayList<>();
        listWeekendDaysPeriod.add(new PeriodTime(LocalTime.of(17,0), LocalTime.of(20,0)));

        weekendPeriod.forEach(days -> listReservablePeriod.add(new ReservablePeriod(days, listWeekendDaysPeriod)));
        return new Restaurant("Hoàng Yến",listReservablePeriod);
    }

    private static class Restaurant{
        public String name;
        List<ReservablePeriod> reservablePeriod;

        public Restaurant(String name, List<ReservablePeriod> reservablePeriod){
            this.name = name;
            this.reservablePeriod = reservablePeriod;
        }
    }

    private static class ReservablePeriod{
        public String dayOfWeeks;
        public List<PeriodTime> periodTime;

        public ReservablePeriod(String dayOfWeeks, List<PeriodTime> listPeriodTime){
            this.dayOfWeeks = dayOfWeeks;
            this.periodTime = listPeriodTime;
        }
    }

    private static class PeriodTime{
        public LocalTime startTime;
        public LocalTime endTime;
        public PeriodTime(LocalTime startTime, LocalTime endTime){
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}
