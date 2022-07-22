# Software to store research participant information

## Introduction
Implemented by AntiikDev & Doomslizer in Java programming course at the
University of Jyväskylä (JYU).

Instructions (JYU, TIM):
<https://tim.jyu.fi/view/kurssit/tie/ohj2/tyokalut/git/ohj2git#fork>


## Java ja tietokannat (SQLite) ongelmat ja ratkaisut
Ongelma 1.
- O: Koehenkilot-luokka palautti tietokantavirheen, joka viittasi sen
etsi metodiin.
- R: koehenkilot.etsi, josta edelleen koehenkilo.anna antoi switch defaultin,
koska puuttui case 0 tietokannan attribuutti "koehenkiloNro", joka
piti antaa String muodossa tietokannalle (ei luokan attribuuttina).

Ongelma 2.
- O: Comtestit eivät toimi ja palauttivat tietokantavirheen.
Ongelma Koehenkilot-luokan etsi-metodissa.
- R: Koehenkilo-luokan metodi String anna, jota Koehenkilot etsi-metodi
kaytti, palautti luokan attribuutin kun olisi pitänyt palauttaa tietokannalle
hakuehto String muodossa.

Ongelma 3.
- O: Kysymykset luokan testissä ja toiminnassa jokin bugi, joka palauttaa
väärää tietoa ja herjaa välillä UI:ssa virhettä "_CONSTRAINT.. UNIQUE
failed: Kysymykset.id
- EI RATKAISTU kokonaan, esiintyy aina välillä alussa yhden kerran
lisättäessä uusi kysymys.
	- Virheilmoitus: SQLITE_CONSTRAINT Abort due to constraint violation
	(UNIQUE constraint failed: Kysymykset.id)
	- Tod. näk. ensimmainen luonti ei mene oikealle id:lle
	- tarkistaId, setId ja parse metodeja korjattu.
	Testit menevät lävitse.