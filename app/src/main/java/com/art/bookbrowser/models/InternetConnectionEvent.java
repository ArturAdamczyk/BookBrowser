package com.art.bookbrowser.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternetConnectionEvent {
    private boolean internetAvailable;
}
