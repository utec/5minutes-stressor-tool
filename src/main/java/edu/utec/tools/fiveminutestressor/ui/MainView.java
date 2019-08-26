/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utec.tools.fiveminutestressor.ui;

import java.awt.Dialog;
import java.awt.FlowLayout;
import javax.swing.JFrame;

import edu.utec.tools.fiveminutestressor.ui.controllers.HumanStressorController;

/**
 *
 * @author jarvis
 */
public class MainView extends javax.swing.JFrame {

	/**
	 * Creates new form MainView
	 */
	public MainView() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanelMainContainer = new javax.swing.JPanel();
		jMenuBarMain = new javax.swing.JMenuBar();
		jMenuProject = new javax.swing.JMenu();
		jMenuItemNewStressHuman = new javax.swing.JMenuItem();
		jMenuItemNewStressDevelopers = new javax.swing.JMenuItem();
		jMenuHelp = new javax.swing.JMenu();
		jMenuItemAbout = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("5 Minutes Stressor Tool");

		jPanelMainContainer.setLayout(new java.awt.BorderLayout());

		jMenuProject.setText("Project");

		jMenuItemNewStressHuman.setText("New  stress test for humans");
		jMenuItemNewStressHuman.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemNewStressHumanActionPerformed(evt);
			}
		});
		jMenuProject.add(jMenuItemNewStressHuman);

		jMenuItemNewStressDevelopers.setText("New stress test for developers");
		jMenuProject.add(jMenuItemNewStressDevelopers);

		jMenuBarMain.add(jMenuProject);

		jMenuHelp.setText("Help");

		jMenuItemAbout.setText("About");
		jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemAboutActionPerformed(evt);
			}
		});
		jMenuHelp.add(jMenuItemAbout);

		jMenuBarMain.add(jMenuHelp);

		setJMenuBar(jMenuBarMain);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanelMainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanelMainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jMenuItemNewStressHumanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemNewStressHumanActionPerformed
		if (panelHttpTestForHumans == null) {
			panelHttpTestForHumans = new JPanelHttpTestForHumans();
		}

		jPanelMainContainer.removeAll();
		jPanelMainContainer.add(panelHttpTestForHumans);
		jPanelMainContainer.revalidate();
		jPanelMainContainer.repaint();
		
		//modified
		HumanStressorController humanStressorController = new HumanStressorController(this);

	}// GEN-LAST:event_jMenuItemNewStressHumanActionPerformed

	private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemAboutActionPerformed

		if (jDialogAbout == null) {
			jDialogAbout = new JDialogAbout(this, true);
		}
		jDialogAbout.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

		jDialogAbout.pack();
		jDialogAbout.setLocationRelativeTo(null);
		jDialogAbout.setVisible(true);
	}// GEN-LAST:event_jMenuItemAboutActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainView mainView = new MainView();
				mainView.setVisible(true);
				mainView.setExtendedState(mainView.getExtendedState() | JFrame.MAXIMIZED_BOTH);				
			}
		});
	}
	
	

	public JPanelHttpTestForHumans getPanelHttpTestForHumans() {
		return panelHttpTestForHumans;
	}

	private JDialogAbout jDialogAbout;
	private JPanelHttpTestForHumans panelHttpTestForHumans;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenuBar jMenuBarMain;
	private javax.swing.JMenu jMenuHelp;
	private javax.swing.JMenuItem jMenuItemAbout;
	private javax.swing.JMenuItem jMenuItemNewStressDevelopers;
	private javax.swing.JMenuItem jMenuItemNewStressHuman;
	private javax.swing.JMenu jMenuProject;
	private javax.swing.JPanel jPanelMainContainer;
	// End of variables declaration//GEN-END:variables
}
