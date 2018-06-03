package com.example.daniel.seriousapp.utils;

import android.content.Context;
import android.location.Location;

/**
 * Created by joaosousa on 31/05/18.
 */

public interface ILocationMessage {
    void sendJMSObject(Location location);
}
