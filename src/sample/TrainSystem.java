package sample;

import sample.graph.GraphManager;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @author suka zalupa govno blyat sobach'e
 */
public class TrainSystem {

    private static final List<Train> trains = new ArrayList<>();
    private static final List<Station> stations = new ArrayList<>();

    public static void main(String[] args) {

        try {
            Scanner reader = new Scanner(new File("tests.txt"));
            while (reader.hasNextLine()) {
                try {
                    Train train = deserializeTrain(reader.nextLine());
                    trains.add(train);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Ошибка данных");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка файла");
        }

        // Поиск пути
        Station A = findStationOrCreate("Рыбинск");
        Station B = findStationOrCreate("Буй");
        List<Train> result = findRoute(A, B);
        result.stream().forEach(train ->
            System.out.printf("\n%12s -> %-12s %02d:%02d - %02d:%02d",
                train.getA().getName(), train.getB().getName(),
                train.getTimeOut().getHour(), train.getTimeOut().getMinute(),
                train.getTimeIn().getHour(), train.getTimeIn().getMinute()
            )
        );

        trainsMenu();
    }

    private static List<Train> findRoute(Station a, Station b) {
        GraphManager<Station, Train> graph = new GraphManager<>(stations);
        return graph.findRoute(a, b);
    }

    public static Train deserializeTrain(String str) {
        String[] a = str.trim().split(" +");
        try {
            Station stationOut = findStationOrCreate(a[2]);
            Station stationIn = findStationOrCreate(a[3]);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'_'HH:mm_VV");
            Train newT = new Train(
                Integer.parseInt(a[0]),
                Integer.parseInt(a[1]),
                stationOut,
                stationIn,
                ZonedDateTime.parse(a[4], formatter),
                ZonedDateTime.parse(a[5], formatter)
            );
            return newT;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка ввода элемента");
            System.out.println(str);
        }
        return null;
    }

    public static void PointOutTimeIn(String pointout, ZonedDateTime time, ArrayList<Train> train) {
        System.out.println(" Номер Цена  Пункт прибытия  Время отправления");
        //как сюда таким же хуем вместить time я хз))))))))))))))
        train.stream().sorted().filter(t -> t.getA().equals(pointout)).forEach((t) -> System.out.println(" " + t.strMinus(2)));
    }

    public static void PointOutPointIn(String pointout, String pointin, ArrayList<Train> train) {
        System.out.println(" Номер Цена    Время отправления Время прибытия");
        //куда суда поинтин я тож хуй ево знает
        train.stream().sorted().filter(t -> t.getA().equals(pointout)).forEach((t) -> System.out.println(" " + t.strMinus(2)));
    }

    public static void trainsMenu() {
        int oper = -1;
        String str;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n1 - добавить поезд\n2 - удалить поезд\n3 - разработать маршрут\n4 - вывод поездов по пункту отправления и времени прибытия\n5 - вывод поездов по пункту отправления и пункту назначения\n0(или не цифра) - выход");
        if (sc.hasNextInt()) {
            oper = sc.nextInt();
        }

        while (oper != 0) {
            switch (oper) {
                case (1): {
                    System.out.println("Добавьте ячейку по типу\nНомер поезда  Пункт отправления  Вреня отпрваления  Пункт прибытия  Время прибытия  Стоимость проезда");
                    Scanner in = new Scanner(System.in);
                    str = in.nextLine();

                    Train t = deserializeTrain(str);
                    trains.add(t);
                    break;
                }

                case (2): {
                    System.out.println("Впишите ячейку по типу\nНомер поезда  Пункт отправления  Вреня отпрваления  Пункт прибытия  Время прибытия  Стоимость проезда");
                    Scanner in = new Scanner(System.in);
                    str = in.nextLine();
                    System.out.println(str);
                    Train t = deserializeTrain(str);
                    trains.remove(t);
                    break;
                }

                case (3): {
                    System.out.println("Впишите ячейку по типу\n Пункт отправления  Пункт назначения");

                    break;
                }

                case (4): {
                    System.out.println("Впишите ячейку по типу\n Пункт отправления  Время отправления");

                    break;
                }

                case (5): {
                    System.out.println("Впишите ячейку по типу\n Пункт отправления  Пункт прибытия");

                    break;
                }

                default: {
                    System.out.println("Неверная команда");
                    break;
                }
            }

            System.out.println("\n1 - добавить поезд\n2 - удалить поезд\n3 - разработать маршрут\n4 - вывод поездов по пункту отправления и времени прибытия\n5 - вывод поездов по пункту отправления и пункту назначения\n0 (или не цифра) - выход");
            try {
                oper = sc.nextInt();
            } catch (Exception e) {
                System.exit(0);
            }
        }
    }

    /*private static List<Train> findRoute(Station out, Station in) {
        HashSet<Station> visited = new HashSet<>(); // Посещеные станции
        Map<Station, Long> weights = new HashMap<>(); // Мапа с весами ребер графа (поездами)
        Map<Station, ZonedDateTime> timesIn = new HashMap<>(); // Мапа с времен приезда ребер графа (поездов)
        HashSet<Station> calculated = new HashSet<>(); // Множество для уже просчитанных станций

        stations.forEach(i -> weights.put(i, Long.MAX_VALUE)); // Инициализируем веса нод по максимуму
        stations.forEach(i -> timesIn.put(i, ZonedDateTime.now())); // Инициализируем веса нод по максимуму
        weights.put(out, 0L); // Устанавливаем начальной ноде (станции) вес равный нулю

        Station prev = out; // Начнем рассмотр нод
        // Зациклим алгоритм, пока не придем в in
        while (!prev.equals(in)) {
            calculated.clear(); // Обнулим сет

            // Поиск минимальной непосещенной станции
            Long minWeight = Long.MAX_VALUE;
            Station station = null;
            for (Station s : stations) {
                if (visited.contains(s)) {
                    continue;
                }

                Long weight = weights.get(s);
                if (weight < minWeight) {
                    station = s;
                    minWeight = weight;
                }
            }

            Long toCurWeight = weights.get(station); // Вес до текущей станции
            final Station finalStation = station;
            station.getTrains().stream()
                .sorted(new TrainTimeComparator()) // Сортируем по времени прибытию
                .filter(train -> train.getTimeOut().isAfter(timesIn.get(finalStation)))
                .filter(train -> !train.getB().equals(finalStation)) // Не учитываем поезда, которые приезжают сюда
                .forEach(train -> {
                    Station to = train.getB(); // Станция назначения
                    if (calculated.contains(to))
                        return;

                    calculated.add(to); // Добавляем в сет станцию, чтоб больше не считать для нее вариации, а так как
                    // у нас отсортированы поезда по времени, то это самый ранний возможной поезд
                    long weight = train.getWeight();
                    if (weight < weights.get(to)) {
                        weights.put(to,  weight);
                        timesIn.put(to, train.getTimeIn());
                    }
                });

            visited.add(station); // Помечаем станцию посещенной
            prev = station;
        }

        // Вывод результата
        List<Train> result = new ArrayList<>();

        Station station = in;
        // Пока не дойдем до исходной ноды
        while (!station.equals(out)) {
            calculated.clear();
            long toCurWeight = weights.get(station);

            // Перебираем все поезда, пока не найдем поезд с нужным весом
            for (Train train : station.getTrains()) {
                Station pointOut = train.getA();

                if (calculated.contains(pointOut))
                    continue;
                calculated.add(pointOut);

                long weight = train.getWeight();

                // Проверка нужный ли нам поезд
                if (weights.get(pointOut) == weight) {
                    result.add(train); // Добавляем в стек результата
                    station = pointOut; // Рассматриваем ту же задачу от этой станции

                    break;
                }
            }
        }

        // Отзеркалим результат
        Collections.reverse(result);

        return result;
    }*/

    private static Station findStationOrCreate(String name) {
        return stations.stream()
            .filter(station -> station.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseGet(() -> {
                Station station = new Station(name);
                stations.add(station);

                return station;
            });
    }
}
