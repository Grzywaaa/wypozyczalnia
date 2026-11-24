# Wypożyczalnia sprzętu

Imię i nazwisko: **[UZUPEŁNIJ]**
Numer albumu: **[UZUPEŁNIJ]**
Repozytorium: **[link, opcjonalnie]**

## 1. Opis domeny

Aplikacja obsługuje małą wypożyczalnię sprzętu (narzędzia, rowery, sprzęt turystyczny). Pracownik wypożycza sprzęt klientom na określony okres, przyjmuje zwroty z wyliczeniem należności i przekazuje sprzęt do serwisu. Cena zależy od długości wypożyczenia, a za spóźniony zwrot naliczana jest opłata dodatkowa.

### Przypadki użycia i reguły biznesowe

**PU1. Wypożycz sprzęt** (`RentalService.rent`)
* R1: data zwrotu musi być późniejsza niż data odbioru (`RentalPeriod`),
* R2: data odbioru nie może być w przeszłości (`StartDateRule`),
* R3: wypożyczenie trwa najwyżej 14 dni (`MaxPeriodRule`),
* R4: klient może mieć najwyżej 3 aktywne wypożyczenia (`CustomerLimitRule`),
* R5: wypożyczyć można tylko sprzęt dostępny, nie wypożyczony i nie w serwisie (`Equipment.rent`).

**PU2. Zwróć sprzęt** (`RentalService.returnEquipment`)
* R6: wypożyczenie na 7 dni lub dłużej ma 10% rabatu (`PricingPolicyFactory`, `WeeklyDiscountPricing`),
* R7: każdy dzień spóźnienia kosztuje 150% stawki dziennej (`Rental.returnOn`),
* R8: wypożyczenie można zakończyć tylko raz, a data zwrotu nie może poprzedzać daty odbioru (`Rental.returnOn`).

**PU3. Obsłuż serwis sprzętu** (`EquipmentService`)
* R9: do serwisu można oddać tylko sprzęt dostępny (`Equipment.sendToService`),
* R10: zakończyć można tylko serwis sprzętu, który w nim jest (`Equipment.finishService`).

### Słownik pojęć

| Pojęcie | W kodzie | Znaczenie |
|---|---|---|
| Sprzęt | `Equipment` | przedmiot do wypożyczenia ze stawką dzienną |
| Stan sprzętu | `EquipmentStatus` | dostępny, wypożyczony, w serwisie |
| Klient | `Customer` | osoba wypożyczająca sprzęt |
| Wypożyczenie | `Rental` | umowa klienta na sprzęt w danym okresie |
| Okres wypożyczenia | `RentalPeriod` | data odbioru i planowana data zwrotu |
| Kwota | `Money` | wartość w złotych z dokładnością do grosza |
| Stawka dzienna | `dailyRate` | cena za jeden dzień |
| Cennik | `PricingPolicy` | sposób wyliczenia ceny za okres |
| Rabat tygodniowy | `WeeklyDiscountPricing` | 10% zniżki od 7 dni |
| Opłata za spóźnienie | `LATE_FEE_FACTOR` | 150% stawki za każdy dzień po terminie |
| Regulamin | `RentalRules` | zestaw reguł sprawdzanych przed wypożyczeniem |
| Serwis | `sendToService`, `finishService` | czasowe wyłączenie sprzętu z wypożyczeń |

### Scenariusze BDD

Testy: `src/test/java/pl/wypozyczalnia/application/RentalScenariosTest.java`

**S1. Limit aktywnych wypożyczeń** (`customerWithThreeActiveRentalsCannotRentFourth`)
* Zakładając, że klient K1 ma 3 aktywne wypożyczenia,
* Gdy próbuje wypożyczyć czwarty sprzęt,
* Wtedy system odmawia z komunikatem o przekroczonym limicie, a sprzęt pozostaje dostępny.

**S2. Spóźniony zwrot** (`lateReturnIsChargedWithLateFee`)
* Zakładając, że klient wypożyczył sprzęt za 25 zł dziennie na 3 dni,
* Gdy zwraca go 2 dni po terminie,
* Wtedy płaci 75 zł za okres i 2 × 37,50 zł za spóźnienie, razem 150 zł.

**S3. Sprzęt w serwisie** (`equipmentInServiceCannotBeRented`)
* Zakładając, że sprzęt E1 został oddany do serwisu,
* Gdy klient próbuje go wypożyczyć,
* Wtedy system odmawia, bo sprzęt nie jest dostępny.

**S4. Rabat tygodniowy** (`weekLongRentalGetsDiscount`)
* Zakładając, że klient wypożyczył sprzęt za 25 zł dziennie na 7 dni,
* Gdy zwraca go w terminie,
* Wtedy płaci 157,50 zł, czyli cenę z 10% rabatem.

## 2. Struktura projektu

**[ZRZUT EKRANU drzewa pakietów z IDE, opcjonalnie diagram klas]**

| Pakiet | Warstwa | Zawartość |
|---|---|---|
| `domain` | domena | encje, obiekty wartości, agregat, wyjątki, interfejs `Repository` |
| `domain.pricing` | domena | cenniki (Strategia) i ich fabryka |
| `domain.rules` | domena | reguły wypożyczenia (Łańcuch zobowiązań) |
| `application` | aplikacja | przypadki użycia: `RentalService`, `EquipmentService`, `RentalListener` |
| `infrastructure` | infrastruktura | `InMemoryRepository` |
| `ui` | interfejs | menu konsolowe, powiadomienia, `Main` składający obiekty |

Domena nie importuje żadnej innej warstwy. Obiekty tworzone są ręcznie w `Main`, bez kontenera DI.

## 3. Tabela realizacji wymagań

| Wymaganie | Klasa lub plik | Commit | Uzasadnienie |
|---|---|---|---|
| DDD: obiekty wartości | `Money`, `RentalPeriod` | 4c5589a, 211ce05 | Rekordy niemutowalne, walidują się w konstruktorze, więc nie da się utworzyć ujemnej kwoty ani błędnego okresu. |
| DDD: encje | `Equipment`, `Customer` | e1beeb3, 15c5d1b | Mają tożsamość (`id`) i stan zmieniający się w czasie. |
| DDD: agregat | `Rental`, `Equipment` | b9e4319, 04478b1 | `Rental` sam nalicza opłatę i blokuje drugi zwrot; `Equipment` pozwala tylko na dozwolone zmiany stanu. Pól nie da się zmienić z zewnątrz. |
| DDD: repozytorium | `Repository` (domena), `InMemoryRepository` (infrastruktura) | dd8fd39, 699321e | Domena zna tylko interfejs, przechowywanie w pamięci jest szczegółem infrastruktury. |
| BDD | `RentalScenariosTest` | 858a4ba | Cztery scenariusze z sekcji 1, każdy test podzielony na given, when, then. |
| TDD | `RentalTest`, `Rental` | 66fba5b, 04478b1 | Najpierw test opłaty za spóźnienie, który nie przechodził, potem implementacja. |
| S: pojedyncza odpowiedzialność | `RentalService`, `ConsoleApp`, `Money` | e88396d, f53f360 | Serwis tylko koordynuje przypadek użycia, reguły są w domenie, a `ConsoleApp` tylko czyta dane i wypisuje wyniki. |
| O: otwarte/zamknięte | `RentalRule`, `PricingPolicy` | e5a01b7, 9fc611d | Nową regułę lub cennik dodaje się nową klasą, bez zmiany `RentalService` ani `Rental`. |
| L: podstawienie Liskov | `StandardPricing`, `WeeklyDiscountPricing` | fa50716 | `Rental.returnOn` działa poprawnie z każdą implementacją `PricingPolicy`. |
| I: segregacja interfejsów | `RentalListener`, `Repository` | b95b0a2, dd8fd39 | Małe interfejsy; obserwator implementuje tylko dwie metody zdarzeń. |
| D: odwracanie zależności | `RentalService`, `Main` | e88396d, f4595dc | Serwis zależy od abstrakcji `Repository`, `RentalRule`, `Clock`; konkretne obiekty wstrzykuje `Main` przez konstruktor. |
| Wzorzec Strategia | `PricingPolicy` i implementacje | 9fc611d, fa50716 | Wypożyczalnia ma różne cenniki; wyliczenie ceny jest wymienne bez zmian w agregacie. |
| Wzorzec Fabryka | `PricingPolicyFactory`, `RentalRules` | fa50716, 8a55103 | Jedno miejsce decyduje, który cennik zastosować i jak złożyć regulamin. |
| Wzorzec Łańcuch zobowiązań | `RentalRule` i podklasy | e5a01b7, 5009cb5 | Kilka niezależnych warunków przed wypożyczeniem; każdy w osobnej klasie, kolejność ustala się przy składaniu. |
| Wzorzec Obserwator | `RentalListener`, `ConsoleNotifier` | e88396d, b95b0a2 | Serwis informuje o wypożyczeniu i zwrocie, nie wiedząc, kto i jak powiadamia klienta. |
| Generyczność | `Repository<T extends Entity>`, `InMemoryRepository<T extends Entity>` | dd8fd39, 699321e | Jeden typ użyty z `Equipment`, `Customer` i `Rental` (`Main`, testy serwisów). |
| Hermetyzacja | wszystkie klasy domeny | cały projekt | Pola prywatne i `final`, stan zmieniany tylko metodami domenowymi, `findAll` zwraca niemodyfikowalną kopię. |
| Walidacja danych | `ConsoleApp`, `Money`, `RentalPeriod` | f53f360 | Błędna data lub złamana reguła daje komunikat, aplikacja działa dalej. |
| Wyjątki i Optional | `DomainException`, `NotFoundException`, `Repository.findById` | 4c5589a, 75e7bb4 | Własne wyjątki domenowe; brak wyniku jako `Optional`, metody nie zwracają `null`. |
| Struktury danych | `InMemoryRepository` | 699321e | `LinkedHashMap` daje wyszukiwanie po id w czasie stałym i stałą kolejność listy. |

## 4. Zrzuty ekranu

**[UZUPEŁNIJ]**
* każdy przypadek użycia w konsoli, w tym błędne dane (zła data, limit, sprzęt w serwisie),
* wynik `mvn test`,
* wynik `git log --oneline`.

## 5. Refaktoryzacje

**Refaktoryzacja 1. Zduplikowane składanie regulaminu i liczby magiczne**
Problem: łańcuch reguł z liczbami 14 i 3 był budowany identycznie w `Main`, `RentalServiceTest` i `RentalScenariosTest`. Zmiana limitu wymagałaby edycji trzech miejsc.
Technika: wydzielenie metody do nowej klasy (Extract Method / Move Method) i zastąpienie liczb magicznych stałymi.
Commity: 8a55103, e9c9bf7.

Przed:
```java
RentalRule rules = new StartDateRule();
rules.then(new MaxPeriodRule(14)).then(new CustomerLimitRule(3));
service = new RentalService(equipment, customers, rentals, rules, clock);
```
Po:
```java
service = new RentalService(equipment, customers, rentals, RentalRules.standard(), clock);
```

**Refaktoryzacja 2. Powtarzane wyszukiwanie encji z wyjątkiem**
Problem: konstrukcja `findById(id).orElseThrow(() -> new NotFoundException(...))` powtarzała się pięć razy w dwóch serwisach, a `EquipmentService` miał do tego własną metodę pomocniczą.
Technika: przeniesienie wspólnego kodu do metody domyślnej `Repository.require` (Extract Method, Pull Up Method).
Commity: 6672b5a, d6d1a98.

Przed:
```java
Rental rental = rentals.findById(rentalId)
        .orElseThrow(() -> new NotFoundException("wypożyczenie", rentalId));
```
Po:
```java
Rental rental = rentals.require(rentalId, "wypożyczenie");
```

**Refaktoryzacja 3. [UZUPEŁNIJ: problem znaleziony w trakcie dalszej pracy, np. przez SonarLint]**

## 6. Narzędzia AI

**[UZUPEŁNIJ własnymi słowami, 2 lub 3 zdania: z jakiego narzędzia i do czego korzystano.]**
