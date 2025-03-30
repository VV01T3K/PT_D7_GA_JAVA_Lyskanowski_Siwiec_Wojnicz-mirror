# Analiza Efektywności Wielowątkowości

## 1. Wprowadzenie
Celem sprawozdania jest przedstawienie różnicy w wydajności aplikacji wielowątkowych w zależności od liczby rdzeni procesora.
Porównane zostaną czasy wykonania algorytmu szyfrowania AES dla 232 plików zawierających dużą ilość danych.

W celu uzyskania dokładniejszego porównania czasu pliki są szyfrowane fragmentami 50-bajtowymi, co wymaga wielokrotnego odczytu z pliku.
Każdy wątek wykonuje operacje szyfrowania lub deszyfrowania na przydzielonym fragmencie danych.

---

## 2. Wykres przedstawiający czasy wykonania

![Wykres 1](Data/Wykresy/overall_execution_times.png)
<div style="display: flex; flex-wrap: wrap; justify-content: space-around;">
    <img src="Data/Wykresy/query_times-1.png" alt="Wykres 1" width="400"/>
    <img src="Data/Wykresy/query_times-8.png" alt="Wykres 2" width="400"/>
    <img src="Data/Wykresy/query_times-64.png" alt="Wykres 3" width="400"/>
    <img src="Data/Wykresy/query_times-1024.png" alt="Wykres 4" width="400"/>
</div>

---

## 3. Opis wykresu 
Analizując wykres, można zauważyć, że czas wykonania algorytmu AES w przypadku użycia tylko jednego wątku jest znacząco dłuższy niż w przypadku zastosowania wielowątkowości.
Czas ten jednak nie zmniejsza się proporcjonalnie wraz ze wzrostem liczby wątków. Największą poprawę wydajności osiągnięto przy użyciu około **16 wątków**, gdzie czas wykonania został zmniejszony około **4-krotnie** w porównaniu z przetwarzaniem sekwencyjnym.

Dla testów z większą liczbą wątków nie zaobserwowano dalszej znaczącej poprawy czasu wykonania. Wręcz przeciwnie – przy dużej liczbie wątków czas ma tendencję do nieznacznego wzrostu. Wynika to z:
- ograniczeń liczby dostępnych rdzeni procesora,
- niewykorzystania wszystkich wątków przez system,
- narzutu związanego z zarządzaniem wieloma wątkami ich tworzeniem i synchronizacją,
- liczby plików do przetworzenia – każdy plik przypisywany jest do jednego wątku.

---

## 4. Wnioski
Na podstawie analizy można wyciągnąć następujące wnioski:
1. Wielowątkowość znacząco poprawia wydajność przetwarzania, jednak efekt nie jest liniowy w stosunku do liczby wątków.
2. Największe korzyści wydajnościowe osiągnięto przy około 16 wątkach – dalsze zwiększanie ich liczby nie przynosi już istotnych zysków.
3. Skalowalność przetwarzania ograniczona jest przez liczbę dostępnych rdzeni oraz działań na plikach.
4. Należy odpowiednio dobierać liczbę wątków w zależności od zasobów sprzętowych i charakterystyki problemu.

---

## 5. Podsumowanie
Wielowątkowość pozwala na znaczne przyspieszenie operacji przetwarzania wielu plików naraz, w tym przypadku: szyfrowania AES, 
ale jej efektywność zależy od liczby dostępnych rdzeni oraz narzutu związanego z zarządzaniem wątkami.
Odpowiednia strategia podziału pracy i alokacji zasobów pozwala na optymalne wykorzystanie dostępnych zasobów sprzętowych.

