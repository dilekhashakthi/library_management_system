package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Book {
    private String bookID;
    private String ISBN;
    private String title;
    private String author;
    private String genre;
    private String availabilityStatus;
}
