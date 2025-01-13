package de.unistuttgart.iste.pe2.api.ToDos;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.StringWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Helper class for converting ToDo objects to CSV format.
 * Provides functionality to export ToDo data into a CSV string representation.
 */
public class CSVHelper {
    private static final String[] CSV_HEADERS = { "id", "title", "description", "finished", "assignees", "createdDate", "dueDate", "finishedDate", "category" };

    /**
     * Converts a collection of ToDo objects to CSV format.
     *
     * @param todos Collection of ToDo objects to be converted
     * @return String representation of the ToDos in CSV format
     */
    public static String TodosToCSV(Collection<ToDo> todos){
        StringWriter writer = new StringWriter();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(CSV_HEADERS)
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {

            for (ToDo todo : todos) {
                csvPrinter.printRecord(
                    todo.getId(),
                    todo.getTitle(),
                    todo.getDescription(),
                    todo.isFinished(),
                    todo.getFormatedAssignees(),
                    formatDate(todo.getCreatedDate()),
                    formatDate(todo.getDueDate()),
                    formatDate(todo.getFinishedDate()),
                    todo.getCategory()
                );
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return writer.toString();
    }

    /**
     * Formats a Date object to a string in "yyyy-MM-dd" format.
     *
     * @param date The date to be formatted
     * @return Formatted date string, or empty string if date is null
     */
    public static String formatDate(Date date){
        if(date == null){
            return "";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}