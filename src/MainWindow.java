/*
 *  This file is part of SimpleChess.
 *  Copyright (C) 2016, Vladimir Fekete
 *
 *  SimpleChess is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SimpleChess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Vladimir Fekete
 */
public class MainWindow extends javax.swing.JFrame implements IHomeCardAction, WindowListener {

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        this.initComponents();
        this.initWindow();

        this.gameCard = new GameCard();
        this.homeCard = new HomeCard(this);

        this.homeCard.setLocation(0, 0);
        this.gameCard.setLocation(0, 0);

        this.mainPanel.add(homeCard, homeCard.getName());
        this.mainPanel.add(gameCard, gameCard.getName());

        CardLayout cl = (CardLayout) mainPanel.getLayout();

        if (cl != null) {
            cl.first(mainPanel);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initWindow() {
        this.setLocationByPlatform(true);
        this.setSize(640, 480);
        this.setResizable(false);
        this.setTitle("Simple Chess");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this);
    }

    //
    //   IHomeCardAction interface
    //
    @Override
    final public void startNewGame() {
        if (gameCard == null) {
            return;
        }

        gameCard.startNew();
        SwitchToCard(gameCard.getName());
    }

    @Override
    final public void loadGameFromFile(Path filePath) {

        if (gameCard == null) {
            return;
        }

        GameReader reader = new GameReader();

        reader.loadFromFile(filePath);

        if (reader.getIsRead() == false) {
            JOptionPane.showMessageDialog(this, "Cannot load this game.", "Error loading game", JOptionPane.ERROR_MESSAGE);
            return;
        }

        gameCard.start(reader.getFigures(), reader.getGameState());
        SwitchToCard(gameCard.getName());
    }

    @Override
    final public void saveGameToFile(Path filePath) {

        if (gameCard == null) {
            return;
        }

        GameWriter writer = new GameWriter();

        IGame game = (IGame) gameCard;

        writer.writeToFile(filePath, game.getState(), game.getFigures());

        if (writer.getIsWritten() == false) {
            JOptionPane.showMessageDialog(this, "Cannot writer game to file.", "Error writting game", JOptionPane.ERROR_MESSAGE);
        }
    }

    //
    //   WindowListener interface
    //
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {

        IGame game = (IGame) this.gameCard;

        if (game != null
                && game.getState() != IGame.State.NotStarted // non-atomic operation?
                && game.getState() != IGame.State.ChessMate) {

            switch (JOptionPane.showConfirmDialog(this, "Game is still in progress, do you want to save it?", "Save game", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                case JOptionPane.CANCEL_OPTION:
                    return;
                case JOptionPane.YES_OPTION: {

                    JFileChooser chooser = new JFileChooser();

                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Simple chess game (*.scgame)", "scgame"));
                    chooser.setAcceptAllFileFilterUsed(false);

                    if (JFileChooser.APPROVE_OPTION != chooser.showSaveDialog(this)) {
                        return;
                    }

                    this.saveGameToFile(chooser.getSelectedFile().toPath());
                }
                break;
            }
        }

        this.dispose();
    }

    private void SwitchToCard(String cardName) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();

        if (cl != null) {
            cl.show(mainPanel, cardName);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables

    private final GameCard gameCard;

    private final HomeCard homeCard;

}
