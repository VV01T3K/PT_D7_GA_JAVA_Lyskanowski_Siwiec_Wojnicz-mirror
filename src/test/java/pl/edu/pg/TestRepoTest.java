package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

public class TestRepoTest {

    @Test
    void generateNRandomHumans() {

        int n = 200;

        CzlowiekContainerFactory.setSortMode(SortModes.UNORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);
        assertEquals(n, TestRepo.getAllPeople().count());
    }

    @Test
    void ensure3levelsOfHierarchy() {

        int n = 200;

        CzlowiekContainerFactory.setSortMode(SortModes.UNORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);
        assertEquals(n, TestRepo.getAllPeople().count());

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

        CzlowiekContainerFactory.setSortMode(SortModes.UNORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);
        assertEquals(n, TestRepo.getAllPeople().count());

        TestRepo.setLoader(new TestRepoJsonLoader("src/test-people.json"));

        TestRepo.saveJson();

        TestRepo.loadJson();
        assertEquals(n, TestRepo.getAllPeople().count());
    }

    @Test
    void jsonSaveLoadWithLimit() {

        int n = 200;
        int limit = 100;

        CzlowiekContainerFactory.setSortMode(SortModes.UNORDERED);
        CzlowiekContainerFactory.setComparator(Comparator.naturalOrder());

        TestRepo.generateTestData(n);
        assertEquals(n, TestRepo.getAllPeople().count());

        TestRepo.setLoader(new TestRepoJsonLoader("src/test-people.json"));

        TestRepo.saveJson();

        TestRepo.loadJson(limit);
        assertEquals(limit, TestRepo.getAllPeople().count());
    }

}
