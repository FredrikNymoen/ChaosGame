# Mappevurdering prog 2 - Chaos Game
Fredrik Nymoen & Amund Larsen

## Beskrivelse av prosjektet
Dette prosjektet innebar å utvikle et «Chaos Game» som genererer fraktaler. Den skal ha funksjonalitet som valg av type transformasjon, endring av steg, endring av minimums og maksimumskoordinater, og justering av Julia og Affine transformasjoner

## Brukerveiledning
Velg en transformasjon og fyll ut de nødvendige feltene før man kan vise transformasjonen ved hjelp av «show»- eller «Make fractal with iterative transformation» knappen. Er ikke de nødvendige feltene fylt ut, vil de tomme feltene få rød kant.
| Brukerinteraksjon | Beskrivelse |
| ------ | ------ |
|  Transformasjonsvalg  |   Velg en transformasjon ved å trykke på den korresponderende knappen.    |
|   Valg av skritt     |   Dra slider dotten for å velge antall skritt.     |
|Minimums-koordinater og maksimums-koordinater | Desimaltall. Anbefalte koordinater: Barnsley: (-4,-1), (4,10). Julia: (-2,-2), (2,2). Sierpinski: (0,0), (1,1). Maple-Tree: (-6,-4), (6,6). |
| Velg hvordan julia fraktalet skal genereres.|Trykk på knappen under «julia-constant». |
|Julia reell- og imaginær tall. | Desimaltall. Anbefaler tall mellom -0.9 og 0.9.|
| Affine matrisefelter og vektorfelter. | Desimaltall |
| Affine «add» og «remove» knapp. | Trykk på «add» for å legge til en transformasjon bestående av en matrise og en vektor. Trykk på «remove» for å fjerne en transformasjon. 1 rad vil alltid vises. |
| Show knapp. | Trykk på show for å vise fraktalet. |
| Make fractal with iterative transformation mode. | Lager fraktaler med å ikke velge en tilfeldig transformasjon, fra et punkt, men setter et punkt for alle transformasjoner fra hvert punkt. Tar naturligvis ikke hensyn til «steps». |
| Heatmap Color Mode | En checkbox som når krysset av vil vise hvilke piksler i fraktalet som er truffet mer enn de andre. |
| Copy last shown transformation | Kopierer den siste transformasjonen som har blitt vist når trykket på show knappen. |
| Exit knappen | Går ut av applikasjonen. |


## Hvordan kjøre applikasjonen
Applikasjonen må kjøres med mvn javafx:run.