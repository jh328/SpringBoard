package com.nc13.springBoard.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyDTO {
    private int id;
    private int writerId;
    private int boardId;

    private String content;
    private String nickname;

    private Date entryDate;
    private Date modifyDate;

}
