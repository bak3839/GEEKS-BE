package com.example.geeks.responseDto;

import com.example.geeks.Enum.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DetailDTO {
    private Long detailId;

    private boolean smoking;

    private boolean habit;

    private Ear ear;

    private Time sleep;

    private Time wakeup;

    private Out out;

    private Cleaning cleaning;

    private Tendency tendency;

    private boolean saved;

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
