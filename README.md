# Ohjelmistotekniikan menetelmät project work: CalculatorApp

Here is the **README-file** for the project work **CalculatorApp**. This project is part of the course *Ohjelmistotekniikan menetelmät (Software engineering methods (unofficial translation))*

The CalculatorApp is, as the name suggests, a calculator. So far it only has the basic arithmetic operations (+,-,*,/,^) available. 

The user can input any expression by using the provided buttons, and if the expression can be parsed (i.e. brackets, operators, dots etc are placed correctly) provides the result as double value. If the input cannot be parsed, then the user is notified and is asked to correct the expression.

The app also shows recently used expressions, and the user can change how many, if any, expressions are kept in memory. This feature will be expanded to include database storage for user selected expressions.

## Documentation

[Käyttöohje/User manual](https://github.com/Jsos17/otm-harjoitustyo/blob/master/dokumentointi/kayttoohje.md)

[Vaatimusmäärittely/Software requirement specification](https://github.com/Jsos17/otm-harjoitustyo/blob/master/dokumentointi/vaatimusmaarittely.md)

[Työaikakirjanpito/Work log](https://github.com/Jsos17/otm-harjoitustyo/blob/master/dokumentointi/tyoaikakirjanpito.md)

[Arkkitehtuurikuvaus/Software architecture](https://github.com/Jsos17/otm-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

## Releases

[Viikko 5](https://github.com/Jsos17/CalculatorApp/releases)

## Command line

**Testing**

Tests can be run with the command:

    mvn test

Test coverage report can be run with the command:

    mvn jacoco:report

And it can be examined by opening the file *target/site/jacoco/index.html* in the chromium-browser for example.

**Generating an executable jar-file**

    mvn package

Generates an executable jar-file, *CalculatorApp-1.0-SNAPSHOT.jar*, inside the target directory. 

**Checkstyle**

Style checks that are specified in the file [checkstyle.xml](https://github.com/Jsos17/otm-harjoitustyo/blob/master/CalculatorApp/checkstyle.xml) can be run with the command:

    mvn jxr:jxr checkstyle:checkstyle

Errors, if any, can be examined by opening the file *target/site/checkstyle.html*.
