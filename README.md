# README #

## Presentation ##

A simple Android app to handle a complex numerical problem.  
Solves the problem of splitting multiple expenses supported by more than one person.

## Input ##

ATTENTION: a person's name is a ONE ONLY word!
*Good names*: Luke, 🎮, ✌🏻😉, M1ch3l3
**Bad names**: ~~Donald Trump~~, ~~🤪 🤳~~, ~~Test 123~~

### Example 1 ###

Example of input:
```text
6.20 John beer
12.00 Adam food
17.80 Paul gasoline
```

The total will be 36 and the output will be:
```text
Total expense per person:
John = 6.20
Adam = 12.00
Paul = 17.80
---

Situation per person:
Adam = 0.00 (you are OK)
John = 5.80 (you pay back)
Paul = -5.80 (you get back)
---

Repartition:
John owes Paul 5.80
---

```

### Example 2 (someone pay for you) ###

**Another** example of input might be:
```text
41.20 🐗 dinner
23.20 🐷 supermarket
3.60 🐷 ice cream (🐗)
```

The third line means that 🐗 did not have the money to buy an ice cream, so 🐷 paid for him.
In this example, the total will be 68 and the output will be:
```text
Total expense per person:
🐗 = 41.20
🐷 = 26.80
---

Situation per person:
🐗 = -5.40 (you get back)
🐷 = 5.40 (you pay back)
---

Repartition:
🐷 owes 🐗 5.40
---

```

Swiping from right to left, you'll also see the detail:
```text
🐗 = -5.40 (you get back)
➫ dinner - you get back 20.60
➫ supermarket - you pay back 11.60
➫ ice cream - you pay back 3.60

🐷 = 5.40 (you pay back)
➫ dinner - you pay back 20.60
➫ supermarket - you get back 11.60
➫ ice cream - you get back 3.60

```

### Example 3 ###

```text
20 XX (XX, MD) Hot chocolate
21.90 LG (LG, XX, SC)
11.6 GP (XX, MD) Beer
```

In this example, XX had an hot chocolate with MD and paid for both;
then, LG went on a dinner with XX and SC: LG paid for all;
finally, GP lent some money for XX and MD.  
The total will be 53.50 and the output will be:
```text
Total expense per person:
GP = 11.60
LG = 21.90
XX = 20.00
---

Situation per person:
GP = -11.60 (you get back)
LG = -14.60 (you get back)
MD = 15.80 (you pay back)
SC = 7.30 (you pay back)
XX = 3.10 (you pay back)
---

Repartition:
MD owes GP 1.20
MD owes LG 14.60
SC owes GP 7.30
XX owes GP 3.10
---

```

- - -

## Copyright ##

Copyright 2020 LUCA LORUSSO - http://lukelorusso.com  
