# Mutation Testing Tool

The [Mutation Testing Tool](https://github.com/HarisPasic/MutationTestingTool) allows you to test the completeness of your tests for a Java Maven project. It will apply various mutations to your project's source code and run your test suite for each mutation. 

If a mutant survives (all tests pass after the mutation is applied), then the corresponding portion of code may not be well-tested. Note that if a mutant doesn't survive (at least one test fails), this does not necessarily mean that your tests are full proof.
In summary, this tool helps you find sections of your code that could be more thoroughly tested.

### How do I use this tool?

The project to be tested must follow the Maven project structure : `src/main/java/*` for the source code and `src/test/*` for the tests. It must also contain a `pom.xml` file.

- Create the jar for the Mutation Testing Tool: `mvn clean compile assembly:single`
- Edit the configuration file `config.properties` to apply specific mutations (by default all mutations are tested) and put it in the same folder as the jar.
- Execute the jar with one or two arguments: project path and maven path (by default `mvn`).
  - `java -jar mutation_testing_tool.jar "/home/Documents/MyProject"`
  - `java -jar mutation_testing_tool.jar "/home/Documents/MyProject" "/usr/bin/mvn"`

The test report `mutation_testing_report.html` will be generated in the same folder as the jar file.

### What mutation operators can I apply to my source code?

- **Conditional expressions** > mutates the value of conditional expression  
  `a < b` becomes `a <= b`  
  `a <= b` becomes `a < b`
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

The Mutation Testing Tool creates a copy of the Maven project to be tested, and scans its `src` directory for source files.  
For each file with `.java` extension:

* An AST (Abstract Syntax Tree) representing the top-level class is created using [Spoon](https://github.com/INRIA/spoon). The selected mutation operators are added as Spoon processors to generate mutations.
* For each mutation that occurs:
	* The source file is updated in the cloned project
	* The test suite is run using the Maven command `mvn clean compile test`
	* The test results are analyzed to check if the mutant survived
	* The mutation is reverted in the AST
* When all the mutations on a source file are done, the original content of the file is restored, and the process moves on to the next file.

At the end of the process, the cloned project is deleted from disk and a HTML report is generated.

### Authors

- Arnaud Cornillon
- Haris Pasic
- Pierre Testart

### Licence

This project is licensed under the MIT License.
