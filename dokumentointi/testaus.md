# Test document

## Test coverage

![Test coverage](https://github.com/Jsos17/CalculatorApp/blob/master/dokumentointi/Test_coverage.png)

As can be seen about 99% of instructions and about 96 % of branches are covered by the tests. 

Some cases seem impossible to actually happen in actual code. For example, if a valid input is injected to the postfixEvaluator, then when a mathematical operator is found in the ArrayDeque of Strings, the stack should never be empty since it would mean that the expression was not valid in the first place, and this error would have been catched earlier. So testing the method indirectly through expressionEvaluation does not cover this branch.

These kind of situations also cause the inability to fully test all possibile branches of the code, since valid input just won't lead there, and the algorithm should only receive valid inputs, if everything else works correctly. 

Thus, in some cases, tests for these "impossible cases" were made.

Certain branches are missed in the tests, because of the above mentioned phenomena.

## Unit testing

Unit test classes were made for very class. Some of the more complex ExpressionEvaluator tests were created by inputting the same (or almost the same expression) into WolframAlpha and then manually copying the result. (Note: it is important to check that radians are used in WolframAlpha, and that the appropriate logarithmic function (ln or base-10 logarithm) are used to obtain the same results.)

The classes with database functionality, were tested using TemporaryFolder-rules.

## Integration testing

The class ExpressionEvaluatorTest partially contains integration tests, since ExpressionEvaluator uses InputParser and CalculatorService as parameters. Therefore, creating additional integration test classes was seen as redundant, since it only would have tested the same functionality.

Since almost all of the complex logic happens between ExpressionEvaluator, InputParser and CalculatorService, it is deemed that the tests of the ExpressionEvaluatorTest class are enough for integration testing.

Additionally, the ExpressionMemory also uses ExpressionEvaluator as a parameter, and thus the tests of ExpressionMemoryTest also serve partially as integration tests. 

The only additional way of doing integration testing would be testing the graphical user interface, but since testing it is not taught or required it was omitted.

## System testing

The user interface was tested manually, and mainly just trying to cause errors. 

Additionally, the jar-file was opened by left-clicking its icon and also through command line. 

This test revealed that, that left-clicking almost certainly results in the database being created somewhere else than the directory where the jar file is (probably the root directory). On the contrary, command line execution of the jar creates the database inside the same directory.

A reliable way of ensuring access to the right database, is opening the jar file using the command line.

## Remaining deficiencies in the application/in testing

It goes without saying that the ability to automatically create random mathematical expressions as inputs and having the accompanying correct results would have been greatly useful in testing the ExpressionEvaluator class in particular. Now the tests leave a feeling that the ExpressionEvaluator class "most likely works correctly".

Some unit tests lean heavily on dependency injections from other classes and are thus more of an integration test than a unit test. This was noticiable during programming, because when errors occurred, they occured in multiple tests at the same time.

### A curious INFO-message related most likely to javafx code

The following message can be seen, if expressions are deleted and the databaselist is rapidly scrolled (the exact mechanism of producing this INFO-message is not clear to me):

    INFO: index exceeds maxCellCount. Check size calculations for class com.sun.javafx.scene.control.skin.ListViewSkin$2

Technically it is not an error, and does not cause any disruption to the program. Additionally, a quick internet search [StackOverFlow](https://stackoverflow.com/questions/30683685/why-do-i-get-this-weird-warning-when-i-scroll-to-the-end-of-a-listview-in-javafx?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa) revealed no significant harm caused by this and also no quick solution. So it was decided to simply ignore the message. 
