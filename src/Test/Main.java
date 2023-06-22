package Test;

import java.nio.file.*;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Specify the path
        Path path = Paths.get("C:\\Users\\anton\\Programovani\\ChatAplicationServer\\src");
        try {
        	
            Main.processPath(path);

        } catch (IOException e) {
            System.err.println("Error while reading or writing a file: " + e.getMessage());
        }
    }

    
    private static void processPath(Path path) throws IOException {
    	 // Specify the path
    	
        
            // Check if the path is a directory
            if (Files.isDirectory(path)) {
            	
                // Process all .txt files in the directory
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                    for (Path filePath : directoryStream) {
                    	Main.processPath(filePath);
                    }
                }
            } else {

                // Process the single file
                processFile(path);
            }

    }
    private static void processFile(Path filePath) throws IOException {
        // Define the pattern
        Pattern pattern = Pattern.compile("System\\.out\\.println\\(.*\\)\\s*;");

        // Create a list to hold the lines to keep
        List<String> linesToKeep = new ArrayList<>();

        // Read all lines of the file
        for (String line : Files.readAllLines(filePath)) {
        	
            // Create a matcher for the line
            Matcher matcher = pattern.matcher(line);

            // If the line does not contain the pattern, add it to the lines to keep
            if (!matcher.find()) {
                linesToKeep.add(line);
            }
        }

        // Write the remaining lines back to the file
             Files.write(filePath, linesToKeep, StandardOpenOption.TRUNCATE_EXISTING);
    }
}

