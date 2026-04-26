package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fine {
    private String fineID;
    private String recordID;
    private String userID;
    private int fineAmount;
    private String status;
    private LocalDate paidDate;
}
