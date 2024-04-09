package com.admin.modules.system.entity.dto;

import lombok.Data;

@Data
public class StatWeekDataDTO {

    private String days;

    private int total;

    private int doCount;

    private int sendCount;

    private int refundCount;

    private int finishCount;
}
