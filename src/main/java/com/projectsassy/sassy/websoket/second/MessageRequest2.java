package com.projectsassy.sassy.websoket.second;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequest2 {

    private String type;
    private String roomId;
    private String sendUserId;
    private String content;

}
