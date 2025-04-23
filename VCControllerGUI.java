
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

public class VCControllerGUI {
    private JFrame frame;
    private DefaultListModel<String> pendingRequestsModel;
    private JList<String> pendingRequestsList;
    private ConcurrentHashMap<String, String> requestKeys = new ConcurrentHashMap<>();

    public VCControllerGUI() {
        frame = new JFrame("VC Controller Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        pendingRequestsModel = new DefaultListModel<>();
        pendingRequestsList = new JList<>(pendingRequestsModel);
        pendingRequestsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(pendingRequestsList);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton acceptButton = new JButton("Accept Selected");
        JButton rejectButton = new JButton("Reject Selected");
        JButton acceptAllButton = new JButton("Accept All");
        JButton rejectAllButton = new JButton("Reject All");
        JButton backButton = new JButton("Back to Main Menu");

        styleButton(acceptButton);
        styleButton(rejectButton);
        styleButton(acceptAllButton);
        styleButton(rejectAllButton);

        backButton.setBackground(new Color(192, 57, 43));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(acceptAllButton);
        buttonPanel.add(rejectAllButton);
        buttonPanel.add(backButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        acceptButton.addActionListener(e -> handleAcceptAction());
        rejectButton.addActionListener(e -> handleRejectAction());
        acceptAllButton.addActionListener(e -> handleAcceptAllAction());
        rejectAllButton.addActionListener(e -> handleRejectAllAction());
        backButton.addActionListener(e -> {
            MainMenu.showMainMenu();
            frame.dispose();
        });

        VCControllerServer.setGUI(this);
        frame.setVisible(true);
    }

    private void handleAcceptAction() {
        int selectedIndex = pendingRequestsList.getSelectedIndex();
        if (selectedIndex != -1) {
            String request = pendingRequestsModel.getElementAt(selectedIndex);
            String requestKey = requestKeys.get(request);

            int response = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to ACCEPT this request?\n" + request,
                    "Confirm Acceptance", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // VCControllerServer.setDecisionForRequest(requestKey, "ACCEPT");
                pendingRequestsModel.remove(selectedIndex);
                requestKeys.remove(request);
                JOptionPane.showMessageDialog(frame, "Request accepted!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a request first.");
        }
    }

    private void handleRejectAction() {
        int selectedIndex = pendingRequestsList.getSelectedIndex();
        if (selectedIndex != -1) {
            String request = pendingRequestsModel.getElementAt(selectedIndex);
            String requestKey = requestKeys.get(request);

            int response = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to REJECT this request?\n" + request,
                    "Confirm Rejection", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // VCControllerServer.setDecisionForRequest(requestKey, "REJECT");
                pendingRequestsModel.remove(selectedIndex);
                requestKeys.remove(request);
                JOptionPane.showMessageDialog(frame, "Request rejected!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a request first.");
        }
    }

    private void handleAcceptAllAction() {
        if (pendingRequestsModel.size() > 0) {
            int response = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to accept ALL pending requests?",
                    "Confirm Accept All", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                for (int i = 0; i < pendingRequestsModel.size(); i++) {
                    String request = pendingRequestsModel.getElementAt(i);
                    String requestKey = requestKeys.get(request);
                    // VCControllerServer.setDecisionForRequest(requestKey, "ACCEPT");
                }
                pendingRequestsModel.clear();
                requestKeys.clear();
                JOptionPane.showMessageDialog(frame, "All requests accepted!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No pending requests to accept.");
        }
    }

    private void handleRejectAllAction() {
        if (pendingRequestsModel.size() > 0) {
            int response = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to reject ALL pending requests?",
                    "Confirm Reject All", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                for (int i = 0; i < pendingRequestsModel.size(); i++) {
                    String request = pendingRequestsModel.getElementAt(i);
                    String requestKey = requestKeys.get(request);
                    // VCControllerServer.setDecisionForRequest(requestKey, "REJECT");
                }
                pendingRequestsModel.clear();
                requestKeys.clear();
                JOptionPane.showMessageDialog(frame, "All requests rejected!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No pending requests to reject.");
        }
    }

    public static void addPendingRequest(String requestType, String data, String requestKey) {
        String displayText = requestType + ": " + data;
        VCControllerGUI gui = VCControllerServer.gui;
        if (gui != null) {
         //   gui.pendingRequestsModel.addElem0ent(displayText);
            gui.requestKeys.put(displayText, requestKey);
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}