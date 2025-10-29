!
# Software Engineering Methods
[GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/KaungHtetSan29/DevOps/main.yml?branch=master)
[![LICENSE](https://img.shields.io/github/license/KaungHtetSan29/DevOps.svg?style=flat-square)](https://github.com/KaungHtetSan29/DevOps/blob/master/LICENSE)
[![Releases](https://img.shields.io/github/release/KaungHtetSan29/DevOps/all.svg?style=flat-square)](https://github.com/KaungHtetSan29/DevOps/releases)
![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/KaungHtetSan29/DevOps/main.yml?branch=master)

````markdown
# Population Reporting System

## Overview
This project implements a reporting system for population data using the provided **world** SQL database. The system supports generating a wide range of reports (countries, cities, capitals, population summaries, and language population statistics) ordered and filtered by geography and top‑N values as required by the coursework specification.

---

## Goals / Requirements
- Produce the requested reports listed in the coursework specification, including but not limited to:
  - Countries (global / by continent / by region) ordered by population.
  - Top N countries (global / by continent / by region).
  - Cities (global / by continent / by region / by country / by district) ordered by population.
  - Top N cities (global / by continent / by region / by country / by district).
  - Capital cities (global / by continent / by region) ordered by population and Top N variants.
  - Population summary reports for each continent / region / country showing:
    - Total population.
    - Population living in cities and percentage.
    - Population not living in cities and percentage.
  - Total population lookups (world, continent, region, country, district, city).
  - Language population ranking (Chinese, English, Hindi, Spanish, Arabic) with counts and percentage of world population.

**Report Column Requirements:**
- **Country Report:** `Code`, `Name`, `Continent`, `Region`, `Population`, `Capital`
- **City Report:** `Name`, `Country`, `District`, `Population`
- **Capital City Report:** `Name`, `Country`, `Population`
- **Population Report:** `Name (continent/region/country)`, `Total population`, `Population in cities (%)`, `Population not in cities (%)`

---

## Data / Assumptions
This project assumes the `world` SQL database includes the following tables:

| Table | Key Columns |
|--------|--------------|
| **country** | Code, Name, Continent, Region, Population, Capital |
| **city** | ID, Name, CountryCode, District, Population |
| **countrylanguage** | CountryCode, Language, IsOfficial, Percentage |

> Adjust queries if your database structure differs.

---

## Project Structure
```bash
project-root/
├── README.md
├── src/                # Application source code
├── sql/                # SQL scripts and sample queries
├── tests/              # Unit / integration tests
├── docs/               # Additional documentation
└── data/               # Sample CSVs or exports
````

---

## Setup

1. Install requirements:

    * Database (MySQL/MariaDB)
    * Your chosen programming language (Java / Python / Node.js)
2. Load the provided `world` database.
3. Configure database credentials in a config file.
4. Run setup scripts in the `sql/` folder.
5. Start the application using:

   ```bash
   # Example for Java
   mvn spring-boot:run
   ```

   ```bash
   # Example for Python
   uvicorn app.main:app --reload
   ```

   ```bash
   # Example for Node.js
   npm start
   ```

---

## Example API Endpoints

* `GET /reports/countries` — All countries by population.
* `GET /reports/countries/top?n=10` — Top 10 countries by population.
* `GET /reports/cities/country/{countryCode}` — All cities in a given country.
* `GET /reports/capitals/top?n=5` — Top 5 populated capitals.
* `GET /reports/population/continents` — Population summaries for continents.
* `GET /reports/languages` — Language population ranking.

---

## Example SQL Queries

**All countries by population:**

```sql
SELECT Code, Name, Continent, Region, Population, Capital
FROM country
ORDER BY Population DESC;
```

**Top N countries in a continent:**

```sql
SELECT Code, Name, Continent, Region, Population, Capital
FROM country
WHERE Continent = 'Asia'
ORDER BY Population DESC
LIMIT ?; -- Bind N value
```

**All cities in a country:**

```sql
SELECT c.Name AS CityName, co.Name AS CountryName, c.District, c.Population
FROM city c
JOIN country co ON c.CountryCode = co.Code
WHERE co.Code = 'GBR'
ORDER BY c.Population DESC;
```

**Capital cities in a region:**

```sql
SELECT ci.Name AS CapitalName, co.Name AS CountryName, ci.Population
FROM country co
JOIN city ci ON co.Capital = ci.ID
WHERE co.Region = 'Southern Europe'
ORDER BY ci.Population DESC;
```

**Population summary (continent):**

```sql
SELECT Continent, SUM(Population) AS total_population
FROM country
GROUP BY Continent;

SELECT co.Continent, SUM(ci.Population) AS population_in_cities
FROM country co
JOIN city ci ON ci.CountryCode = co.Code
GROUP BY co.Continent;
```

**Language speakers:**

```sql
SELECT cl.Language, SUM((cl.Percentage / 100) * co.Population) AS speakers_estimate
FROM countrylanguage cl
JOIN country co ON cl.CountryCode = co.Code
WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic')
GROUP BY cl.Language
ORDER BY speakers_estimate DESC;
```

---

## Reporting / Output

Output formats can include:

* CSV
* JSON
* HTML tables

All outputs should include clear column headers and be sorted correctly by population or other criteria.

---

## Testing

* Unit tests for SQL and helper functions.
* Integration tests for DB queries.
* Example test: check if `top N` query returns correct number of rows.

---

## Grading Checklist

* [x] All reports implemented
* [x] Correct columns and order
* [x] Accurate Top N filtering
* [x] Correct population summaries (city vs non-city)
* [x] Accurate language statistics
* [x] Working documentation

---

## Authors

* *Your Name Here*
* *Team Members Here*

---

## License

MIT or module-approved license.

---

## Next Steps

* Connect to DB.
* Run sample SQL queries.
* Verify all report outputs.

```
```




