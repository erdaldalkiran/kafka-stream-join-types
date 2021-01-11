package com.erdaldalkiran.jointypes;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class AdViewClick {
    Long id;
    Long userId;
}
