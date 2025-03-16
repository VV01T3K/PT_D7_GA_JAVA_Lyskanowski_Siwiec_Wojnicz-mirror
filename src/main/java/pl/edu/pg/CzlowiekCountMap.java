package pl.edu.pg;

import java.util.Map;
import java.util.stream.Stream;

public class CzlowiekCountMap {
  Map<Czlowiek, Integer> czlowiekPodleglajacyCountMap;

  public CzlowiekCountMap(Map<Czlowiek, Integer> czlowiekPodleglajacyCountMap) {
    this.czlowiekPodleglajacyCountMap = czlowiekPodleglajacyCountMap;
  }

  public static CzlowiekCountMap czlowiekPodleglajacyCountMap(Stream<Czlowiek> ludzieFlattened) {
    Map<Czlowiek, Integer> czlowiekPodleglajacyCountMap = CzlowiekContainerFactory.chooseMap();
    ludzieFlattened.forEach(czlowiek -> {
      int podlegliCount = czlowiek.getAllInferiorsCount();
      czlowiekPodleglajacyCountMap.put(czlowiek, podlegliCount);
    });
    return new CzlowiekCountMap(czlowiekPodleglajacyCountMap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Czlowiek, Integer> entry : czlowiekPodleglajacyCountMap.entrySet()) {
      sb.append(entry.getKey().toShortString()).append(" -> ").append(entry.getValue()).append(" podlega").append("\n");
    }
    return sb.toString();
  }
}
