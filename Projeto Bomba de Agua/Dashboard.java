import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

class Tank {
    private int waterLevel; // 0: low, 1: medium, 2: high
    private Dashboard dashboard;

    public Tank(Dashboard dashboard) {
        this.dashboard = dashboard;
        this.waterLevel = 0; // Default to low level
    }

    public void setWaterLevel(int level) {
        this.waterLevel = level;
        updateDashboard();
    }

    private void updateDashboard() {
        switch (waterLevel) {
            case 0:
                dashboard.updateWaterLevel("low");
                break;
            case 1:
                dashboard.updateWaterLevel("medium");
                break;
            case 2:
                dashboard.updateWaterLevel("high");
                break;
        }
    }
}

public class Dashboard extends JFrame {
    private JLabel waterLevelLabel;
    private JLabel pumpStatusLabel;
    private JTextArea pumpHistoryArea;

    private Map<String, ImageIcon> waterLevelImages;

    public Dashboard() {
        setTitle("Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize water level images
        waterLevelImages = new HashMap<>();
        waterLevelImages.put("low", new ImageIcon(getClass().getResource("/images/nivel25.png")));
        waterLevelImages.put("medium", new ImageIcon(getClass().getResource("/images/nivel50.png")));
        waterLevelImages.put("high", new ImageIcon(getClass().getResource("/images/nivel100.png")));

        // Water level stage
        waterLevelLabel = new JLabel();
        updateWaterLevel("low"); // Default to low level
        add(waterLevelLabel, BorderLayout.NORTH);

        // Pump status stage
        pumpStatusLabel = new JLabel("Bomba 1 e 2 desligada");
        add(pumpStatusLabel, BorderLayout.CENTER);

        // Pump history stage
        pumpHistoryArea = new JTextArea();
        pumpHistoryArea.setEditable(false);
        add(new JScrollPane(pumpHistoryArea), BorderLayout.SOUTH);

        setVisible(true);
    }

    public void updateWaterLevel(String level) {
        waterLevelLabel.setIcon(waterLevelImages.get(level));
    }

    public void updatePumpStatus(String status) {
        pumpStatusLabel.setText(status);
    }

    public void addPumpHistory(String history) {
        pumpHistoryArea.append(history + "\n");
    }

    public static void main(String[] args) {
        Dashboard dashboard = new Dashboard();
        Tank tank = new Tank(dashboard);

        // Simulate updates
        tank.setWaterLevel(1); // Medium level
        dashboard.updatePumpStatus("Bomba 1 ligada, Bomba 2 desligada");
        dashboard.addPumpHistory("Bomba 1 ligada por 2 horas");
        dashboard.addPumpHistory("Bomba 2 desligada por 3 horas");
    }
}