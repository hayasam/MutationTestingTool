# Mutation Testing Tool

The Mutation Testing Tool allows you to test the completeness of your own tests. It will apply various mutations to your project source code and run your test suite for each mutation. 

If a mutant survives (a test passes after the mutation is applied), then this test is not well-written and you should rewrite it. If a mutant doesn't survive (the test fails), this does not mean the test is valid but at least, it passes against numerous edge cases. This tool helps you invalidate tests that were poorly written.

### How do I use the tool?

The project to be tested must follow the Maven project structure : `src/main/java/*` for the source code and `src/test/*` for the tests.

- Create the jar for the Mutation Testing Tool: `mvn clean compile assembly:single`
- Edit the configuration file `config.properties` to apply specific mutations (by default all mutations are tested) and put it in the same folder as the jar.
- Execute the jar with one or two arguments : project path and maven path (by default `mvn`).
  - `java -jar mutation_testing_tool.jar "/home/Documents/MyProject"`
  - `java -jar mutation_testing_tool.jar "/home/Documents/MyProject" "/usr/bin/mvn"`

The test report `mutation_testing_report.html` will be generated in the same folder as the jar.

### What mutations can I apply to my source code?

- **Conditional expressions** > mutates the value of conditional expression  
  `a < 3` becomes `a <= 3`  
  `a <= 3` becomes `a < 3`
- **Void methods** > empties the body of void methods
  ```java
  void compute() { // BEFORE
    doSomething();
  }
  ```
  ```java
  void compute() { } // AFTER
  ```
- **Conditional negation** > negates boolean expressions  
  `a || b` becomes `!(a || b)`
- **Default returned value** when returning a primitive type value from a method
  ```java
  int getX() { // BEFORE
    return x;
  }
  ```
  ```java
  int getX() { // AFTER
    return 0;
  }
  ```
- **Null default returned value** when returning an Object from a method  
  ```java
  List getList() { // BEFORE
    return myList;
  }
  ```
  ```java
  List getList() { // AFTER
    return null;
  }
  ```
- **AND (&&) and OR (||)** operator swaps  
`a || b` becomes `a && b`  
`a && b` becomes `a || b`  
- **+ and -** operator swaps  
`a + b` becomes `a - b`  
`a - b` becomes `a + b`

### How does it work?

<!--- TO DO -->

:sparkles: M A G I C :sparkles:

### Authors

- Arnaud Cornillon
- Haris Pasic
- Pierre Testart

### Licence

This project is licensed under the MIT License.
