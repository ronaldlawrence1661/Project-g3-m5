import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class VehicleOwnerSignUpGUI {
    public VehicleOwnerSignUpGUI(String username, String password) {
        JFrame frame = new JFrame("Vehicle Owner Sign Up");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));

        JTextField makeField = new JTextField();
        JTextField modelField = new JTextField();
        JButton signUpButton = new JButton("Sign Up");

        frame.add(new JLabel("Make:"));
        frame.add(makeField);
        frame.add(new JLabel("Model:"));
        frame.add(modelField);
        frame.add(signUpButton);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String make = makeField.getText();
                String model = modelField.getText();
                if (make.isEmpty() || model.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Make and model cannot be empty.");
                    return;
                }
                try (FileWriter writer = new FileWriter("vehicle_owners.txt", true)) {
                    writer.write(username + "," + password + "," + make + "," + model + "\n");
                    JOptionPane.showMessageDialog(frame, "Sign Up Successful!");
                    new VehicleOwnerLoginGUI();
                    frame.dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error writing to vehicle owners file: " + ex.getMessage());
                }
            }
               });

        frame.setVisible(true);
    }
}
