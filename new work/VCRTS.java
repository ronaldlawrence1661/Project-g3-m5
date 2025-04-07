package com.vcrts;

import com.vcrts.controller.VCControllerServer;
import com.vcrts.gui.MainMenu;

public class VCRTS {
    public static void main(String[] args) {
        new Thread(() -> VCControllerServer.startServer()).start();
        MainMenu.showCoverPage();
    }
}