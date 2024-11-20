import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class Dashboard extends JFrame {
    private JLabel waterLevelLabel;
    private JLabel pumpStatusLabel;
    private JTextArea pumpHistoryArea;
    private Map<String, ImageIcon> waterLevelImages;
    private JButton startPumpButton;
    private JButton stopPumpButton;
    private Tank tank;

    public Dashboard(Tank tank) {
        this.tank = tank;

        setTitle("Dashboard");
        setSize(500, 400);
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

        // Control buttons
        JPanel controlPanel = new JPanel();
        startPumpButton = new JButton("Iniciar Bomba");
        stopPumpButton = new JButton("Parar Bomba");
        controlPanel.add(startPumpButton);
        controlPanel.add(stopPumpButton);
        add(controlPanel, BorderLayout.EAST);

        // Button actions
        startPumpButton.addActionListener(e -> updatePumpStatus("Bomba 1 ligada, Bomba 2 desligada"));
        stopPumpButton.addActionListener(e -> updatePumpStatus("Bomba 1 e 2 desligada"));

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

    public void setWaterLevel(int level) {
        switch (level) {
            case 0:
                updateWaterLevel("low");
                break;
            case 1:
                updateWaterLevel("medium");
                break;
            case 2:
                updateWaterLevel("high");
                break;
        }
    }

    public void setPumpStatus(String status) {
        updatePumpStatus(status);
    }

    public void refreshDashboard() {
        int codeStatus = tank.getCode_dashboard();

        switch (codeStatus) {
            case 4: // 1111: level_low, level_medium, level_high, water_flow = ON
                setWaterLevel(2);
                setPumpStatus("Tanque status: Caixa d'água com 100% e recebendo água da rua");
                break;
            case 3: // 0111: level_low, level_medium, water_flow = ON
                setWaterLevel(1);
                setPumpStatus("Tanque status: Caixa d'água com 50%, recebendo água da rua");
                break;
            case 2: // 0010: level_low, water_flow = ON
                setWaterLevel(0);
                setPumpStatus("Tanque status: Atenção! Caixa d'água 10%, mas recebendo água da rua.");
                break;
            case 1: // 0001: Apenas water_flow = ON
                setWaterLevel(0);
                setPumpStatus("Tanque status: ALERTA! Caixa d'água vazia, mas recebendo água da rua!");
                break;
            case 0: // 0000: Nenhum sensor está ativo
                setWaterLevel(0);
                setPumpStatus("Tanque status: EMERGENCIA! Caixa d'água vazia, sem água da rua!");
                break;
            default:
                setPumpStatus("Tanque status: Erros nos sensores, verificar!");
                break;
        }
    }

    public static void main(String[] args) {
        Tank tank = new Tank();
        Dashboard dashboard = new Dashboard(tank);

        // Simulate updates
        tank.setLevel_low();
        tank.setLevel_medium();
        tank.setLevel_high();
        tank.setWater_flow();
        dashboard.refreshDashboard();
    }
}
