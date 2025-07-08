package model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class BorrowingRecords {

    private String recordID;
    private String userID;
    private String bookID;
    private LocalDate borrowDate;
    private LocalDate return_Date;
    private Integer Fine;
}