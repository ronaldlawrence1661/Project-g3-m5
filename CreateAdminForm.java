import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;

public class CreateAdminForm extends JFrame {
    private Object dataObject;
    private ObjectOutputStream outputStream;
    private String currentUsername;

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

        acceptButton.addActionListener(e -> handleApprovalFile(true));
        rejectButton.addActionListener(e -> handleApprovalFile(false));

        add(label);
        add(acceptButton);
        add(rejectButton);
    }

    private void handleApprovalDB(boolean isAccepted) {
        try {
            if (isAccepted) {
                if (dataObject instanceof Job) {
                    Job job = (Job) dataObject;
                    submitJob( job.getDate(), job.getDescription(), job.getDuration());
                }
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
    private void handleApprovalFile(boolean isAccepted) {
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

    private void submitJob( String jobDate, String jobDesc, int duration) {
        // Assuming Database is a class that handles database operations
        try {
            boolean success = Database.submitJob( jobDate, jobDesc, duration);
            if (success) {
                JOptionPane.showMessageDialog(this, "Job submitted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit job.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
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