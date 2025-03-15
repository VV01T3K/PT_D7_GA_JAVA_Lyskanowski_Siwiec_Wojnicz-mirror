package pl.edu.pg;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static Stream<Czlowiek> getAllPeople() {
        return flattendTree().stream();
    }

    public static void saveJson() {
        loader.saveJson(heads);
    }

    public static void loadJson() {
        heads = loader.readJson();
    }

    public static void loadJson(int n) {
        heads = loader.readJson();
        n = flattendTree().size() - n;
        while (n > 0) {
            List<Czlowiek> leafs = getAllPeople().filter(c -> c.getPodlegli().size() == 0).limit(n)
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

    private static void addRecursively(Czlowiek head, Set<Czlowiek> all) {
        for (Czlowiek podlegly : head.getPodlegli()) {
            all.add(podlegly);
            addRecursively(podlegly, all);
        }
    }

    private static Set<Czlowiek> flattendTree() {
        Set<Czlowiek> all = CzlowiekContainerFactory.chooseSet();
        all.addAll(heads);
        for (Czlowiek head : heads) {
            addRecursively(head, all);
        }
        return all;
    }

    public static void printAll() {
        System.out.println(getHeads().getClass() + " | heads");
        for (Czlowiek czlowiek : getAllPeople().collect(Collectors.toList())) {
            System.out.println("" +
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
        saveJson();
        loadJson(13);

        printAll();
        printRecursively();
        // printAll();

        // getAllPeople().forEach(System.out::println);

        // System.out.println("Removing all people");

        // // getAllPeople().forEach(Czlowiek::printAllDetails);
        // getAllPeople().forEach(TestRepo::remove);
        // printAll();
    }
}