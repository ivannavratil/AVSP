# Analiza velikih skupova podataka (AVSP)

U ovom repozitoriju se nalaze rješenja laboratorijskih vježbi iz predmeta Analiza velikih skupova podataka održanog 2021. godine.

# Laboratorijske vježbe
 ## 1. Laboratorijska vježba
 U laboratorijskoj vježbi je potrebno uz pomoć Simhash algoritma vršiti identifikaciju sličnih tekstova. Identifikaciju sličnih tekstova treba provesti slijednim pretraživanjem sažetaka svih tekstova te uporabom tehnike sažimanja osjetljivog na bliskost (eng. Locality Sensitive Hashing, LSH ).
 
### SPRUT autograder 
#### Točnost:
 - slijedno pretraživanje - 4/4
 - LSH - 2/2
#### Performanse:
- slijedno pretraživanje (vremensko ograničenje: 20s)
    - test2 - 1.01s, test3 - 1.05s, test4 - 0.90s, test5 - 0.98s
- LSH (vremensko ograničenje: 30s)
    - test0 - 2.1s, test1 - 9.35s

## 2. Laboratorijska vježba
U laboratorijskoj vježbi zadatak je ostvariti algoritam za pronalažanje čestih skupova predmeta PCY (Park-Chen-Yu). Na laboratorijskoj vježbi skup podataka se sastoji od odjeljaka (košara), a svaki odjeljak se sastoji od više predmeta. Potrebno je pronaći podskupove predmeta koji se pojavljuju u najvećem broj košara.

### SPRUT autograder
#### Točnost:
- PCY (Park-Chen-Yu) - 2/2
#### Performanse:
- PCY (Park-Chen-Yu) (vremensko ograničenje: 40s)
    - tiny2 - 0.26s, test3 - 6.53s

## 3. Laboratorijska vježba
U laboratorijskoj vježbi zadatak je ostvariti algoritam za preporučivanje zasnovan na tehnici suradničkog filtriranja. Implementirana su oba osnovna principa suradničkog sortiranja, item-item pristup te user-user pristup.

### SPRUT autograder
#### Točnost:
- Suradničko filtriranje - 3/3
#### Performanse:
- Suradničko filtriranje (vremensko ograničenje: 10s)
  - test1 - 0.74s, test2 - 0.64s, test3 - 0.71s
