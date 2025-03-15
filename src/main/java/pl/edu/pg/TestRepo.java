package pl.edu.pg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Consumer;

public class TestRepo {

    private static final int defaultNumberOfPeople = 10;

    private static Set<Czlowiek> heads = CzlowiekContainerFactory.chooseSet();

    private static TestRepoGenerator generator = new TestRepoGenerator();
    private static TestRepoJsonLoader loader = new TestRepoJsonLoader("src/people.json");

    public static void setGenerator(TestRepoGenerator generator) {
        TestRepo.generator = generator;
    }

    public static void setLoader(TestRepoJsonLoader loader) {
        TestRepo.loader = loader;
    }

    public static Set<Czlowiek> getHeads() {
        return heads;
    }

    // sorted
    public static Stream<Czlowiek> getAllPeopleStream() {
        return flattendTree().stream();
    }

    // sorted
    public static Set<Czlowiek> getAllPeopleSet() {
        return flattendTree();
    }

    public static void saveJson() {
        loader.saveJson(heads);
    }

    public static void loadJson() {
        heads = loader.readJson();
    }

    // load all then prune
    public static void loadJson(int n) {
        heads = loader.readJson();
        n = flattendTree().size() - n;
        while (n > 0) {
            List<Czlowiek> leafs = getAllPeopleStream().filter(c -> c.getPodlegli().size() == 0).limit(n)
                    .collect(Collectors.toList());
            leafs.forEach(TestRepo::remove);
            n -= leafs.size();
        }
    }

    public static boolean remove(Czlowiek czlowiek) {
        if (heads.remove(czlowiek))
            return true;
        for (Czlowiek head : heads) {
            if (removeRecursively(head, czlowiek)) {
                if (CzlowiekContainerFactory.getSortMode() == SortModes.ORDERED) {
                    heads = heads.stream()
                            .collect(Collectors.toCollection(CzlowiekContainerFactory::chooseSet));
                }
                return true;
            }
        }
        return false;
    }

    private static boolean removeRecursively(Czlowiek current, Czlowiek toRemove) {
        boolean removed = current.getPodlegli().remove(toRemove);
        for (Czlowiek podlegly : current.getPodlegli()) {
            if (removeRecursively(podlegly, toRemove)) {
                removed = true;
            }
        }

        return removed;
    }

    public static void generateTestData() {
        heads = generator.generateTestData(defaultNumberOfPeople);
    }

    public static void generateTestData(int n) {
        heads = generator.generateTestData(n);
    }

    // unsorted
    public static void recursivelyApplyFunction(Consumer<Czlowiek> function) {
        for (Czlowiek head : heads) {
            function.accept(head);
            recursivelyApplyFunction(head, function);
        }
    }

    // unsorted
    private static void recursivelyApplyFunction(Czlowiek current, Consumer<Czlowiek> function) {
        for (Czlowiek podlegly : current.getPodlegli()) {
            function.accept(podlegly);
            recursivelyApplyFunction(podlegly, function);
        }
    }

    // sorted
    private static Set<Czlowiek> flattendTree() {
        Set<Czlowiek> all = CzlowiekContainerFactory.chooseSet();
        for (Czlowiek head : heads) {
            all.add(head);
            recursivelyApplyFunction(head, all::add);
        }
        return all;
    }

    public static void printAll() {
        int index = 0;
        System.out.println(getHeads().getClass() + " | heads");
        for (Czlowiek czlowiek : getAllPeopleSet()) {
            System.out.println(++index + (index > 9 ? " " : "  ") +
                    czlowiek.getPodlegli().getClass() + " | " +
                    czlowiek.getImie() + " " +
                    czlowiek.getNazwisko() + " " +
                    czlowiek.getWiek() + " " +
                    czlowiek.getPlec() + " " +
                    "[" + czlowiek.getPodlegli().size() + "]" + " " +
                    "[" + czlowiek.getAllInferiorsCount() + "]");
        }
    }

    public static void printRecursively() {
        for (Czlowiek head : heads) {
            head.printRecursively();
        }
    }

    public static void main(String[] args) {

        CzlowiekContainerFactory.setSortMode(SortModes.ORDERED);
        CzlowiekContainerFactory.setComparator(new SortByNumberOfInferiors());

        generateTestData(20);
        // saveJson();
        // loadJson(13);

        printAll();
        // printRecursively();

        Set<String> names = new HashSet<>();
        TestRepo.recursivelyApplyFunction(czlowiek -> {
            names.add(czlowiek.getImie());
        });
        System.out.println("Unique names: " + names.size());

        // printAll();

        // getAllPeople().forEach(System.out::println);

        // System.out.println("Removing all people");

        // // getAllPeople().forEach(Czlowiek::printAllDetails);
        // getAllPeople().forEach(TestRepo::remove);
        // printAll();
    }
}