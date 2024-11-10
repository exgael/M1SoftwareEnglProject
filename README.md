# M1SoftwareEnglProject

This project is a Sudoku Solver application that allows you to load a Sudoku grid, solve it, and reset it as needed.

## Getting Started

To use this project, download the latest release JAR file from the [Releases](https://github.com/exgael/M1SoftwareEnglProject/releases/tag/v1.0) section. 

### Prerequisites

- **Java 21**: Make sure you have Java 21 or higher installed on your system to run the JAR file.

### Running the Application

- Running the lastest JAR file using the following command:
   ```bash
   java -jar SudokuSolver-v1.0.jar
   ```

### Using the Application

1. **Load a Sudoku Grid**: 
   - Click on **Load Grid** to start.
   - You can load a Sudoku grid by either:
     - **Copy-pasting the grid values** directly into the input prompt, or
     - **Providing a file path** to a text file containing the grid values.

2. **Example Grid Format**:
   ```plaintext
   0, 8, 6, 5, 7, 4, 0, 1, 2
   5, 1, 3, 0, 8, 9, 0, 7, 6
   0, 0, 0, 3, 0, 0, 0, 5, 9
   0, 0, 2, 0, 0, 0, 5, 0, 0
   0, 0, 0, 0, 2, 8, 0, 6, 0
   8, 3, 0, 6, 4, 0, 0, 0, 0
   0, 9, 8, 0, 0, 0, 7, 3, 0
   0, 0, 0, 4, 0, 0, 0, 8, 0
   2, 5, 0, 0, 0, 7, 6, 0, 0
   ```

3. **Solving and Resetting**:
   - After loading a grid, you can click **Solve** to see the solution, or **Reset** to clear the current grid.

