package cl.duoc.app;

import cl.duoc.vistas.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.out.println("No se pudo cambiar la apariencia.");
            }

            new LoginView().setVisible(true);
        });
    }
}