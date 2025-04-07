public class LauncherApp {
    public static void main(String[] args) {
        // Start the server in a new thread to avoid blocking the main thread
        System.out.println("Starting the server...");
         // Create a new thread for the server
         // This allows the server to run in the background while the client starts
         // and avoids blocking the main thread.
        new Thread(() -> {
            ServerApp.main(null);
        }).start();

        // Wait a moment for the server to start
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Start the client
        ClientApp.main(null);
    }
}

