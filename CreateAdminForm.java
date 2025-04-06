import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class CreateAdminForm extends JFrame {
    private Object dataObject;
    private ObjectOutputStream outputStream;

    public CreateAdminForm(Object obj, ObjectOutputStream outputStream) {
        this.dataObject = obj;
        this.outputStream = outputStream;

        setTitle("VC Controller - Approve or Reject");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Approve request: " + obj.toString() + "?");
        JButton acceptButton = new JButton("Accept");
        JButton rejectButton = new JButton("Reject");

        acceptButton.addActionListener(e -> handleApproval(true));
        rejectButton.addActionListener(e -> handleApproval(false));

        add(label);
        add(acceptButton);
        add(rejectButton);
    }

    private void handleApproval(boolean isAccepted) {
        try {
            if (isAccepted) {
                saveToFile(dataObject);
                outputStream.writeObject("Accepted"); // Send approval response
            } else {
                outputStream.writeObject("Rejected"); // Send rejection response
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispose(); // Close window
    }

    private void saveToFile(Object obj) {
        String filename = obj instanceof Job ? "jobs.txt" : "cars.txt";

        try (FileOutputStream fos = new FileOutputStream(filename, true);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
            System.out.println("Saved to file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
