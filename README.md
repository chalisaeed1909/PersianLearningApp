# Persian Language Learning Application

SCD Project, Iteration 1 (team of 4). Admin application for preparing Persian chapters for Urdu-speaking learners.

## Team
- Member 1: UC1 Manage Chapters and Basic Details (repo, starter project, final merge)
- Member 2: UC2 Configure Chapter Structure (MySQL script)
- Member 3: UC3 Manage Chapter Instructional Content (UTF-8)
- Member 4: UC4 Manage Chapter Glossary (MongoDB)

## Setup
1. Clone the repository and import it in Eclipse (File > Import > Existing Projects into Workspace).
2. Use JDK 1.8. Text file encoding must be UTF-8.
3. Copy `db.properties.example` to `db.properties` and enter your own database details. Never push `db.properties`.
4. Add the JARs from `lib/` to the Build Path if they are not already listed.

## Structure
- `model`: Chapter, ChapterComponent, GlossaryEntry
- `business`: Facade classes
- `data.dao`, `data.mysql`, `data.mongo`, `data.factory`: data layer
- `presentation`: screens
- `test`: JUnit 5 tests (no Mockito, custom stubs only)

## Git rules
- Never push directly to `main`. Use your branch: `feature/uc1-chapters`, `feature/uc2-structure`, `feature/uc3-content`, `feature/uc4-glossary`.
- Before a pull request: `git pull --rebase origin main`
- Edit only your own files.