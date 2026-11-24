# Wypożyczalnia sprzętu

Konsolowa aplikacja wypożyczalni sprzętu (Java 21, Maven, JUnit 5).

## Wymagania

* JDK 21
* Maven 3.9 lub nowszy

## Budowanie i testy

```
mvn clean package
mvn test
```

## Uruchomienie

```
mvn compile exec:java
```

albo po zbudowaniu:

```
java -cp target/classes pl.wypozyczalnia.ui.Main
```

W konsoli Windows polskie znaki wyświetlają się poprawnie po wykonaniu `chcp 65001`.

## Dane startowe

Sprzęt: `E1` wiertarka udarowa, `E2` rower górski, `E3` namiot czteroosobowy.
Klienci: `K1` Jan Kowalski, `K2` Anna Nowak.
Daty wpisuje się w formacie `RRRR-MM-DD`.

## Struktura

| Pakiet | Warstwa |
|---|---|
| `pl.wypozyczalnia.domain` | model domeny, reguły biznesowe, interfejs repozytorium |
| `pl.wypozyczalnia.application` | przypadki użycia |
| `pl.wypozyczalnia.infrastructure` | repozytorium w pamięci |
| `pl.wypozyczalnia.ui` | menu konsolowe i składanie obiektów |
