// Source code is decompiled from a .class file using FernFlower decompiler.
// Source code is decompiled from a .class file using FernFlower decompiler.
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainMenuGui {
   public MainMenuGui() {
   }

   public void display() {
      JFrame frame = new JFrame("VCRTS - Vehicular Cloud Real-Time System");
      frame.setSize(400, 300);
      frame.setDefaultCloseOperation(3);
      frame.setLayout(new BorderLayout());
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(6, 1, 10, 10)); // Increased rows from 5 to 6
      panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      JButton clientLoginButton = new JButton("Client Login");
      JButton vehicleOwnerLoginButton = new JButton("Vehicle Owner Login");
      JButton checkClientInfoButton = new JButton("Check Client Info");
      JButton checkVehicleOwnerInfoButton = new JButton("Check Vehicle Owner Info");
      JButton serverClientButton = new JButton("Start Server & Client"); // new button
      JButton exitButton = new JButton("Exit");

      this.styleButton(clientLoginButton);
      this.styleButton(vehicleOwnerLoginButton);
      this.styleButton(checkClientInfoButton);
      this.styleButton(checkVehicleOwnerInfoButton);
      this.styleButton(serverClientButton); // style new button
      exitButton.setBackground(new Color(192, 57, 43));
      exitButton.setForeground(Color.WHITE);
      exitButton.setFocusPainted(false);
      exitButton.setFont(new Font("Arial", 1, 14));

      panel.add(clientLoginButton);
      panel.add(vehicleOwnerLoginButton);
      panel.add(checkClientInfoButton);
      panel.add(checkVehicleOwnerInfoButton);
      panel.add(serverClientButton); // add new button to layout
      panel.add(exitButton);

      frame.add(panel, "Center");

      clientLoginButton.addActionListener((e) -> {
         new ClientLoginGUI();
         frame.dispose();
      });

      vehicleOwnerLoginButton.addActionListener((e) -> {
         new VehicleOwnerLoginGUI();
         frame.dispose();
      });

      checkClientInfoButton.addActionListener((e) -> {
         try {
            BufferedReader clientReader = new BufferedReader(new FileReader("clients.txt"));
            try {
               StringBuilder clientInfo = new StringBuilder();
               String line;
               while((line = clientReader.readLine()) != null) {
                  clientInfo.append(line).append("\n");
               }
               JOptionPane.showMessageDialog(frame, "Client Info:\n" + clientInfo.toString());
            } catch (IOException ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(frame, "Error reading clients file: " + ex.getMessage());
            } finally {
               clientReader.close();
            }
         } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error reading clients file: " + ex.getMessage());
         }
      });

      checkVehicleOwnerInfoButton.addActionListener((e) -> {
         try {
            BufferedReader vehicleOwnerReader = new BufferedReader(new FileReader("vehicle_owners.txt"));
            try {
               StringBuilder vehicleOwnerInfo = new StringBuilder();
               String line;
               while((line = vehicleOwnerReader.readLine()) != null) {
                  vehicleOwnerInfo.append(line).append("\n");
               }
               JOptionPane.showMessageDialog(frame, "Vehicle Owner Info:\n" + vehicleOwnerInfo.toString());
            } catch (IOException ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(frame, "Error reading vehicle owners file: " + ex.getMessage());
            } finally {
               vehicleOwnerReader.close();
            }
         } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error reading vehicle owners file: " + ex.getMessage());
         }
      });

      serverClientButton.addActionListener((e) -> {
         new Thread(() -> {
            try {
               ServerApp.main(null); // call your server
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }).start();

         new Thread(() -> {
            try {
               Thread.sleep(1000); // small delay to let the server start first
               ClientApp.main(null); // call your client
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }).start();

         JOptionPane.showMessageDialog(frame, "Server and Client started.");
      });

      exitButton.addActionListener((e) -> {
         frame.dispose();
      });

      frame.setVisible(true);
   }

   private void styleButton(JButton button) {
      button.setBackground(new Color(59, 89, 182));
      button.setForeground(Color.WHITE);
      button.setFocusPainted(false);
      button.setFont(new Font("Arial", 1, 14));
   }
}
