package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRepoTest {

    @Test
    void generateNRandomHumans() {

        int n = 200;

        CzlowiekContainerFactory.setSortMode(SortModes.ORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);
        assertEquals(n, TestRepo.getAllPeopleStream().count());
    }

    @Test
    void ensure3levelsOfHierarchy() {

        int n = 200;

        CzlowiekContainerFactory.setSortMode(SortModes.ORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);
        assertEquals(n, TestRepo.getAllPeopleStream().count());

        for (Czlowiek head : TestRepo.getHeads()) {
            for (Czlowiek podlegly : head.getPodlegli()) {
                for (Czlowiek podpodlegly : podlegly.getPodlegli()) {
                    assertTrue(podpodlegly.getPodlegli().isEmpty());
                }
            }
        }
    }

    @Test
    void jsonSaveLoad() {

        int n = 200;

        CzlowiekContainerFactory.setSortMode(SortModes.ORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);
        assertEquals(n, TestRepo.getAllPeopleStream().count());

        TestRepo.setLoader(new TestRepoJsonLoader("src/test-people.json"));

        TestRepo.saveJson();

        TestRepo.loadJson();
        assertEquals(n, TestRepo.getAllPeopleStream().count());
    }

    @Test
    void jsonSaveLoadWithLimit() {

        int n = 200;
        int limit = 100;

        CzlowiekContainerFactory.setSortMode(SortModes.ORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);
        assertEquals(n, TestRepo.getAllPeopleStream().count());

        TestRepo.setLoader(new TestRepoJsonLoader("src/test-people.json"));

        TestRepo.saveJson();

        TestRepo.loadJson(limit);
        assertEquals(limit, TestRepo.getAllPeopleStream().count());
    }

    @Test
    void recursivelyApplingFunction() {

        int n = 200;

        CzlowiekContainerFactory.setSortMode(SortModes.ORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);

        assertEquals(n, TestRepo.getAllPeopleStream().count());

        Set<Czlowiek> people = new HashSet<>();
        AtomicInteger counter = new AtomicInteger(0);
        TestRepo.recursivelyApplyFunction(czlowiek -> {
            people.add(czlowiek);
            counter.incrementAndGet();
        });

        assertEquals(n, counter.get());
        assertEquals(n, people.size());
    }

}
