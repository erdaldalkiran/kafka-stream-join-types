package com.erdaldalkiran.jointypes;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class XdockUser {
    Long id;
    String xDockName;
    Long userId;
    String userName;
}
