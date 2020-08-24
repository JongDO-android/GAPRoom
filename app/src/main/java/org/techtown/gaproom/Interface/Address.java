package org.techtown.gaproom.Interface;

import java.util.Observer;

public interface Address {
    void registerobserver(Observer observer);
    void removeobserver(Observer observer);
    void notifyobserver(Observer observer);
}

