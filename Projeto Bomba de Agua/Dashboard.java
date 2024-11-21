import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class Dashboard extends JFrame {
    private JLabel pumpStatusLabel;
    private JTextArea pumpHistoryArea;
    private JButton startPump1Button;
    private JButton stopPump1Button;
    private JButton startPump2Button;
    private JButton stopPump2Button;

    private Pumpss pumpss; // Instância de Pumpss
    private TankPanel tankPanel; // Painel para desenhar o tanque

    public Dashboard(Pumpss pumpss) {
        this.pumpss = pumpss;

        // Configuração da Janela
        this.setTitle("Dashboard");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Painel Gráfico do Tanque
        tankPanel = new TankPanel();
        this.add(tankPanel, BorderLayout.CENTER);

        // Área de Status das Bombas
        pumpStatusLabel = new JLabel("Status das Bombas: Desconhecido");
        this.add(pumpStatusLabel, BorderLayout.NORTH);

        // Área de Histórico
        pumpHistoryArea = new JTextArea(10, 30);
        pumpHistoryArea.setEditable(false);
        this.add(new JScrollPane(pumpHistoryArea), BorderLayout.SOUTH);

        // Painel de Botões
        JPanel buttonPanel = new JPanel();
        startPump1Button = new JButton("Ligar Bomba 1");
        stopPump1Button = new JButton("Desligar Bomba 1");
        startPump2Button = new JButton("Ligar Bomba 2");
        stopPump2Button = new JButton("Desligar Bomba 2");

        buttonPanel.add(startPump1Button);
        buttonPanel.add(stopPump1Button);
        buttonPanel.add(startPump2Button);
        buttonPanel.add(stopPump2Button);

        this.add(buttonPanel, BorderLayout.EAST);

        // Ações dos Botões
        startPump1Button.addActionListener(e -> startPump(1));
        stopPump1Button.addActionListener(e -> stopPump(1));
        startPump2Button.addActionListener(e -> startPump(2));
        stopPump2Button.addActionListener(e -> stopPump(2));

        // Atualização Periódica
        startDashboardUpdater();

        this.setVisible(true);
    }

    private void startPump(int pumpNumber) {
        if (pumpNumber == 1) {
            pumpss.setWater_pump1(); // Liga a bomba 1
        } else if (pumpNumber == 2) {
            pumpss.setWater_pump2(); // Liga a bomba 2
        }
        refreshDashboard();
    }

    private void stopPump(int pumpNumber) {
        if (pumpNumber == 1) {
            pumpss.setWater_pump1(); // Desliga a bomba 1
        } else if (pumpNumber == 2) {
            pumpss.setWater_pump2(); // Desliga a bomba 2
        }
        refreshDashboard();
    }

    void refreshDashboard() {
        // Atualiza o painel do tanque
        tankPanel.setWaterLevel(pumpss.getWaterLevel());
        
                // Atualiza o status das bombas
                pumpStatusLabel.setText("Status Bomba 1: " + pumpss.getPump1Status() +
                        " | Bomba 2: " + pumpss.getPump2Status());
        
                // Atualiza o histórico
                pumpHistoryArea.append("Bomba 1 - Tempo Ligada: " + pumpss.getPump1TotalWorkedTime() + " minutos\n");
                pumpHistoryArea.append("Bomba 2 - Tempo Ligada: " + pumpss.getPump2TotalWorkedTime() + " minutos\n");
            }
        
            private void startDashboardUpdater() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        refreshDashboard();
                    }
                }, 0, 5000); // Atualiza a cada 5 segundos
            }
        
            // Painel Gráfico para Representar o Tanque
            class TankPanel extends JPanel {
                private int waterLevel = 0; // Nível de água (0-100)
        
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
        
                    // Desenho do Tanque
                    g.setColor(Color.GRAY);
                    g.fillRect(50, 50, 200, 300); // Contorno do tanque
        
                    // Nível de Água
                    g.setColor(Color.BLUE);
                    int waterHeight = (300 * waterLevel) / 100;
                    g.fillRect(50, 350 - waterHeight, 200, waterHeight);
        
                    // Sensores e Indicadores de Nível
                    g.setColor(Color.RED);
        
                    // Linha e texto do nível de 100%
                    g.drawLine(50, 50, 250, 50);
                    g.drawString("100%", 260, 55);
        
                    // Linha e texto do nível de 50%
                    g.drawLine(50, 200, 250, 200);
                    g.drawString("50%", 260, 205);
        
                    // Linha e texto do nível de 25%
                    g.drawLine(50, 275, 250, 275);
                    g.drawString("25%", 260, 280);
        
                    // Texto do Nível Atual
                    g.setColor(Color.BLACK);
                    g.drawString("Nível de Água: " + waterLevel + "%", 60, 370);
                }
        
                public void setWaterLevel(String waterLevel2) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'setWaterLevel'");
                }
        
                public void setWaterLevel(int level) {
            this.waterLevel = Math.max(0, Math.min(level, 100)); // Garante que o nível fique entre 0 e 100
            repaint(); // Re-desenha o painel
        }
    }

    public static void main(String[] args) {
        Pumpss pumpss = new Pumpss(); // Instância da classe Pumpss
        new Dashboard(pumpss);       // Inicia o Dashboard com Pumpss
    }
}
