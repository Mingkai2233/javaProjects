package org.example.springbootheadlinepart.pojo;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Headline {
    private Integer hid;
    private String title;
    private Integer type;
    private Integer pageViews;
    private Integer publisher;
    private Integer pastHours;
    private String article;
}
